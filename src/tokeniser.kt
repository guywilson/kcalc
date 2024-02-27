package com.guy.calc

class Tokeniser constructor(expression: String) {
    private val expr: String = expression
    private val exprLen: Int = expression.length

    private var startIndex: Int = 0
    private var endIndex: Int = 0

    private var isWhiteSpace: (Char) -> Boolean = { ch: Char -> ch in " \t\n\r" }
    private var isToken: (Char) ->      Boolean = { ch: Char -> ch in " \t\n\r+-*/%()[]{}" }
    private var isDigit: (Char) ->      Boolean = { ch: Char -> ch in "0123456789" }
    private var isBrace: (Char) ->      Boolean = { ch: Char -> ch in "([{}])" }
    private var isBraceLeft: (Char) ->  Boolean = { ch: Char -> ch in "([{" }
    private var isBraceRight: (Char) -> Boolean = { ch: Char -> ch in "}])" }

    private fun findNextTokenPos() : Int {
        var i:                      Int = startIndex
        var ch:                     Char
        var lookingForWhiteSpace:   Boolean = true
        var tokenLength:            Int = 0

        while (i < exprLen) {
            ch = expr[i]

            if (lookingForWhiteSpace) {
                if (isWhiteSpace(ch)) {
                    startIndex++
                    continue
                }
            }
            else {
                lookingForWhiteSpace = false
            }

            if (isWhiteSpace(ch)) {
                return i
            }
            if (isToken(ch)) {
                /*
                ** Do we have a token on it's own, or is it a delimiter...
                */
                if (tokenLength > 0) {
                    // The token is the delimiter to another token...
                    return i
                }
                else {
                    /*
                    ** If this is the '-' character and if the next char is a digit (0-9)
                    ** and the previous char is not a ')' or a digit then this must be a -ve number,
                    ** not the '-' operator...
                    */
                    if (ch == '-' && isDigit(expr[i + 1])) {
                        var isNegativeOperand: Boolean = false

                        if (i > 0) {
                            val isPreviousCharBrace: Boolean = isBraceRight(expr[i - 1])
                            val isPreviousCharDigit: Boolean = isDigit(expr[i - 1])

                            if (!isPreviousCharBrace && !isPreviousCharDigit) {
                                isNegativeOperand = true
                            }
                            else {
                                isNegativeOperand = false
                            }
                        }
                        else if (i == 0) {
                            // We're at the beginning of the expression, must be
                            // a -ve operand
                            isNegativeOperand = true
                        }

                        if (isNegativeOperand) {
                            // Found a -ve number...
                            continue
                        }
                        else {
                            return i + 1
                        }
                    }
                    else {
                        // The token is the token we want to return...
                        return i + 1
                    }
                }
            }

            tokenLength++

            /*
            ** If we haven't returned yet and we're at the end of
            ** the expression, we must have an operand at the end
            ** of the expression...
            */
            if (i == (exprLen - 1)) {
                return i + 1
            }

            i++
        }

        return (i + 1)
    }

    public fun hasMoreTokens() : Boolean {
        val pos: Int = findNextTokenPos()

        if (pos > 0) {
            endIndex = pos
            return true
        }
        else {
            return false
        }
    }
}
