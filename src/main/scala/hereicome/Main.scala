package hereicome

object Main {
  def testSelect() = {
    import given hereicome.givens._
    assert(Vector(1, 2, 3).select(1) == Some(2))
    assert(Vector(1, 2, 3).select(3) == None)
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("b") == Some(2))
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("d") == None)
  }
  def main(args: Array[String]): Unit = {
    testSelect()
  }
}