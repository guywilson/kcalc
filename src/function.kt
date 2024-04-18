package com.guy.calc

import kotlin.math.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

open class Function {
    companion object StaticMembers {
        public fun evaluate(token: String, o1: BigDecimal) : String {
            Utils.debug("Evaluate $token($o1)")

            when (token.lowercase()) {
                "sin"   -> return BigDecimal(sin(Utils.toRadians(o1).toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "cos"   -> return BigDecimal(cos(Utils.toRadians(o1).toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "tan"   -> return BigDecimal(tan(Utils.toRadians(o1).toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "asin"  -> return Utils.toDegrees(BigDecimal(asin(o1.toDouble()))).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "acos"  -> return Utils.toDegrees(BigDecimal(acos(o1.toDouble()))).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "atan"  -> return Utils.toDegrees(BigDecimal(atan(o1.toDouble()))).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "sinh"  -> return BigDecimal(sinh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "cosh"  -> return BigDecimal(cosh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "tanh"  -> return BigDecimal(tanh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "asinh" -> return BigDecimal(asinh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "acosh" -> return BigDecimal(acosh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "atanh" -> return BigDecimal(atanh(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "sqrt"  -> return BigDecimal(sqrt(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "log"   -> return BigDecimal(log10(o1.toDouble())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "ln"    -> return BigDecimal(log(o1.toDouble(), 10.0)).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "fact"  -> return factorial(o1).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "rad"   -> return Utils.toRadians(o1).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "deg"   -> return Utils.toDegrees(o1).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                "mem"   -> return BigDecimal(Utils.memoryRetrieve(o1.toInt())).setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
                else    -> return BigDecimal.ZERO.setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
            }
        }

        public fun getPrescedence() : Int {
            return 5
        }

        public fun getAssociativity() : Associativity {
            return Associativity.LEFT
        }

        private fun factorial(value: BigDecimal) : BigDecimal {
            var index: BigInteger = value.toBigInteger().abs().subtract(BigInteger.ONE)

            var result: BigInteger = value.toBigInteger()

            while (index.compareTo(BigInteger.ZERO) > 0) {
                result = result.multiply(index)
                index = index.subtract(BigInteger.ONE)

                Utils.debug("Result: $result")
            }

            return BigDecimal(result)
        }
    }
}
