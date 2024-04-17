package com.guy.calc

import java.math.MathContext
import java.math.RoundingMode

fun ArrayDeque<Char>.push(item: Char) {
    this.addFirst(item)
}

fun ArrayDeque<Char>.pop() : Char {
    return this.removeFirst()
}

fun ArrayDeque<Char>.peek() : Char {
    return this.first()
}

fun ArrayDeque<String>.push(item: String) {
    this.addFirst(item)
}

fun ArrayDeque<String>.pop() : String {
    return this.removeFirst()
}

fun ArrayDeque<String>.peek() : String {
    return this.first()
}

fun ArrayDeque<String>.poll() : String {
    return this.removeFirst()
}

class Utils {
    companion object StaticMembers {
        private const val DEFAULT_SCALE: Int = 2
        private const val MAX_DISPLAY_PRECISION: Int = 80

        public var mathContext: MathContext = 
                    MathContext(MAX_DISPLAY_PRECISION, RoundingMode.HALF_UP)

        public var isWhiteSpace: (Char) -> Boolean = { ch: Char -> ch in " \t\n\r" }
        public var isToken: (Char) ->      Boolean = { ch: Char -> ch in " \t\n\r+-*/%()[]{}" }
        public var isDigit: (Char) ->      Boolean = { ch: Char -> ch in "0123456789abcdefABCDEF" }
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
                for (i in 0..token.length - 1) {
                    if (!Utils.isDigit(token[i])) {
                        isop = false
                        break
                    }
                }
            }
    
            return isop
        }

        public var scale: Int = DEFAULT_SCALE
    }
}
