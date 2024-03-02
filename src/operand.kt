package com.guy.calc

import java.math.BigDecimal
import java.math.RoundingMode

open class Operand constructor(value: BigDecimal) : Token() {
    constructor(strValue: String) : this(BigDecimal(strValue)) {

    }

    constructor(doubleValue: Double) : this(BigDecimal(doubleValue)) {

    }

    public override fun toString() : String {
        return decimalValue.setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
    }

    public override fun toChar() : Char {
        return '0'
    }

    public var decimalValue: BigDecimal = value
}
