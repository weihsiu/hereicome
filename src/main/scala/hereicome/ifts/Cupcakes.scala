package hereicome.ifts

type |=>[A, B] = (given A) => B // Implicit Function Type

trait FileService
  def write(data: String): Unit =
    println(s"writing $data")
object FileService
  def write(data: String): FileService |=> Unit =
    summon[FileService].write(data) // summon function

trait DatabaseService
  def insert(data: String): Int =
    println(s"inserting $data")
    123

trait NetworkService
  def transmit(data: String): Boolean = 
    println(s"transmitting $data")
    true

trait LogService(val prefix: String)
  def log(data: String): FileService |=> Unit =
    summon[FileService].write(s"log $prefix:$data")

type AllServices = FileService & DatabaseService & NetworkService & LogService // Intersection Type

def persist(name: String): FileService & DatabaseService |=> Int = (given ctx) =>
  import FileService._
  write(name)
  ctx.insert(name)

def send(name: String): DatabaseService & NetworkService |=> Boolean = (given ctx) =>
  ctx.insert(name)
  ctx.transmit(name)

def process(name: String): AllServices |=> Boolean = (given ctx) =>
  // given mockCtx: FileService, DatabaseService, NetworkService, LogService("mock")
  //   export ctx._ // Export Clause
  //   override def write(data: String) = println(s"mock writing $data")
  ctx.log(name)
  persist(name)
  send(name)

@main def testCupcakes() =
  class Universe extends FileService with DatabaseService with NetworkService with LogService("omg")
  given Universe

  persist("adrian")
  send("brian")
  process("walter")