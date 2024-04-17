package com.guy.calc

import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

enum class Operation {
    ADD, 
    SUBTRACT, 
    MULTIPLY, 
    DIVIDE, 
    MOD,
    UNKNOWN
}

open class OperationUtils constructor() {
    companion object StaticMembers {
        public var toChar: (Operation) -> Char = {
            operation: Operation -> 
                when (operation) {
                    Operation.ADD       -> '+'
                    Operation.SUBTRACT  -> '-'
                    Operation.MULTIPLY  -> '*'
                    Operation.DIVIDE    -> '/'
                    Operation.MOD       -> '%'
                    else                -> '0'
                }
        }

        public fun getOperation(ch: Char) : Operation {
            when (ch) {
                '+' -> return Operation.ADD
                '-' -> return Operation.SUBTRACT
                '*' -> return Operation.MULTIPLY
                '/' -> return Operation.DIVIDE
                '%' -> return Operation.MOD
                else -> return Operation.UNKNOWN
            }
        }

        public fun getPrecedence(operation: Operation) : Int {
            when (operation) {
                Operation.ADD       -> return 2
                Operation.SUBTRACT  -> return 2
                Operation.MULTIPLY  -> return 3
                Operation.DIVIDE    -> return 3
                Operation.MOD       -> return 3
                else                -> return 0
            }
        }

        public fun toString(operation: Operation) : String {
            when (operation) {
                Operation.ADD       -> return "+"
                Operation.SUBTRACT  -> return "-"
                Operation.MULTIPLY  -> return "*"
                Operation.DIVIDE    -> return "/"
                Operation.MOD       -> return "%"
                else                -> return "0"
            }
        }
    }
}
open class DecimalOperation constructor() {
    companion object StaticMembers {
        public fun evaluate(operation: Operation, o1: BigDecimal, o2: BigDecimal) : String {
            when (operation) {
                Operation.ADD       -> return o1.add(o2, Utils.mathContext).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Operation.SUBTRACT  -> return o1.subtract(o2, Utils.mathContext).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Operation.MULTIPLY  -> return o1.multiply(o2, Utils.mathContext).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Operation.DIVIDE    -> return o1.divide(o2, Utils.mathContext).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Operation.MOD       -> return o1.remainder(o2, Utils.mathContext).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                else                -> return BigDecimal(0).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
            }
        }
    }
}

open class IntegerOperation constructor() {
    companion object StaticMembers {
        public fun evaluate(operation: Operation, radix: Int, o1: BigInteger, o2: BigInteger) : String {
            when (operation) {
                Operation.ADD       -> return o1.add(o2).toString(radix).uppercase()
                Operation.SUBTRACT  -> return o1.subtract(o2).toString(radix).uppercase()
                Operation.MULTIPLY  -> return o1.multiply(o2).toString(radix).uppercase()
                Operation.DIVIDE    -> return o1.divide(o2).toString(radix).uppercase()
                Operation.MOD       -> return o1.remainder(o2).toString(radix).uppercase()
                else                -> return BigInteger("0", radix).toString(radix).uppercase()
            }
        }
    }
}
