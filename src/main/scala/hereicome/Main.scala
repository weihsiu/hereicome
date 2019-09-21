package hereicome

object Main
  def testSelect() =
    import hereicome.givens.Select.given
    assert(Vector(1, 2, 3).select(1) == Some(2))
    assert(Vector(1, 2, 3).select(3) == None)
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("b") == Some(2))
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("d") == None)

  def testSelect2() =
    import hereicome.givens.Select2.given
    import hereicome.givens.RandomInt
    assert(Vector(1, 2, 3).select(1) == Some(2))
    assert(Vector(1, 2, 3).select(3) == None)
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("b") == Some(2))
    assert(Map("a" -> 1, "b" -> 2, "c" -> 3).select("d") == None)
    println(Vector(1, 2, 3).select(RandomInt(0, 3)))

  def main(args: Array[String]): Unit =
    testSelect()
    testSelect2()