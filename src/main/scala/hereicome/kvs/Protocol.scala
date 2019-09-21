package hereicome.kvs

object Protocol
  val PUT: Byte = 0
  val GET: Byte = 1
  val DEL: Byte = 2

  val OK: Byte = 0
  val OK_SOME: Byte = 1
  val Ok_NONE: Byte = 2
  val FAIL: Byte = 3