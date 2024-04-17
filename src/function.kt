package com.guy.calc

import kotlin.math.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

enum class Function {
    SIN,
    COS,
    TAN,
    ASIN,
    ACOS,
    ATAN,
    SINH,
    COSH,
    TANH,
    ASINH,
    ACOSH,
    ATANH,
    SQRT,
    LOG,
    LOGN,
    FACT,
    UNKNOWN
}

open class FunctionUtils constructor() {
    companion object StaticMembers {
        public fun evaluate(function: Function, o1: BigDecimal) : String {
            when (function) {
                Function.SIN    -> return BigDecimal(sin(Utils.toRadians(o1).toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.COS    -> return BigDecimal(cos(Utils.toRadians(o1).toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.TAN    -> return BigDecimal(tan(Utils.toRadians(o1).toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ASIN   -> return Utils.toDegrees(BigDecimal(asin(o1.toDouble()))).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ACOS   -> return Utils.toDegrees(BigDecimal(acos(o1.toDouble()))).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ATAN   -> return Utils.toDegrees(BigDecimal(atan(o1.toDouble()))).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.SINH   -> return BigDecimal(sinh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.COSH   -> return BigDecimal(cosh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.TANH   -> return BigDecimal(tanh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ASINH  -> return BigDecimal(asinh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ACOSH  -> return BigDecimal(acosh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.ATANH  -> return BigDecimal(atanh(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.SQRT   -> return BigDecimal(sqrt(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.LOG    -> return BigDecimal(log10(o1.toDouble())).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.LOGN   -> return BigDecimal(log(o1.toDouble(), 10.0)).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                Function.FACT   -> return factorial(o1).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
                else            -> return BigDecimal.ZERO.setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
            }
        }

        public fun getFunction(token: String) : Function {
            when (token) {
                "sin"   -> return Function.SIN
                "cos"   -> return Function.COS
                "tan"   -> return Function.TAN
                "asin"  -> return Function.ASIN
                "acos"  -> return Function.ACOS
                "atan"  -> return Function.ATAN
                "sinh"  -> return Function.SINH
                "cosh"  -> return Function.COSH
                "tanh"  -> return Function.TANH
                "asinh" -> return Function.ASINH
                "acosh" -> return Function.ACOSH
                "atanh" -> return Function.ATANH
                "sqrt"  -> return Function.SQRT
                "log"   -> return Function.LOG
                "ln"    -> return Function.LOGN
                "fact"  -> return Function.FACT
                else    -> return Function.UNKNOWN
            }
        }

        private fun factorial(value: BigDecimal) : BigDecimal {
            var intValue: BigInteger = value.toBigInteger().abs()

            var result: BigInteger = BigInteger.ZERO

            while (intValue.compareTo(BigInteger.ZERO) > 0) {
                result = intValue.multiply(intValue.subtract(BigInteger.ONE))

                intValue = intValue.subtract(BigInteger.ONE)
            }

            return BigDecimal(result)
        }
    }
}