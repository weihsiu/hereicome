package hereicome.depends

enum DoorState:
  case IsOpen()
  case IsClosed()
  
import DoorState._

sealed trait DoorCommand[S <: DoorState]:
  type NextState <: DoorState
  def nextState: NextState

case object Open extends DoorCommand[IsClosed]:
  type NextState = IsOpen
  val nextState = new IsOpen
  
case object Close extends DoorCommand[IsOpen]:
  type NextState = IsClosed
  val nextState = new IsClosed
  
case object RingBell extends DoorCommand[IsClosed]:
  type NextState = IsClosed
  val nextState = new IsClosed

def doCommand[S <: DoorState](state: S, command: DoorCommand[S]): command.NextState =
  command.nextState

val s1 = doCommand(new IsOpen, Close)
val s2 = doCommand(s1, RingBell)
val s3 = doCommand(s2, Open)

def execute[S <: DoorState](command: DoorCommand[S])(state: S): command.NextState =
  doCommand(state, command)

// import given hereicome._
// val _ = new IsOpen |> execute(Close) |> execute(RingBell) |> execute(Open)