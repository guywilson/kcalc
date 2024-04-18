package com.guy.calc

import java.math.BigDecimal
import java.math.RoundingMode

open class Constant constructor() {
    companion object StaticMembers {
        private const val PIE: String = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899"
        private const val EULER: String = "0.57721566490153286060651209008240243104215933593992359880576723488486772677766467"
        private const val GRAVITY: String = "0.000000000066743"
        private const val LIGHTSPEED: String = "299792458"

        public fun evaluate(token: String) : String {
            when (token.lowercase()) {
                "pi"    -> return PIE
                "eu"    -> return EULER
                "g"     -> return GRAVITY
                "c"     -> return LIGHTSPEED
                else    -> return BigDecimal.ZERO.setScale(Utils.MAX_PRECISION, RoundingMode.HALF_UP).toPlainString()
            }
        }
    }
}
