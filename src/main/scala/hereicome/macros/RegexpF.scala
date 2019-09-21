package hereicome.macros

// https://www.cs.princeton.edu/courses/archive/spr09/cos333/beautiful.html
// c    matches any literal character c
// .    matches any single character
// ^    matches the beginning of the input string
// $    matches the end of the input string
// *    matches zero or more occurrences of the previous character

object RegexpF
  def matchRegexp(regexp: String, text: String): Boolean =
    def matchHere(ri: Int, ti: Int): Boolean =
      if ri == regexp.length then true
      else if ri + 1 < regexp.length && regexp(ri + 1) == '*' then matchStar(regexp(ri), ri + 2, ti)
      else if regexp(ri) == '$' && ri == regexp.length - 1 then ti == text.length
      else if ti != text.length && (regexp(ri) == '.' || regexp(ri) == text(ti)) then matchHere(ri + 1, ti + 1)
      else false
    def matchStar(c: Char, ri: Int, ti: Int): Boolean =
      var i = ti
      var r = false
      while
        if matchHere(ri, i) then r = true
        !r && i != text.length && (i + 1 < text.length && text(i + 1) == c || c == '.')
      do i += 1
      r
    if regexp(0) == '^'
      then matchHere(1, 0)
      else
        var i = 0
        var r = false
        while
          if matchHere(0, i) then r = true
          !r && i + 1 != text.length
        do i += 1
        r

@main def testRegexpF() =
  import RegexpF._
  assert(matchRegexp("abc", "abc123"))
  assert(matchRegexp("123", "abc123"))
  assert(!matchRegexp("^123", "abc123"))
  assert(!matchRegexp("abc$", "abc123"))
  assert(matchRegexp(".*123", "abc123"))
  assert(matchRegexp(".*12", "abc123"))
  assert(!matchRegexp(".*12$", "abc123"))
  assert(matchRegexp("^a.*3$", "abc123"))