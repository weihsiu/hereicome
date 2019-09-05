package hereicome.depends

import hereicome._

trait Switcheroo[A]:
  type Type
  def apply(x: A): Type

given as Switcheroo[Boolean]:
  type Type = Int
  def apply(x: Boolean) = if x then 1 else 0

given as Switcheroo[Int]:
  type Type = Boolean
  def apply(x: Int) = x != 1

def switcheroo[A](x: A) given (S: Switcheroo[A]): S.Type = S(x)

val r1: Boolean = switcheroo(0)
val r2: Int = switcheroo(r1)
