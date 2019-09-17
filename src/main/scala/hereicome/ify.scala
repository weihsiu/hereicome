package hereicome

@main def testIFY() =
  case class Ctx()
  type Context[A] = given Ctx => A
  def testContext[A](x: A): Context[A] = given ctx =>
    x
  given as Ctx
  println(testContext(123))