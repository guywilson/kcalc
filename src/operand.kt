package com.guy.calc

import java.math.BigDecimal

open class Operand constructor(value: BigDecimal) {

    constructor(strValue: String) : this(BigDecimal(strValue)) {

    }

    constructor(doubleValue: Double) : this(BigDecimal(doubleValue)) {

    }

    public var decimalValue: BigDecimal = value
        get() = decimalValue
        set
}
