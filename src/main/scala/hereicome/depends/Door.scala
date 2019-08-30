package hereicome.depends

sealed trait DoorState
case object IsOpen extends DoorState
case object IsClosed extends DoorState

sealed trait DoorCommand[S <: DoorState] {
  type NextState <: DoorState
  def nextState: NextState
}
case object Open extends DoorCommand[IsClosed.type] {
  type NextState = IsOpen.type
  val nextState = IsOpen
}
case object Close extends DoorCommand[IsOpen.type] {
  type NextState = IsClosed.type
  val nextState = IsClosed
}
case object RingBell extends DoorCommand[IsClosed.type] {
  type NextState = IsClosed.type
  val nextState = IsClosed
}

def doCommand[S <: DoorState](state: S, command: DoorCommand[S]): command.NextState = command.nextState

val s1 = doCommand(IsOpen, Close)
val s2 = doCommand(s1, RingBell)
val s3 = doCommand(s2, Open)
