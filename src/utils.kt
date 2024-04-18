package com.guy.calc

import kotlin.math.PI
import java.math.MathContext
import java.math.RoundingMode
import java.math.BigDecimal

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
        public const val MAX_PRECISION: Int = 80
    
        public var mathContext: MathContext = 
                    MathContext(MAX_PRECISION, RoundingMode.HALF_UP)

        private var degreesToRadians: BigDecimal = 
            BigDecimal(Constant.evaluate("pi"))
                .divide(BigDecimal(180.0, Utils.mathContext), MAX_PRECISION, RoundingMode.HALF_UP)
        
        private var radiansToDegrees: BigDecimal = 
            BigDecimal(180.0, Utils.mathContext)
                .divide(BigDecimal(Constant.evaluate("pi")), MAX_PRECISION, RoundingMode.HALF_UP)

        public var isWhiteSpace: (Char) -> Boolean = { ch: Char -> ch in " \t\n\r" }
        public var isToken: (Char) ->      Boolean = { ch: Char -> ch in " \t\n\r+-*/%^&|~()[]{}" }
        public var isDigit: (Char) ->      Boolean = { ch: Char -> ch in "0123456789abcdefABCDEF" }
        public var isBrace: (Char) ->      Boolean = { ch: Char -> ch in "([{}])" }
        public var isBraceLeft: (Char) ->  Boolean = { ch: Char -> ch in "([{" }
        public var isBraceRight: (Char) -> Boolean = { ch: Char -> ch in "}])" }
        public var isOperator: (Char) ->   Boolean = { ch: Char -> ch in "+-*/%&|~^" }

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

        public fun isConstant(token: String) : Boolean {
            when (token) {
                "pi"    -> return true
                "c"     -> return true
                "g"     -> return true
                "eu"    -> return true
                else    -> return false
            }
        }

        public fun isFunction(token: String) : Boolean {
            when (token) {
                "sin"   -> return true
                "cos"   -> return true
                "tan"   -> return true
                "asin"  -> return true
                "acos"  -> return true
                "atan"  -> return true
                "sinh"  -> return true
                "cosh"  -> return true
                "tanh"  -> return true
                "asinh" -> return true
                "acosh" -> return true
                "atanh" -> return true
                "sqrt"  -> return true
                "log"   -> return true
                "ln"    -> return true
                "fact"  -> return true
                else    -> return false
            }
        }

        public fun toRadians(degrees: BigDecimal) : BigDecimal {
            return degrees.multiply(degreesToRadians, Utils.mathContext)
        }

        public fun toDegrees(radians: BigDecimal) : BigDecimal {
            return radians.multiply(radiansToDegrees, Utils.mathContext)
        }

        public var scale: Int = DEFAULT_SCALE
    }
}
