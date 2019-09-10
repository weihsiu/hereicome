package hereicome.macros

import For._for

@main def forTest = 
  _for({var i = 0})(false)(1) {
    println("what")
  }