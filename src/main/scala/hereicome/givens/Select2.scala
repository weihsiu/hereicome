package hereicome.givens

opaque type RandomInt = Int
object RandomInt {
  def apply()
}

trait Selector[A] {
  def value(x: A): A
}

object Selector {
  given [A] as Selector[A] {
    def value(x: A) = x
  }
}

trait Select2[A, B] {
  type Out
  def (x: A) select (selector: B) given Selector[B]: Option[Out]
}

object Select2 {
  given [A] as Select2[IndexedSeq[A], Int] {
    type Out = A
    def (x: IndexedSeq[A]) select (selector: Int) given (S: Selector[Int]): Option[A] =
      if (x.length > selector) Some(x(S.value(selector))) else None
  }

  given [A, B] as Select2[Map[A, B], A] {
    type Out = B
    def (x: Map[A, B]) select (selector: A) given (S: Selector[A]): Option[B] =
      x.get(S.value(selector))
  }
}