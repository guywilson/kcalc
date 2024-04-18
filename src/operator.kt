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
    POWER,
    AND,
    OR,
    XOR,
    LSHIFT,
    RSHIFT,
    UNKNOWN
}

enum class Associativity {
    LEFT,
    RIGHT
}

open class OperationUtils {
    companion object StaticMembers {
        public var toChar: (Operation) -> Char = {
            operation: Operation -> 
                when (operation) {
                    Operation.ADD       -> '+'
                    Operation.SUBTRACT  -> '-'
                    Operation.MULTIPLY  -> '*'
                    Operation.DIVIDE    -> '/'
                    Operation.MOD       -> '%'
                    Operation.POWER     -> '^'
                    Operation.AND       -> '&'
                    Operation.OR        -> '|'
                    Operation.XOR       -> '~'
                    Operation.LSHIFT    -> '<'
                    Operation.RSHIFT    -> '>'
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
                '^' -> return Operation.POWER
                '&' -> return Operation.AND
                '|' -> return Operation.OR
                '~' -> return Operation.XOR
                '<' -> return Operation.LSHIFT
                '>' -> return Operation.RSHIFT
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
                Operation.POWER     -> return 4
                Operation.AND       -> return 4
                Operation.OR        -> return 4
                Operation.XOR       -> return 4
                Operation.LSHIFT    -> return 4
                Operation.RSHIFT    -> return 4
                else                -> return 0
            }
        }

        public fun getAssociativity(operation: Operation) : Associativity {
            when (operation) {
                Operation.ADD       -> return Associativity.LEFT
                Operation.SUBTRACT  -> return Associativity.LEFT
                Operation.MULTIPLY  -> return Associativity.LEFT
                Operation.DIVIDE    -> return Associativity.LEFT
                Operation.MOD       -> return Associativity.LEFT
                Operation.POWER     -> return Associativity.RIGHT
                Operation.AND       -> return Associativity.LEFT
                Operation.OR        -> return Associativity.LEFT
                Operation.XOR       -> return Associativity.LEFT
                Operation.LSHIFT    -> return Associativity.RIGHT
                Operation.RSHIFT    -> return Associativity.RIGHT
                else                -> return Associativity.LEFT
            }
        }

        public fun toString(operation: Operation) : String {
            when (operation) {
                Operation.ADD       -> return "+"
                Operation.SUBTRACT  -> return "-"
                Operation.MULTIPLY  -> return "*"
                Operation.DIVIDE    -> return "/"
                Operation.MOD       -> return "%"
                Operation.POWER     -> return "^"
                Operation.AND       -> return "&"
                Operation.OR        -> return "|"
                Operation.XOR       -> return "~"
                Operation.LSHIFT    -> return "<"
                Operation.RSHIFT    -> return ">"
                else                -> return ""
            }
        }
    }
}
open class DecimalOperation {
    companion object StaticMembers {
        public fun evaluate(operation: Operation, o1: BigDecimal, o2: BigDecimal) : String {
            val op: String = OperationUtils.toString(operation)
            Utils.debug("Evaluate $o1 $op $o2")

            when (operation) {
                Operation.ADD       -> return o1.add(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.SUBTRACT  -> return o1.subtract(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.MULTIPLY  -> return o1.multiply(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.DIVIDE    -> return o1.divide(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.MOD       -> return o1.remainder(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.POWER     -> return o1.pow(o2.toInt(), Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.AND       -> return BigDecimal(o1.toLong() and o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.OR        -> return BigDecimal(o1.toLong() or o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.XOR       -> return BigDecimal(o1.toLong() xor o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.LSHIFT    -> return BigDecimal(o1.toLong() shl o2.toInt()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                Operation.RSHIFT    -> return BigDecimal(o1.toLong() shr o2.toInt()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                else                -> return BigDecimal(0).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
            }
        }
    }
}

open class IntegerOperation {
    companion object StaticMembers {
        public fun evaluate(operation: Operation, radix: Int, o1: BigInteger, o2: BigInteger) : String {
            val op: String = OperationUtils.toString(operation)
            Utils.debug("Evaluate $o1 $op $o2")
            
            when (operation) {
                Operation.ADD       -> return o1.add(o2).toString(radix).uppercase()
                Operation.SUBTRACT  -> return o1.subtract(o2).toString(radix).uppercase()
                Operation.MULTIPLY  -> return o1.multiply(o2).toString(radix).uppercase()
                Operation.DIVIDE    -> return o1.divide(o2).toString(radix).uppercase()
                Operation.MOD       -> return o1.remainder(o2).toString(radix).uppercase()
                Operation.POWER     -> return o1.pow(o2.toInt()).toString(radix).uppercase()
                Operation.AND       -> return BigInteger((o1.toLong() and o2.toLong()).toString(), radix).toString(radix).uppercase()
                Operation.OR        -> return BigInteger((o1.toLong() or o2.toLong()).toString(), radix).toString(radix).uppercase()
                Operation.XOR       -> return BigInteger((o1.toLong() xor o2.toLong()).toString(), radix).toString(radix).uppercase()
                Operation.LSHIFT    -> return BigInteger((o1.toLong() shl o2.toInt()).toString(), radix).toString(radix).uppercase()
                Operation.RSHIFT    -> return BigInteger((o1.toLong() shr o2.toInt()).toString(), radix).toString(radix).uppercase()
                else                -> return BigInteger("0", radix).toString(radix).uppercase()
            }
        }
    }
}
