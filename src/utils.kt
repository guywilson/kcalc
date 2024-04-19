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

        private const val WHITESPACE: String = " \t\n\r"
        private const val DIGITS: String = "-.0123456789abcdefABCDEF"
        private const val OPERATORS: String = "+-*/%^&|~<>"
        private const val LEFT_BRACES: String = "([{"
        private const val RIGHT_BRACES: String = "}])"
        private const val BRACES: String = "$LEFT_BRACES$RIGHT_BRACES"
        private const val TOKENS: String = "$WHITESPACE$OPERATORS$BRACES"

        public const val MAX_PRECISION: Int = 80
        public const val MEMORY_SLOTS: Int = 10
    
        public var mathContext: MathContext = 
                    MathContext(MAX_PRECISION, RoundingMode.HALF_UP)

        private var degreesToRadians: BigDecimal = 
            BigDecimal(Constant.evaluate("pi"))
                .divide(BigDecimal(180.0, Utils.mathContext), MAX_PRECISION, RoundingMode.HALF_UP)
        
        private var radiansToDegrees: BigDecimal = 
            BigDecimal(180.0, Utils.mathContext)
                .divide(BigDecimal(Constant.evaluate("pi")), MAX_PRECISION, RoundingMode.HALF_UP)

        private var memory = Array<String>(10) {"0.00"}

        public var isWhiteSpace: (Char) -> Boolean = { ch: Char -> ch in WHITESPACE }
        public var isToken: (Char) ->      Boolean = { ch: Char -> ch in TOKENS }
        public var isDigit: (Char) ->      Boolean = { ch: Char -> ch in DIGITS }
        public var isBrace: (Char) ->      Boolean = { ch: Char -> ch in BRACES }
        public var isBraceLeft: (Char) ->  Boolean = { ch: Char -> ch in LEFT_BRACES }
        public var isBraceRight: (Char) -> Boolean = { ch: Char -> ch in RIGHT_BRACES }
        public var isOperator: (Char) ->   Boolean = { ch: Char -> ch in OPERATORS }

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

        public fun memoryStore(result: String, location: Int) {
            if (location >= 0 && location < MEMORY_SLOTS) {
                memory[location] = result
            }
            else {
                throw IndexOutOfBoundsException()
            }
        }

        public fun memoryClear(location: Int) {
            if (location >= 0 && location < MEMORY_SLOTS) {
                memory[location] = "0.00"
            }
            else {
                throw IndexOutOfBoundsException()
            }
        }

        public fun memoryRetrieve(location: Int) : String {
            if (location >= 0 && location < MEMORY_SLOTS) {
                return memory[location]
            }
            else {
                throw IndexOutOfBoundsException()
            }
        }

        public fun getPrescedence(token: String) : Int {
            if (isOperator(token[0])) {
                return Operator.getPrecedence(token)
            }
            else if (isFunction(token)) {
                return Function.getPrescedence()
            }

            return 0;
        }

        public fun getAssociativity(token: String) : Associativity {
            if (isOperator(token[0])) {
                return Operator.getAssociativity(token)
            }
            else {
                return Associativity.LEFT
            }
        }

        public fun debug(message: String) {
            if (isDebug) {
                println(message)
            }
        }
        
        public var scale: Int = DEFAULT_SCALE
        public var isDebug: Boolean = false
    }
}
