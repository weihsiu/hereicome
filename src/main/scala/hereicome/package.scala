package hereicome

enum Nat
  case Z
  case S[A <: Nat](n: A)

object Nat
  inline def toNat(n: Int): Nat = if n == 0 then Nat.Z else Nat.S(toNat(n - 1))
  inline def toInt(n: Nat): Int = inline n match
    case Nat.Z => 0
    case Nat.S(n) => 1 + toInt(n)

type _0 = Nat.Z.type
type _1 = Nat.S[_0]
type _2 = Nat.S[_1]
type _3 = Nat.S[_2]