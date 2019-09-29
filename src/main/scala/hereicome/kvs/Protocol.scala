package hereicome.kvs

object Protocol

  given [A <: Enum]: Conversion[A, Byte] = _.ordinal.toByte

  enum Command
    case Put, Get, Del

  enum Reply
    case Ok, OkSome, OkNone
