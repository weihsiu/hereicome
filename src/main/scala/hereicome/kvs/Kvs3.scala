package hereicome.kvs

import cats._
import cats.effect._
import cats.implicits._
import java.net.Socket
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.collection.mutable

object Kvs3

  trait Kvs[A, F[_]]
    def (x: A) put (key: Vector[Byte], value: Vector[Byte]): F[Unit]
    def (x: A) get (key: Vector[Byte]): F[Option[Vector[Byte]]]
    def (x: A) del (key: Vector[Byte]): F[Unit]

  object Kvs
    opaque type MapKvs = mutable.Map[Vector[Byte], Vector[Byte]]
    object MapKvs
      def apply(): MapKvs = mutable.Map.empty[Vector[Byte], Vector[Byte]]
      given Kvs[MapKvs, Id] // type Id[A] = A
        def (x: MapKvs) put (key: Vector[Byte], value: Vector[Byte]) = x.put(key, value)
        def (x: MapKvs) get (key: Vector[Byte]) = x.get(key)
        def (x: MapKvs) del (key: Vector[Byte]) = x.remove(key)

    case class KvsClient(host: String, port: Int)
    object KvsClient
      given (given ContextShift[IO]): Kvs[KvsClient, IO]
        import NetIO.given
        import Protocol._
        import Command._, Reply._
        def (x: KvsClient) put (key: Vector[Byte], value: Vector[Byte]) = for {
          s <- NetIO.block(Socket(x.host, x.port))
          _ <- s.writeByte(Put)
          _ <- s.writeNBytes(key)
          _ <- s.writeNBytes(value)
          r <- s.readByte
          _ <- NetIO.block(s.close)
          _ = assert(Reply.values(r) == Ok)
        }
        yield ()
        def (x: KvsClient) get (key: Vector[Byte]) = for {
          s <- NetIO.block(Socket(x.host, x.port))
          _ <- s.writeByte(Get)
          _ <- s.writeNBytes(key)
          r <- s.readByte
          v <- if Reply.values(r) == OkNone then IO.pure(None) else s.readNBytes.map(Some(_))
          _ <- NetIO.block(s.close)
        }
        yield v
        def (x: KvsClient) del (key: Vector[Byte]) = for {
          s <- NetIO.block(Socket(x.host, x.port))
          _ <- s.writeByte(Del)
          _ <- s.writeNBytes(key)
          r <- s.readByte
          _ <- NetIO.block(s.close)
          _ = assert(Reply.values(r) == Ok)
        }
        yield ()

@main def testKvs3() = {
  import Kvs3._
  def putGetDel[A, F[_]](x: A)(given Kvs[A, F], FlatMap[F]): F[Unit] = for {
    _ <- x.put(Vector(1, 2, 3), Vector(4, 5, 6))
    v <- x.get(Vector(1, 2, 3))
    _ = assert(v == Some(Vector(4, 5, 6)))
    _ <-x.del(Vector(1, 2, 3))
  }
  yield ()

  val executorService = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool)
  given ioContextShift: ContextShift[IO] = IO.contextShift(executorService) // Alias Given

  val mapKvs = Kvs.MapKvs()
  putGetDel(mapKvs)

  val kvsClient = Kvs.KvsClient("localhost", 3000)
  putGetDel(kvsClient).unsafeRunSync

  executorService.shutdown}
