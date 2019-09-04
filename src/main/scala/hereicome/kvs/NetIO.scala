package hereicome.kvs

import cats.effect._
import cats.implicits._
import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket

trait NetIO[F[_]] {
  def (socket: Socket) readByte: F[Byte]
  def (socket: Socket) readNBytes: F[Vector[Byte]]
  def (socket: Socket) writeByte(byte: Byte): F[Unit]
  def (socket: Socket) writeNBytes(bytes: Vector[Byte]): F[Unit]
}

object NetIO {
  def block[A](thunk: => A) given ContextShift[IO]: IO[A] = Blocker[IO].use(_.delay(thunk))
  given as NetIO[IO] given ContextShift[IO] {
    def (socket: Socket) readByte = block(socket.getInputStream.read.toByte)
    def (socket: Socket) readNBytes = block {
      val dis = DataInputStream(socket.getInputStream)
      val bs = Array.ofDim[Byte](dis.readInt)
      dis.readFully(bs)
      bs.toVector
    }
    def (socket: Socket) writeByte(byte: Byte): IO[Unit] = block(socket.getOutputStream.write(byte))
    def (socket: Socket) writeNBytes(bytes: Vector[Byte]): IO[Unit] = block {
      val dos = DataOutputStream(socket.getOutputStream)
      dos.writeInt(bytes.length)
      val bs = bytes.toArray
      dos.write(bs, 0, bs.length)
    }
  }
}