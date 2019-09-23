package hereicome.kvs

import Kvs1._

object Kvs3

  trait TypedKvs[A]
    def (x: A) putT[K, V] (key: K, value: V)(given Serde[K], Serde[V]): Unit
    def (x: A) getT[K, V] (key: K)(given Serde[K], Serde[V]): Option[V]
    def (x: A) delT[K] (key: K)(given Serde[K]): Unit

  object TypedKvs
    given [A](given Kvs[A]): TypedKvs[A]
      def (x: A) putT[K, V] (key: K, value: V)(given Serde[K], Serde[V]) =
        x.put(key.serialize, value.serialize)
      def (x: A) getT[K, V] (key: K)(given Serde[K], Serde[V]) =
        x.get(key.serialize).map(summon[Serde[V]].deserialize)
      def (x: A) delT[K] (key: K)(given Serde[K]) =
        x.del(key.serialize)

@main def testKvs3() =
  import Kvs3._
  import TypedKvs.given
  import Kvs.SimpleKvs.given
  val simpleKvs = Kvs.SimpleKvs()
  simpleKvs.putT("hello", "world")
  assert(simpleKvs.getT[V = String]("hello") == Some("world")) // Named Type Argument
  simpleKvs.delT("hello")

  simpleKvs.putT(1, "one")
  simpleKvs.putT("two", 2)
  assert(simpleKvs.getT[V = String](1) == Some("one"))
  assert(simpleKvs.getT[V = Int]("two") == Some(2))
  assert(simpleKvs.getT[V = Int](3) == None)