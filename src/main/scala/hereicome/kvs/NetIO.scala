package hereicome.kvs

import cats.effect.IO
import cats.implicits._
import java.net.Socket

trait NetIO[F[_]] {
  def (socket: Socket) readByte: F[Byte]
  def (socket: Socket) readBytes: F[Vector[Byte]]
  def (socket: Socket) writeByte(byte: Byte): F[Unit]
  def (socket: Socket) writeBytes(bytes: Vector[Byte]): F[Unit]
}

object NetIO {
  given as NetIO[IO] {
    def (socket: Socket) readByte = socket.getInputStream.read
    def (socket: Socket) readBytes: IO[Vector[Byte]]
    def (socket: Socket) writeByte(byte: Byte): IO[Unit]
    def (socket: Socket) writeBytes(bytes: Vector[Byte]): IO[Unit]
  }
}