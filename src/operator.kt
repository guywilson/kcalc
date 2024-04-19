package com.guy.calc

import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

enum class Associativity {
    LEFT,
    RIGHT
}

open class Operator {
    companion object StaticMembers {
        public fun evaluate(operator: String, base: Base, operand1: String, operand2: String) : String {
            Utils.debug("Evaluate $operand1 $operator $operand2")

            if (base == Base.DECIMAL) {
                val o1: BigDecimal = BigDecimal(operand1, Utils.mathContext)
                val o2: BigDecimal = BigDecimal(operand2, Utils.mathContext)

                when (operator[0]) {
                    '+'     -> return o1.add(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '-'     -> return o1.subtract(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '*'     -> return o1.multiply(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '/'     -> return o1.divide(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '%'     -> return o1.remainder(o2, Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '^'     -> return o1.pow(o2.toInt(), Utils.mathContext).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '&'     -> return BigDecimal(o1.toLong() and o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '|'     -> return BigDecimal(o1.toLong() or o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '~'     -> return BigDecimal(o1.toLong() xor o2.toLong()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '<'     -> return BigDecimal(o1.toLong() shl o2.toInt()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    '>'     -> return BigDecimal(o1.toLong() shr o2.toInt()).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                    else    -> return BigDecimal(0).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                }
            }
            else {
                val o1: BigInteger = BigInteger(operand1, base.radix)
                val o2: BigInteger = BigInteger(operand2, base.radix)
            
                when (operator[0]) {
                    '+'     -> return o1.add(o2).toString(base.radix).uppercase()
                    '-'     -> return o1.subtract(o2).toString(base.radix).uppercase()
                    '*'     -> return o1.multiply(o2).toString(base.radix).uppercase()
                    '/'     -> return o1.divide(o2).toString(base.radix).uppercase()
                    '%'     -> return o1.remainder(o2).toString(base.radix).uppercase()
                    '^'     -> return o1.pow(o2.toInt()).toString(base.radix).uppercase()
                    '&'     -> return BigInteger((o1.toLong() and o2.toLong()).toString(), base.radix).toString(base.radix).uppercase()
                    '|'     -> return BigInteger((o1.toLong() or o2.toLong()).toString(), base.radix).toString(base.radix).uppercase()
                    '~'     -> return BigInteger((o1.toLong() xor o2.toLong()).toString(), base.radix).toString(base.radix).uppercase()
                    '<'     -> return BigInteger((o1.toLong() shl o2.toInt()).toString(), base.radix).toString(base.radix).uppercase()
                    '>'     -> return BigInteger((o1.toLong() shr o2.toInt()).toString(), base.radix).toString(base.radix).uppercase()
                    else    -> return BigInteger("0", base.radix).toString(base.radix).uppercase()
                }
            }
        }

        public fun getPrecedence(operator: String) : Int {
            when (operator[0]) {
                '+'     -> return 2
                '-'     -> return 2
                '*'     -> return 3
                '/'     -> return 3
                '%'     -> return 3
                '^'     -> return 4
                '&'     -> return 4
                '|'     -> return 4
                '~'     -> return 4
                '<'     -> return 4
                '>'     -> return 4
                else    -> return 0
            }
        }

        public fun getAssociativity(operator: String) : Associativity {
            when (operator[0]) {
                '+'     -> return Associativity.LEFT
                '-'     -> return Associativity.LEFT
                '*'     -> return Associativity.LEFT
                '/'     -> return Associativity.LEFT
                '%'     -> return Associativity.LEFT
                '^'     -> return Associativity.RIGHT
                '&'     -> return Associativity.LEFT
                '|'     -> return Associativity.LEFT
                '~'     -> return Associativity.LEFT
                '<'     -> return Associativity.RIGHT
                '>'     -> return Associativity.RIGHT
                else    -> return Associativity.LEFT
            }
        }
    }
}
