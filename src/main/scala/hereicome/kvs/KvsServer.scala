package hereicome.kvs

import cats._
import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import java.net.{ServerSocket, Socket}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import Kvs3._
import Protocol._

object KvsServer extends IOApp
  def process[A](socket: Socket, kvsM: MVar[IO, A])(given ContextShift[IO], Kvs[A, Id]): IO[Unit] =
    import NetIO.given
    import Command._, Reply._
    for {
      b <- socket.readByte
      _ <- Command.values(b) match
        case Put =>
          for {
            k <- socket.readNBytes
            v <- socket.readNBytes
            _ = println(s"put k = $k, v = $v")
            kvs <- kvsM.take
            _ = kvs.put(k, v)
            _ <- kvsM.put(kvs)
            _ <- socket.writeByte(Ok)
          }
          yield ()
        case Get =>
          for {
            k <- socket.readNBytes
            _ = println(s"get k = $k")
            kvs <- kvsM.take
            v = kvs.get(k)
            _ <- kvsM.put(kvs)
            _ <- if v.isEmpty then socket.writeByte(OkNone) else socket.writeByte(OkSome) >> socket.writeNBytes(v.get)
          }
          yield ()
        case Del =>
          for {
            k <- socket.readNBytes
            _ = println(s"del k = $k")
            kvs <- kvsM.take
            _ = kvs.del(k)
            _ <- kvsM.put(kvs)
            _ <- socket.writeByte(Ok)
          }
          yield ()
      _ <- NetIO.block(socket.close)
    }
    yield ()

  def listen[A](serverSocket: ServerSocket, kvs: MVar[IO, A])(given ContextShift[IO], Kvs[A, Id]): IO[Unit] = for {
    socket <- NetIO.block(serverSocket.accept)
    _ = println("listening...")
    _ <- process(socket, kvs).start(contextShift).void
    _ <- listen(serverSocket, kvs)
  }
  yield ()

  def run(args: List[String]): IO[ExitCode] = {
    import Kvs3.Kvs.given
    given ioContextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutorService(Executors.newCachedThreadPool))
    for {
      serverSocket <- NetIO.block(ServerSocket(3000))
      kvsM <- MVar.of[IO, Kvs.MapKvs](Kvs.MapKvs())
      _ <- listen(serverSocket, kvsM)
    }
    yield ExitCode.Success  }
