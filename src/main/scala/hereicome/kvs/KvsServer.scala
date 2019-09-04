package hereicome.kvs

import cats._
import cats.effect._
import cats.effect.concurrent._
import cats.implicits._
import java.net.{ServerSocket, Socket}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import Kvs2._

object KvsServer extends IOApp {
  def process[A](socket: Socket, kvs: MVar[IO, A]) given ContextShift[IO], Kvs[A, Id]: IO[Unit] = {
    import given NetIO._
    for {
      b <- socket.readByte
      _ = Thread.sleep(5000)
      _ = println(s"time = ${System.currentTimeMillis}, thread = ${Thread.currentThread.getName}")
      _ <- socket.writeByte(b)
      _ <- NetIO.block(socket.close)
    } yield ()
  }

  def listen[A](serverSocket: ServerSocket, kvs: MVar[IO, A]) given ContextShift[IO], Kvs[A, Id]: IO[Unit] = for {
    socket <- NetIO.block(serverSocket.accept)
    fiber <- process(socket, kvs).start
    _ <- listen(serverSocket, kvs)
  } yield ()

  def run(args: List[String]): IO[ExitCode] = {
    import given Kvs2.Kvs._
    given ioContextShift as ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutorService(Executors.newCachedThreadPool))
    val serverSocket = ServerSocket(3000)
    val kvs = Kvs.MapKvs()
    (MVar.of[IO, Kvs.MapKvs](kvs) >>= (listen(serverSocket, _))).as(ExitCode.Success)
  }
  
  // while (true) {
  //   val socket = serverSocket.accept
  //   process(socket).unsafeRunSync
  //   socket.close
  // }
}