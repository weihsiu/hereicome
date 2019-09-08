package hereicome.kvs

import cats._
import cats.effect._
import cats.implicits._
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import verify._
import Kvs2._
import given Kvs2._

object Kvs2Test extends BasicTestSuite:
  test("put, get, and del") {
    def putGetDel[A, F[_]](x: A) given Kvs[A, F], FlatMap[F]: F[Unit] = for
      _ <- x.put(Vector(1, 2, 3), Vector(4, 5, 6))
      v <- x.get(Vector(1, 2, 3))
      _ = assert(v == Some(Vector(4, 5, 6)))
      _ <-x.del(Vector(1, 2, 3))
    yield ()

    val executorService = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool)
    given ioContextShift as ContextShift[IO] = IO.contextShift(executorService)

    val mapKvs = Kvs.MapKvs()
    putGetDel(mapKvs)

    val kvsClient = Kvs.KvsClient("localhost", 3000)
    putGetDel(kvsClient).unsafeRunSync

    executorService.shutdown
  }