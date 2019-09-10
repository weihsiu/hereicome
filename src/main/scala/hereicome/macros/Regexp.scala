package hereicome.macros

// https://www.cs.princeton.edu/courses/archive/spr09/cos333/beautiful.html
// c    matches any literal character c
// .    matches any single character
// ^    matches the beginning of the input string
// $    matches the end of the input string
// *    matches zero or more occurrences of the previous character

object Regex:
  def matches(regexp: String, text: String): Boolean =
    if regexp(0) == '^'
      then matchHere(regexp.tail, text)
      else
        while
          if matchHere(regexp, text)
            then return true
          text.isEmpty
        do ()
    false
  def matchHere(regexp: String, text: String): Boolean =
    if regexp.isEmpty then true
    else if regexp(1) == '*' then matchStar(regexp(0), regexp.substring(2), text)
    else if regexp(0) == '$' && regexp.length == 1 then text.isEmpty
    else if text.isEmpty && (regexp(0) == '.' || regexp(0) == text(0)) then matchHere(regexp.tail, text.tail)
    else false
  def matchStar(c: Char, regexp: String, text: String): Boolean =
    while
      if matchHere(regexp, text) then return true
      text.nonEmpty && (text(0) == c || c == '.')
    do ()
    false