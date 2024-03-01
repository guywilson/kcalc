package com.guy.calc
// hjkhkjh
class Utils {
    companion object CharUtils {
        public var isWhiteSpace: (Char) -> Boolean = { ch: Char -> ch in " \t\n\r" }
        public var isToken: (Char) ->      Boolean = { ch: Char -> ch in " \t\n\r+-*/%()[]{}" }
        public var isDigit: (Char) ->      Boolean = { ch: Char -> ch in "0123456789" }
        public var isBrace: (Char) ->      Boolean = { ch: Char -> ch in "([{}])" }
        public var isBraceLeft: (Char) ->  Boolean = { ch: Char -> ch in "([{" }
        public var isBraceRight: (Char) -> Boolean = { ch: Char -> ch in "}])" }
        public var isOperator: (Char) ->   Boolean = { ch: Char -> ch in "+-*/%" }

        public fun isOperand(token: String) : Boolean {
            var isop: Boolean = true
    
            if (token[0] == '-' && token.length == 1) {
                isop = false
            }
            else {
                for (i in 0..token.length) {
                    if (!Utils.isDigit(token[i])) {
                        isop = false
                        break
                    }
                }
            }
    
            return isop
        }
    }
}
