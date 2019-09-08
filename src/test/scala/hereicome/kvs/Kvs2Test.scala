package hereicome.kvs

import cats._
import cats.effect._
import cats.implicits._
import java.io._
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.sys.process._
import verify._
import Kvs2._
import given Kvs2._

object Kvs2Test extends TestSuite[Unit]:

  var process = none[Process]
  override def setupSuite() =
    val pis = PipedInputStream()
    val os: OutputStream = PipedOutputStream(pis)
    process = ("sbt \"run-main hereicome.kvs.KvsServer\"" #> os).run.some
    val br = BufferedReader(InputStreamReader(pis, "utf-8"))
    while {
      val l = br.readLine
      println(l)
      l != "listening..."
    } do ()
    println("started")
  override def tearDownSuite() =
    process.foreach(_.destroy)
  override def setup() = ()
  override def tearDown(env: Unit) = {}
  
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

    (_) => executorService.shutdown
  }
