package hereicome.actors

import scala.concurrent.ExecutionContext.Implicits.global

@main def testActor(): Unit =
  import scala.concurrent.ExecutionContext.Implicits.global
  val actor = new Actor[Int | String]:
    def receive(msg: Int | String) = msg match {
      case n: Int => println(n + 1)
      case s: String => println(s.toUpperCase)
    }
  actor.send(1)
  actor.send("hello")
  // actor.send(true)
