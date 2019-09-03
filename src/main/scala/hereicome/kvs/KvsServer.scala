package hereicome.kvs

import cats.effect._
import cats.implicits._
import java.net.{ServerSocket, Socket}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object KvsServer extends App {
  def process(socket: Socket) given ContextShift[IO]: IO[Unit] = {
    import given NetIO._
    for {
      b <- socket.readByte
      _ <- socket.writeByte(b)
    } yield ()
  }
  val serverSocket = ServerSocket(3000)
  given as ContextShift[IO] = IO.contextShift(ExecutionContext.fromExecutorService(Executors.newCachedThreadPool))
  while (true) {
    val socket = serverSocket.accept
    process(socket).unsafeRunSync
    socket.close
  }
}