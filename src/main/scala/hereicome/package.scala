package hereicome

given {
  def (x: A) |>[A, B] (f: A => B): B = f(x)
}