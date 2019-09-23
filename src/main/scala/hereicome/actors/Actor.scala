package hereicome.actors

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

// https://twitter.com/li_haoyi/status/1169178929963229184?s=20
trait Actor[A](implicit ec: ExecutionContext)
  private val msgs = mutable.Queue[A]()
  private var scheduled = false
  def receive(msg: A): Unit
  def send(msg: A): Unit = synchronized {
    msgs.enqueue(msg)
    if !scheduled then
      scheduled = true
      ec.execute(() => dispatch())
  }
  private def dispatch(): Unit =
    val msg = synchronized(msgs.dequeue())
    try receive(msg)
    catch (e: Throwable) => e.printStackTrace
    synchronized {
      if msgs.nonEmpty
        then ec.execute(() => dispatch())
        else
          assert(scheduled)
          scheduled = false
    }
    
@main def testActor(): Unit =
  import scala.concurrent.ExecutionContext.Implicits.global
  val actor = new Actor[Int | String] {
    def receive(msg: Int | String) = msg match
      case n: Int => println(n + 1)
      case s: String => println(s.toUpperCase)
  }
  actor.send(1)
  actor.send("hello")
  // actor.send(true)
    