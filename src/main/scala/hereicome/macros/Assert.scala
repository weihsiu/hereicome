package hereicome.macros

import scala.quoted._
import given scala.quoted.autolift._

object Assert:
  inline def assert(expr: => Boolean): Unit =
    ${ assertImpl('expr) }

  def assertImpl(expr: Expr[Boolean]) given QuoteContext =
    '{ if !($expr) then throw new AssertionError(s"failed assertion: ${${ showExpr(expr) }}") }

  def showExpr[T](expr: Expr[T]) given QuoteContext: Expr[String] = 
    val code: String = expr.show
    code.toExpr