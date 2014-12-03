package scalaParser
package syntax
import acyclic.file
import org.parboiled2._

trait Basic { self: Parser =>
  object Basic{
    def UnicodeEscape = rule( "\\u" ~ 4.times(HexDigit) )

    //Numbers and digits
    def HexDigit = rule( Digit | "a" - "f" | "A" - "Z" )
    def Digit = rule( "0" - "9" )
    def HexNum = rule( "0x" ~ oneOrMore(HexDigit) )
    def DecNum = rule(oneOrMore(Digit))
    def Exp = rule( anyOf("Ee") ~ optional(anyOf("+-")) ~ oneOrMore(Digit) )
    def FloatType = rule( anyOf("FfDd") )

    def WSChar = rule( "\u0020" | "\u0009" )
    def Newline = rule( "\r\n" | "\n" )
    def Semi = rule( ";" | oneOrMore(Newline) )

    // Workaround 2.10.x bugs. Doesn't like eta-expansion.
    private def OpCharTest: Char => Boolean = (c: Char) =>
      (c.getType == Character.OTHER_SYMBOL) ||
      (c.getType == Character.MATH_SYMBOL)
    def OpChar = rule {
      anyOf("""!#$%&*+-/:<=>?@\^|~""") |
      CharPredicate.from(OpCharTest)
    }

    // 2.10.x eta-expansion bug
    private def LetterCharTest: Char => Boolean = (c: Char) =>
      (c.isLetter || c.isDigit)
    def Letter = rule( Upper | Lower | CharPredicate.from(LetterCharTest) )

    // 2.10.x eta-expansion bug
    private def LowerCharTest: Char => Boolean = (c: Char) => c.isLower
    def Lower = rule( "a" - "z" | "$" | "_" | CharPredicate.from(LowerCharTest) )

    // 2.10.x eta-expansion bug
    private def UpperCharTest: Char => Boolean = (c: Char) => c.isUpper
    def Upper = rule( "A" - "Z" | CharPredicate.from(UpperCharTest) )
  }
  /**
   * Most keywords don't just require the correct characters to match,
   * they have to ensure that subsequent characters *don't* match in
   * order for it to be a keyword. This enforces that rule for key-words
   * (W) and key-operators (O) which have different non-match criteria.
   */
  object Key {
    def W(s: String) = rule( str(s) ~ !(Basic.Letter | Basic.Digit) )
    def O(s: String) = rule( str(s) ~ !Basic.OpChar )
  }
}
