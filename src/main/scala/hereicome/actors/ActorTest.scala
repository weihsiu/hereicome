package hereicome.actors

import scala.concurrent.ExecutionContext.Implicits.global

// object ActorTest extends App:
//   import hereicome.actors.Actor
// @main def actorTest(): Unit =
//   val actor = new Actor[Int]:
//     def run(msg: Int) = println(msg)
//   actor.send(1)
//   actor.send(2)

@main def funcTest(): Unit =
  import scala.collection.mutable.Queue
  val q = Queue[Int]()
  q.dequeueAll(_ => true)
