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

@main def testSelect() =
  import Select.given
  assert(Vector(1, 2, 3).select(1) == Some(2))
  assert(Vector(1, 2, 3).select(3) == None)
  assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("b") == Some(2))
  assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("d") == None)
