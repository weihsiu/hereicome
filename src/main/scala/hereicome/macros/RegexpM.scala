package hereicome.macros

// https://www.cs.princeton.edu/courses/archive/spr09/cos333/beautiful.html
// c    matches any literal character c
// .    matches any single character
// ^    matches the beginning of the input string
// $    matches the end of the input string
// *    matches zero or more occurrences of the previous character

object RegexpM:
  inline def matchRegexp(regexp: String, text: String): Boolean =
    if regexp(0) == '^'
      then matchHere(regexp, text, 1, 0)
      else
        var i = 0
        var r = false
        while
          if matchHere(regexp, text, 0, i) then r = true
          !r && i + 1 != text.length
        do i += 1
        r
  def matchHere(re: String, t: String, ri: Int, ti: Int): Boolean =
    if ri == re.length then true
    else if ri + 1 < re.length && re(ri + 1) == '*' then matchStar(re, t, re(ri), ri + 2, ti)
    else if re(ri) == '$' && ri == re.length - 1 then ti == t.length
    else if ti != t.length && (re(ri) == '.' || re(ri) == t(ti)) then matchHere(re, t, ri + 1, ti + 1)
    else false
  def matchStar(re: String, t: String, c: Char, ri: Int, ti: Int): Boolean =
    var i = ti
    var r = false
    while
      if matchHere(re, t, ri, i) then r = true
      !r && i != t.length && (i + 1 < t.length && t(i + 1) == c || c == '.')
    do i += 1
    r

@main def testRegexpM() =
  import RegexpM._
  assert(matchRegexp("abc", "abc123"))
  assert(matchRegexp("123", "abc123"))
  assert(!matchRegexp("^123", "abc123"))
  assert(!matchRegexp("abc$", "abc123"))
  assert(matchRegexp(".*123", "abc123"))
  assert(matchRegexp(".*12", "abc123"))
  assert(!matchRegexp(".*12$", "abc123"))
  assert(matchRegexp("^a.*3$", "abc123"))