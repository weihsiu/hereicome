package hereicome.kvs

import cats._
import cats.effect._
import cats.implicits._
import scala.collection.mutable

object Kvs2 extends App {
  trait Kvs[A, F[_]] {
    def (x: A) put (key: Vector[Byte], value: Vector[Byte]): F[Unit]
    def (x: A) get (key: Vector[Byte]): F[Option[Vector[Byte]]]
    def (x: A) del (key: Vector[Byte]): F[Unit]
  }

  object Kvs {
    opaque type MapKvs = mutable.Map[Vector[Byte], Vector[Byte]]
    object MapKvs {
      def apply(): MapKvs = mutable.Map.empty[Vector[Byte], Vector[Byte]]
      given as Kvs[MapKvs, Id] {
        def (x: MapKvs) put (key: Vector[Byte], value: Vector[Byte]) = x.put(key, value)
        def (x: MapKvs) get (key: Vector[Byte]) = x.get(key)
        def (x: MapKvs) del (key: Vector[Byte]) = x.remove(key)
      }
    }
  }

  def putGetDel[A, F[_]](x: A) given Kvs[A, F], FlatMap[F]: F[Unit] = for {
    _ <- x.put(Vector(1, 2, 3), Vector(4, 5, 6))
    _ = assert(x.get(Vector(1, 2, 3)) == Some(Vector(4, 5, 6)))
    _ <-x.del(Vector(1, 2, 3))
  } yield ()

  val mapKvs = Kvs.MapKvs()
  putGetDel(mapKvs)
}