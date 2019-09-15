package hereicome.actors

import scala.collection.mutable
import scala.concurrent.ExecutionContext

trait Actor[A](implicit ec: ExecutionContext):
  private val msgs = mutable.Queue[A]()
  private var scheduled = false
  def run(msg: A): Unit
  def send(msg: A): Unit = synchronized:
    msgs.enqueue(msg)
    if !scheduled then
      scheduled = true
      ec.execute(() => runWithMsgs())
  private def runWithMsgs(): Unit =
    val ms = synchronized(msgs.dequeueAll(_ => true))
    try ms.foreach(run)
    catch (e: Throwable) => e.printStackTrace
    synchronized:
      if msgs.nonEmpty
        then ec.execute(() => runWithMsgs())
        else
          assert(scheduled)
          scheduled = false

@main def testActor(): Unit =
  import scala.concurrent.ExecutionContext.Implicits.global
  val actor = new Actor[Int]:
    def run(msg: Int) = println(msg)
  actor.send(1)
  actor.send(2)