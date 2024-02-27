package com.guy.calc

import com.guy.calc.Operand
import java.math.BigDecimal

enum class Operation {
    ADD, 
    SUBTRACT, 
    MULTIPLY, 
    DIVIDE, 
    MOD
}

open class Operator constructor(op: Operation) {

    val operation: Operation = op

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
}
