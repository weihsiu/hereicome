package hereicome.kvs

import java.nio._

trait Serde[A]
  def (x: A) serialize: Vector[Byte]
  def deserialize(bs: Vector[Byte]): A

object Serde
  given Serde[Int]
    def (x: Int) serialize = ByteBuffer.allocate(4).putInt(x).array.toVector
    def deserialize(bs: Vector[Byte]) = ByteBuffer.wrap(bs.toArray).getInt
    
  given Serde[String]
    def (x: String) serialize = x.getBytes.toVector
    def deserialize(bs: Vector[Byte]) = String(bs.toArray)