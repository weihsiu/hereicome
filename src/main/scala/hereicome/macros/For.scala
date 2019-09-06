package hereicome.macros

import scala.quoted._

inline def _for(init: => Any)(cond: => Boolean)(inc: => Any)(body: => Any): Unit =
  ${forImpl('init, 'cond, 'inc, 'body)}

def forImpl(init: Expr[Any], cond: Expr[Boolean], inc: Expr[Any], body: Expr[Any]) given QuoteContext = '{
  val w = '{
    while ($cond) {
      $body
      $inc
    }
  }
  init match {
    case '{i} =>
      $i; $w
    case _ =>
      $init; $w
  }
}

@main def forTest = 
  _for(var i = 0)({i < 10})(i += 1) {
    println(s"i = $i")
  }