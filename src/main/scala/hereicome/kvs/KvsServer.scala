package hereicome.kvs

import cats._
import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import java.net.{ServerSocket, Socket}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import Kvs2._
import Protocol._

object KvsServer extends IOApp:
  def process[A](socket: Socket, kvsM: MVar[IO, A]) given ContextShift[IO], Kvs[A, Id]: IO[Unit] =
    import given NetIO._
    for
      b <- socket.readByte
      _ <- b match {
        case PUT => for
          k <- socket.readNBytes
          v <- socket.readNBytes
          _ = println(s"put k = $k, v = $v")
          kvs <- kvsM.take
          _ = kvs.put(k, v)
          _ <- kvsM.put(kvs)
          _ <- socket.writeByte(OK)
        yield ()
        case GET => for
          k <- socket.readNBytes
          _ = println(s"get k = $k")
          kvs <- kvsM.take
          v = kvs.get(k)
          _ <- kvsM.put(kvs)
          _ <- if v.isEmpty then socket.writeByte(Ok_NONE) else socket.writeByte(OK_SOME) >> socket.writeNBytes(v.get)
        yield ()
        case DEL => for
          k <- socket.readNBytes
          _ = println(s"del k = $k")
          kvs <- kvsM.take
          _ = kvs.del(k)
          _ <- kvsM.put(kvs)
          _ <- socket.writeByte(OK)
        yield ()
        case _ => socket.writeByte(FAIL)
      }
      _ <- NetIO.block(socket.close)
    yield ()

  def listen[A](serverSocket: ServerSocket, kvs: MVar[IO, A]) given ContextShift[IO], Kvs[A, Id]: IO[Unit] = for
    socket <- NetIO.block(serverSocket.accept)
    fiber <- process(socket, kvs).start(contextShift)
    _ <- listen(serverSocket, kvs)
  yield ()

  def run(args: List[String]): IO[ExitCode] =
    import given Kvs2.Kvs._
    given ioContextShift as ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutorService(Executors.newCachedThreadPool))
    for
      serverSocket <- NetIO.block(ServerSocket(3000))
      kvsM <- MVar.of[IO, Kvs.MapKvs](Kvs.MapKvs())
      _ <- listen(serverSocket, kvsM)
    yield ExitCode.Success