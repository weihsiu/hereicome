package hereicome.kvs

import scala.collection.mutable

object Kvs1:
  trait Kvs[A]:
    def (x: A) put (key: Vector[Byte], value: Vector[Byte]): Unit
    def (x: A) get (key: Vector[Byte]): Option[Vector[Byte]]
    def (x: A) del (key: Vector[Byte]): Unit

  object Kvs:
    case class SimpleKvs(var kvs: List[(Vector[Byte], Vector[Byte])])
    object SimpleKvs:
      def apply(): SimpleKvs = SimpleKvs(List.empty)
      given as Kvs[SimpleKvs]:
        def (x: SimpleKvs) put (key: Vector[Byte], value: Vector[Byte]) = 
          x.del(key)
          x.kvs = (key, value) :: x.kvs
        def (x: SimpleKvs) get (key: Vector[Byte]) = x.kvs.find(_._1 == key).map(_._2)
        def (x: SimpleKvs) del (key: Vector[Byte]) = x.kvs = x.kvs.filterNot(_._1 == key)

    opaque type MapKvs = mutable.Map[Vector[Byte], Vector[Byte]]
    object MapKvs:
      def apply(): MapKvs = mutable.Map.empty
      def getMap(mk: MapKvs): Map[Vector[Byte], Vector[Byte]] = mk.toMap

      given as Kvs[MapKvs]:
        def (x: MapKvs) put (key: Vector[Byte], value: Vector[Byte]) = x.put(key, value)
        def (x: MapKvs) get (key: Vector[Byte]) = x.get(key)
        def (x: MapKvs) del (key: Vector[Byte]) = x.remove(key)

  import Kvs._

  trait KvsExt[A]:
    def (x: A) getPrefixed (prefix: Vector[Byte]): List[(Vector[Byte], Vector[Byte])]

  object KvsExt:
    given as KvsExt[SimpleKvs]:
      def (x: SimpleKvs) getPrefixed (prefix: Vector[Byte]) =
        x.kvs.filter((k, v) => k.startsWith(prefix, 0)) // parameter untupling
    given as KvsExt[MapKvs]:
      def (x: MapKvs) getPrefixed (prefix: Vector[Byte]) =
        MapKvs.getMap(x).view.filterKeys(_.startsWith(prefix)).toList
