package hereicome.macros

import scala.quoted._

inline def _for(init: => Any)(cond: => Boolean)(inc: => Any)(body: => Any): Unit =
  ${forImpl('init, 'cond, 'inc, 'body)}

def forImpl(init: Expr[Any], cond: Expr[Boolean], inc: Expr[Any], body: Expr[Any]) given (ctx: QuoteContext) = {
  import ctx.tasty._
  println(init.toString)
  println(init.unseal)
  '{}
}