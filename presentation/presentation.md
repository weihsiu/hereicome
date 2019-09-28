---
marp: false
---
# Scala 3, Here I Come!
## 張瑋修 Walter Chang
### @weihsiu / weihsiu@gmail.com
![https://github.com/weihsiu/hereicome](qrcode.png)

---
# Scala Taiwan
## [Scala Taiwan gitter channel](https://gitter.im/ScalaTaiwan/ScalaTaiwan)
## [Scala Taiwan FB group](https://www.facebook.com/groups/ScalaTW/)
## [Scala Taiwan meetup](https://www.meetup.com/Scala-Taiwan-Meetup/)
![Scala Taiwan Logo](scalataiwan.png)

---
# Agenda
- What is Scala 3?
- New syntax
- Select.scala
- Actor.scala
- Conversions.scala
- Door.scala
- It.scala
- Cupcakes.scala
- Kvs.scala
- Q&A
---
# What is Scala 3?
- Next generation of Scala
- Coming out Fall 2020
- Many new features and cleanups
- Source code is mostly backward compatible with Scala 2
- Libraries from both Scala 2/3 can be used interchangeably
- VSCode with Dotty Language Server plugin
---
# New syntax
- Optional
- **New Control Syntax**
```scala
if x < 0 then y else z
while x > 0 do ???
for x <- xs do println(x)
```
- **Optional Braces**
  - No more curly braces
  - Just indent the block that normally goes inside curly braces
- Compiler switches allow to go back and forth
---
# Select.scala
- The ability to select something
- **Anonymous Given Instances**
```scala
given Select[List[Int], Int] { ??? } // implicit val in Scala 2
given [A]: Select[List[A], A] { ??? } // implicit def in Scala 2
```
- **Extension Methods**
```scala
def (x: A) select (selector: B): Option[Out]
```
- **Main Functions**
```scala
@main def testSelect() = ???
```
- **Given Imports**
```scala
import hereicome.Select.given
```
---
# Select2.scala
- On top of the ability to select something, abstract how the selection is done.
- **Opaque Type Aliases**
```scala
opaque type RandomInt = (Int, Int)
object RandomInt
  def apply(start: Int, end: Int): RandomInt = (start, end)
```
- **Named Given Instances**
```scala
given id[A]: Selector[A, A]
```
- **Given Parameters**
```scala
def (x: A) select (selector: B)(given Selector[B, C]): Option[Out]
```
---
# Actor.scala
- A minimum actor implementation taken shamelessly from @li_haoyi
- **Union Types**
```scala
new Actor[Int | String] { ??? }
```
---
# Conversions.scala
- The new and safer way to define implicit conversion
- **Implicit Conversions**
```scala
import scala.language.implicitConversions
given [A, B]: Conversion[Either[A, B], A | B] = ???
// abstract class Conversion[-T, +U] extends Function1[T, U]
```
---
# Door.scala
- Translated from an Idris example
- **Enums**
```scala
enum DoorState
  case IsOpen()
  case IsClosed()
```
- **Dependent Function Types**
```scala
(c: DoorCommand[S]) => c.NextState
```
- **Toplevel Definitions**
  - ```type```, ```val```, ```var```, and ```def``` can be defined without the enclosing ```trait```, ```class```, or ```object```.
---
# It.scala
- Mimics Kotlin's "it"
- **Implicit Function Types** (as parameter)
```scala
def apply[A, B](f: (given GivenParameter[A]) => B): A => B
```
---
# Cupcakes.scala
- A minimum DI framework
- New take on the infamous Cake Pattern in Scala 2 ;)
- **Implicit Function Types** (as return value)
```scala
def write(data: String): (given FileService) => Unit
```
- **summon** aka. "the" or a better "implicitly"
```scala
summon[FileService].write(data)
```
- **Trait Parameters**
```scala
trait LogService(val prefix: String)
```
- **Intersection Types**
```scala
type AllServices = FileService & DatabaseService & NetworkService & LogService
```
- **Export Clauses**
```scala
export ctx._
```
---
# Kvs1.scala
- A simple key-value store
- Typeclass Oriented Programming (TOP)
  - A better way to organize program
  - A solution to the [Expression problem](https://en.wikipedia.org/wiki/Expression_problem)
- **Parameter Untupling**
```scala
ps.foreach((k, v) => x.del(k))
```
---
# Kvs2.scala
- Typed key-value store
- **Named Type Arguments**
```scala
simpleKvs.getT[V = String]("hello")
```
---
# Serde.scala
- Serialization / deserialization
---
# Kvs3.scala
- Abstract effects
  - Identity effect
  - Network IO effect
- Kvs network client
- **Alias Givens**
```scala
given ioContextShift: ContextShift[IO] = IO.contextShift(executorService)
```
---
# NetIO.scala
- Socket IO effect
  - Synchronous IO (Blocker thread pool)
- **Creator Applications**
```scala
DataInputStream(socket.getInputStream)
```
---
# KvsServer.scala
- Kvs network server
- Concurrency safe (Fiber, MVar)
---
# Q&A
## That's all and thank you for your attention
![https://github.com/weihsiu/hereicome](qrcode.png)

