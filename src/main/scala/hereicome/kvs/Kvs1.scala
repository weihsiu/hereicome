package hereicome.kvs

import scala.collection.mutable

object Kvs1

  trait Kvs[A]
    def (x: A) put (key: Vector[Byte], value: Vector[Byte]): Unit
    def (x: A) get (key: Vector[Byte]): Option[Vector[Byte]]
    def (x: A) del (key: Vector[Byte]): Unit

  object Kvs
    case class SimpleKvs(var kvs: List[(Vector[Byte], Vector[Byte])])
    object SimpleKvs
      def apply(): SimpleKvs = SimpleKvs(List.empty)
      given Kvs[SimpleKvs]
        def (x: SimpleKvs) put (key: Vector[Byte], value: Vector[Byte]) = 
          x.del(key)
          x.kvs = (key, value) :: x.kvs
        def (x: SimpleKvs) get (key: Vector[Byte]) = x.kvs.find(_._1 == key).map(_._2)
        def (x: SimpleKvs) del (key: Vector[Byte]) = x.kvs = x.kvs.filterNot(_._1 == key)

    opaque type MapKvs = mutable.Map[Vector[Byte], Vector[Byte]]
    object MapKvs
      def apply(): MapKvs = mutable.Map.empty
      def getMap(mk: MapKvs): Map[Vector[Byte], Vector[Byte]] = mk.toMap

      given Kvs[MapKvs]
        def (x: MapKvs) put (key: Vector[Byte], value: Vector[Byte]) = x.put(key, value)
        def (x: MapKvs) get (key: Vector[Byte]) = x.get(key)
        def (x: MapKvs) del (key: Vector[Byte]) = x.remove(key)

  import Kvs._

  trait KvsExt[A]
    def (x: A) getPrefixed (prefix: Vector[Byte]): List[(Vector[Byte], Vector[Byte])]

  object KvsExt
    given KvsExt[SimpleKvs]
      def (x: SimpleKvs) getPrefixed (prefix: Vector[Byte]) =
        x.kvs.filter((k, v) => k.startsWith(prefix, 0)) // parameter untupling
    given KvsExt[MapKvs]
      def (x: MapKvs) getPrefixed (prefix: Vector[Byte]) =
        MapKvs.getMap(x).view.filterKeys(_.startsWith(prefix)).toList

@main def testKvs1() =
  import Kvs1._
  def putGetDel[A](x: A)(given Kvs[A]): Unit = 
    x.put(Vector(1, 2, 3), Vector(4, 5, 6))
    val v = x.get(Vector(1, 2, 3))
    assert(v == Some(Vector(4, 5, 6)))
    x.del(Vector(1, 2, 3))

  val simpleKvs = Kvs.SimpleKvs()
  putGetDel(simpleKvs)
  val mapKvs = Kvs.MapKvs()
  putGetDel(mapKvs)

@main def testKvsExt() =
  import Kvs1._
  def putGetPrefixedDel[A](x: A)(given Kvs[A], KvsExt[A]): Unit =
    x.put(Vector(1, 2, 3), Vector(4, 5, 6))
    x.put(Vector(1, 2, 4), Vector(5, 6, 7))
    val ps = x.getPrefixed(Vector(1, 2))
    assert(ps.toSet == Set((Vector(1, 2, 3), Vector(4, 5, 6)), (Vector(1, 2, 4), Vector(5, 6, 7))))
    ps.foreach((k, v) => x.del(k))

  val simpleKvs = Kvs.SimpleKvs()
  putGetPrefixedDel(simpleKvs)
  val mapKvs = Kvs.MapKvs()
  putGetPrefixedDel(mapKvs)