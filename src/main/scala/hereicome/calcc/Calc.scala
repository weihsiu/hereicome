package hereicome.calcs

import scala.annotation._

object Calcs

  trait Unit[A] 
    def apply(n: Double): A
    def (x: A) unitValue: Double
    def (x: A) toMeters: Double
    def (x: A) to[B](given TM: Unit[B]): B = TM(x.toMeters / TM(1).toMeters)

  opaque type Millimeters = Double // Opaque Type Alias
  given Unit[Millimeters]
    def apply(n: Double): Millimeters = n
    def (x: Millimeters) unitValue = x
    def (x: Millimeters) toMeters = x / 1000

  opaque type Centimeters = Double
  given Unit[Centimeters]
    def apply(n: Double): Centimeters = n
    def (x: Centimeters) unitValue = x
    def (x: Centimeters) toMeters = x / 100

  opaque type Meters = Double
  given Unit[Meters]
    def apply(n: Double): Meters = n
    def (x: Meters) unitValue = x
    def (x: Meters) toMeters = x

  opaque type Inches = Double
  given Unit[Inches]
    def apply(n: Double): Inches = n
    def (x: Inches) unitValue = x
    def (x: Inches) toMeters = x * 0.0254
    
  opaque type Feet = Double
  given Unit[Feet]
    def apply(n: Double): Feet = n
    def (x: Feet) unitValue = x
    def (x: Feet) toMeters = x * 0.3048

  // given {
  //   def (n: Long) millimeters: Millimeters = n
  //   def (n: Long) meters: Meters = n
  // }
  given (n: Long) { // Collective Parameter
    def millimeters: Millimeters = n
    def centimeters: Centimeters = n
    def meters: Meters = n
    def inches: Inches = n
    def feet: Feet = n
  }

  given [A : Unit](x: A) {
    inline def +[B : Unit](y: B): Meters = x.toMeters + y.toMeters // Inline
    inline def -[B : Unit](y: B): Meters = x.toMeters - y.toMeters
    inline def *(y: Double): Meters = x.toMeters * y
    inline def /(y: Double): Meters = x.toMeters / y
  }

@main def testCalcs() =
  import Calcs._, Calcs.given
  println((2.meters * 10 + 3.millimeters).unitValue)
  println(10.meters.to[Feet].unitValue)
  println(10.feet.to[Inches].unitValue)
  println((5.feet + 9.inches).to[Centimeters].unitValue)
  