package com.guy.calc

import com.guy.calc.Operand
import java.math.BigDecimal

fun getOperation(ch: Char) : Operation {
    when (ch) {
        '+' -> return Operation.ADD
        '-' -> return Operation.SUBTRACT
        '*' -> return Operation.MULTIPLY
        '/' -> return Operation.DIVIDE
        '%' -> return Operation.MOD
        else -> return Operation.UNKNOWN
    }
}

enum class Operation {
    ADD, 
    SUBTRACT, 
    MULTIPLY, 
    DIVIDE, 
    MOD,
    UNKNOWN
}

open class Operator constructor(op: Operation) {

    val operation: Operation = op

    constructor(op: Char) : this(getOperation(op)) {

    }

    private fun add(o1: Operand, o2: Operand) : Operand {
        return Operand(o1.decimalValue.add(o2.decimalValue))
    }

    private fun subtract(o1: Operand, o2: Operand) : Operand {
        return Operand(o1.decimalValue.subtract(o2.decimalValue))
    }

    private fun multiply(o1: Operand, o2: Operand) : Operand {
        return Operand(o1.decimalValue.multiply(o2.decimalValue))
    }

    private fun divide(o1: Operand, o2: Operand) : Operand {
        return Operand(o1.decimalValue.divide(o2.decimalValue))
    }

    private fun mod(o1: Operand, o2: Operand) : Operand {
        return Operand(o1.decimalValue.remainder(o2.decimalValue))
    }

    public fun evaluate(o1: Operand, o2: Operand) : Operand {
        when (operation) {
            Operation.ADD       -> return add(o1, o2)
            Operation.SUBTRACT  -> return subtract(o1, o2)
            Operation.MULTIPLY  -> return multiply(o1, o2)
            Operation.DIVIDE    -> return divide(o1, o2)
            Operation.MOD       -> return mod(o1, o2)
            else                -> return Operand(0.0)
        }
    }

    public fun getPrecedence() : Int {
        when (operation) {
            Operation.ADD       -> return 2
            Operation.SUBTRACT  -> return 2
            Operation.MULTIPLY  -> return 3
            Operation.DIVIDE    -> return 3
            Operation.MOD       -> return 3
            else                -> return 0
        }
    }

    public fun toChar() : Char {
        when (operation) {
            Operation.ADD       -> return '+'
            Operation.SUBTRACT  -> return '-'
            Operation.MULTIPLY  -> return '*'
            Operation.DIVIDE    -> return '/'
            Operation.MOD       -> return '%'
            else                -> return '0'
        }
    }

    public override fun toString() : String {
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
