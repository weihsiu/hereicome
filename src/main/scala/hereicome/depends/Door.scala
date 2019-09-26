package hereicome.depends

import scala.annotation._

enum DoorState // Enum
  case IsOpen()
  case IsClosed()
  
import DoorState._

sealed trait DoorCommand[S <: DoorState]
  type NextState <: DoorState
  def nextState: NextState

case object Open extends DoorCommand[IsClosed]
  type NextState = IsOpen
  val nextState = new IsOpen
  
case object Close extends DoorCommand[IsOpen]
  type NextState = IsClosed
  val nextState = new IsClosed
  
case object RingBell extends DoorCommand[IsClosed]
  type NextState = IsClosed
  val nextState = new IsClosed

type DoCommand[S <: DoorState] = (c: DoorCommand[S]) => c.NextState // Dependent Function Types

// Toplevel Definitions

def (state: S) after[S <: DoorState]: DoCommand[S] = command =>
  command.nextState

val s1 = new IsOpen after Close
val s2 = s1 after RingBell
val s3 = s2 after Open