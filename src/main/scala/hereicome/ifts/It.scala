package hereicome.ifts

object It
  opaque type GivenParameter[A] = A
  def it[A](given p: GivenParameter[A]): A = p
  def apply[A, B](f: (given GivenParameter[A]) => B): A => B = f(given _) // Implicit Function Type

@main def testIt() =
  import It._
  println(List(1, 2, 3).map(x => "*" * x + x))
  println(List(1, 2, 3).map(It((given p) => "*" * it(given p) + it(given p))))
  println(List(1, 2, 3).map(It((given p) => "*" * it + it)))
  println(List(1, 2, 3).map(It("*" * it + it)))
  List("a", "b", "c").zip(1 to 10).foreach(It(println(s"${it._2} = ${it._1}")))