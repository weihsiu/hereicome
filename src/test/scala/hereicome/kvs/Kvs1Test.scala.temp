package hereicome.kvs

import verify._
import Kvs1._
import given Kvs1._

object Kvs1Test extends BasicTestSuite:

  test("put, get, and del") {
    def putGetDel[A](x: A) given Kvs[A]: Unit = 
      x.put(Vector(1, 2, 3), Vector(4, 5, 6))
      val v = x.get(Vector(1, 2, 3))
      assert(v == Some(Vector(4, 5, 6)))
      x.del(Vector(1, 2, 3))

    val simpleKvs = Kvs.SimpleKvs()
    putGetDel(simpleKvs)
    val mapKvs = Kvs.MapKvs()
    putGetDel(mapKvs)
  }

  test("put, getPrefixed, and del") {
    def putGetPrefixedDel[A](x: A) given Kvs[A], KvsExt[A]: Unit =
      x.put(Vector(1, 2, 3), Vector(4, 5, 6))
      x.put(Vector(1, 2, 4), Vector(5, 6, 7))
      val ps = x.getPrefixed(Vector(1, 2))
      assert(ps.toSet == Set((Vector(1, 2, 3), Vector(4, 5, 6)), (Vector(1, 2, 4), Vector(5, 6, 7))))
      ps.foreach((k, v) => x.del(k))

    val simpleKvs = Kvs.SimpleKvs()
    putGetPrefixedDel(simpleKvs)
    val mapKvs = Kvs.MapKvs()
    putGetPrefixedDel(mapKvs)
  }
  