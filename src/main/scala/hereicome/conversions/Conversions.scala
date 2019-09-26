package hereicome.conversions

import scala.language.implicitConversions

given [A, B]: Conversion[Either[A, B], A | B] = // Implicit Conversion
  case Left(x) => x
  case Right(x) => x

given [A]: Conversion[Unit | A, Option[A]] = 
  case () => None
  case x: A  => Some(x)

@main def testConversions() =
  val e: Either[Int, Unit] = Left(456)
  val x: Int | Unit = e
  val y: Option[Int] = x
  println(y)