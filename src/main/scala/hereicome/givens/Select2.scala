package hereicome.givens

opaque type RandomInt = (Int, Int)
object RandomInt {
  def apply(start: Int, end: Int): RandomInt = (start, end)
  def range(randomInt: RandomInt): (Int, Int) = randomInt
}

trait Selector[A, B] {
  def value(x: A): B
}

object Selector {
  given [A] as Selector[A, A] {
    def value(x: A) = x
  }

  given as Selector[RandomInt, Int] {
    def value(x: RandomInt) = {
      val (start, end) = RandomInt.range(x)
      scala.util.Random.nextInt(end - start) + start
    }
  }
}

trait Select2[A, B, C] {
  type Out
  def (x: A) select (selector: B) given Selector[B, C]: Option[Out]
}

object Select2 {
  given [A, B] as Select2[IndexedSeq[A], B, Int] {
    type Out = A
    def (x: IndexedSeq[A]) select (selector: B) given (S: Selector[B, Int]): Option[A] = {
      val index = S.value(selector)
      if (index >= 0 && index < x.length) Some(x(index)) else None
    }
  }

  given [A, B] as Select2[Map[A, B], A, A] {
    type Out = B
    def (x: Map[A, B]) select (selector: A) given (S: Selector[A, A]): Option[B] =
      x.get(S.value(selector))
  }
}