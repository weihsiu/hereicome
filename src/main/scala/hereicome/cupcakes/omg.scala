package hereicome.cupcakes

type >:>[A, B] = given A => B

trait FileService:
  def write(data: String): Unit
object FileService:
  def write(data: String): FileService >:> Unit = the[FileService].write(data)

trait DatabaseService:
  def insert(data: String): Int

trait NetworkService:
  def transmit(data: String): Boolean

trait LogService:
  def log(data: String): FileService >:> Unit

type AllServices = FileService & DatabaseService & NetworkService & LogService

def persist(name: String): (FileService & DatabaseService) >:> Int = given ctx =>
  import FileService._
  write(name)
  ctx.insert(name)

def send(name: String): (DatabaseService & NetworkService) >:> Boolean = given ctx =>
  ctx.insert(name)
  ctx.transmit(name)

def process(name: String): AllServices >:> Boolean = given ctx =>
  ctx.log(name)
  persist(name)
  send(name)

@main def testOMG() =
  given universe as FileService, DatabaseService, NetworkService, LogService:
    def write(data: String) = println(s"writing $data")
    def insert(data: String) =
      println(s"inserting $data")
      123
    def transmit(data: String) =
      println(s"transmitting $data")
      true
    def log(data: String) = // given fs =>
      the[FileService].write(s"log $data")

  persist("adrian")
  send("brian")
  process("walter")