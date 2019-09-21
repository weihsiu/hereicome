package hereicome.givens

trait Select[A, B]
  type Out
  def (x: A) select (selector: B): Option[Out]

object Select
  given [A]: Select[IndexedSeq[A], Int]
    type Out = A
    def (x: IndexedSeq[A]) select (selector: Int): Option[A] =
      if x.sizeIs > selector then Some(x(selector)) else None

  given [A, B]: Select[Map[A, B], A]
    type Out = B
    def (x: Map[A, B]) select (selector: A): Option[B] =
      x.get(selector)