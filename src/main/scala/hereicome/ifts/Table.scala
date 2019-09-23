package hereicome.ifts

import scala.collection.mutable.ArrayBuffer

class Table
  val rows = ArrayBuffer.empty[Row]
  def add(r: Row): Unit = rows += r
  override def toString = rows.mkString("Table(", ",", ")")

class Row
  val cells = ArrayBuffer.empty[Cell]
  def add(c: Cell): Unit = cells += c
  override def toString = cells.mkString("Row(", ", ", ")")

case class Cell(elem: String)

def table(init: (given Table) => Unit): Table =
  given t: Table
  init
  t

def row(init: (given Row) => Unit)(given t: Table) =
  given r: Row
  init
  t.add(r)

def cell(str: String)(given r: Row) =
  r.add(Cell(str))

@main def testTable() =
  println(
    table {
      row {
        cell("top left")
        cell("top right")
      }
      row {
        cell("bottom left")
        cell("bottom right")
      }
    }
  )