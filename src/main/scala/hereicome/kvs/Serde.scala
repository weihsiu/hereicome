package hereicome.kvs

import java.nio._

trait Serde[A]
  def (x: A) serialize: Vector[Byte]
  def deserialize(bs: Vector[Byte]): A

object Serde
  given Serde[Int]
    def (x: Int) serialize = ByteBuffer.allocate(4).putInt(x).array.toVector
    def deserialize(bs: Vector[Byte]) = ByteBuffer.wrap(bs.toArray).getInt
    
  given stringSerde: Serde[String] 
    def (x: String) serialize = x.getBytes.toVector
    def deserialize(bs: Vector[Byte]) = String(bs.toArray)

  given [A, B](given SA: Serde[A], SB: Serde[B]): Serde[(A, B)] // given with other given constraints
    def (x: (A, B)) serialize =
      val bs = SA.serialize(x._1)
      bs.length.serialize ++ bs ++ SB.serialize(x._2)
    def deserialize(bs: Vector[Byte]) = {
      val len = summon[Serde[Int]].deserialize(bs.take(4))
      (SA.deserialize(bs.drop(4).take(len)), SB.deserialize(bs.drop(4 + len)))    }
