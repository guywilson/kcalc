package com.guy.calc

import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

fun convertToRPN(expr: String, base: Base, outQueue: ArrayDeque<String>) {
    var operatorStack: ArrayDeque<String> = ArrayDeque<String>(24)

    var tok: Tokeniser = Tokeniser(expr)

    while (tok.hasMoreTokens()) {
        val token: String = tok.nextToken()

        Utils.debug("Got token: '$token'")

        if (Utils.isOperand(token)) {
            outQueue.add(token)
        }
        else if (Utils.isConstant(token)) {
            when (base) {
                Base.DECIMAL    -> outQueue.add(token)
                else            -> throw Exception("Constants are only supported in DECimal mode")
            }
        }
        else if (Utils.isFunction(token)) {
            operatorStack.push(token)
        }
        /*
        ** If the token is an operator, o1, then:
        **	while there is an operator token o2, at the top of the operator stack
        **	and either
        **	o1 is left-associative and its precedence is less than or equal to that
        **	of o2, or
        **	o1 is right associative, and has precedence less than that of o2,
        **	pop o2 off the operator stack, onto the output queue;
        **	at the end of iteration push o1 onto the operator stack.
        */
        else if (Utils.isOperator(token[0])) {
            val o1: String = token

            while (!operatorStack.isEmpty()) {
                var stackToken: String = operatorStack.peek()

                if (!Utils.isOperator(stackToken[0]) && !Utils.isFunction(stackToken)) {
                    break
                }

                var o2: String = stackToken

                val assoc: Associativity = 
                                Utils.getAssociativity(o1)
                val o1Prescedence: Int = Utils.getPrescedence(o1)
                val o2Prescedence: Int = Utils.getPrescedence(o2)

                if ((assoc == Associativity.LEFT && o1Prescedence <= o2Prescedence) ||
                    (assoc == Associativity.RIGHT && o1Prescedence < o2Prescedence))
                {
                    o2 = operatorStack.pop()
                    outQueue.add(o2)
                }
                else {
                    break
                }
            }

            operatorStack.push(o1.toString())
        }
        else if (Utils.isBrace(token[0])) {
            /*
            ** If the token is a left parenthesis (i.e. "("), then push it onto the stack.
            */
            if (Utils.isBraceLeft(token[0])) {
                operatorStack.push(token)
            }
            else {
                /*
                ** If the token is a right parenthesis (i.e. ")"):
                ** Until the token at the top of the stack is a left parenthesis, pop
                ** operators off the stack onto the output queue.
                ** Pop the left parenthesis from the stack, but not onto the output queue.
                ** If the token at the top of the stack is a function token, pop it onto
                ** the output queue.
                ** If the stack runs out without finding a left parenthesis, then there
                ** are mismatched parentheses.
                */
                var foundLeftParenthesis: Boolean = false

                while (!operatorStack.isEmpty()) {
                    val stackToken: String = operatorStack.pop()

                    Utils.debug("Popped token '$stackToken' off the stack")

                    if (Utils.isBrace(stackToken[0])) {
                        if (Utils.isBraceLeft(stackToken[0])) {
                            foundLeftParenthesis = true
                            break
                        }
                    }
                    else {
                        outQueue.add(stackToken)
                    }
                }

                if (!foundLeftParenthesis) {
                    /*
                    ** If we've got here, we must have unmatched parenthesis...
                    */
                    throw Exception("Failed to find left parenthesis on operator stack")
                }
            }
        }
    }

    /*
    ** While there are still operator tokens in the stack:
    ** If the operator token on the top of the stack is a parenthesis,
    ** then there are mismatched parentheses.
    ** Pop the operator onto the output queue.
    */
    while (!operatorStack.isEmpty()) {
        val op: String = operatorStack.pop()

        if (Utils.isBrace(op[0])) {
            /*
            ** If we've got here, we must have unmatched parenthesis...
            */
            throw Exception("Found too many parenthesis on operator stack")
        }
        else {
            outQueue.add(op)
        }
    }
}

fun evaluate(expression: String, base: Base) : String {
    var queue: ArrayDeque<String> = ArrayDeque<String>(25)

    convertToRPN(expression, base, queue)

    var stack: ArrayDeque<String> = ArrayDeque<String>(25)

    while (!queue.isEmpty()) {
        val t: String = queue.poll()

        if (Utils.isOperand(t)) {
            stack.push(t)
        }
        else if (Utils.isConstant(t)) {
            when (base) {
                Base.DECIMAL    -> stack.push(Constant.evaluate(t))
                else            -> throw Exception("Constants are only supported in DECimal mode")
            }
        }
        else if (Utils.isOperator(t[0])) {
            if (stack.size < 2) {
                throw Exception("Too few arguments for operator")
            }

            when (base) {
                Base.DECIMAL -> {
                    val o2: BigDecimal = BigDecimal(stack.pop(), Utils.mathContext)
                    val o1: BigDecimal = BigDecimal(stack.pop(), Utils.mathContext)

                    val op: Operation = OperationUtils.getOperation(t[0])

                    stack.push(DecimalOperation.evaluate(op, o1, o2))
                }
                else -> {
                    val o2: BigInteger = BigInteger(stack.pop(), base.radix)
                    val o1: BigInteger = BigInteger(stack.pop(), base.radix)

                    val op: Operation = OperationUtils.getOperation(t[0])

                    stack.push(IntegerOperation.evaluate(op, base.radix, o1, o2))
                }
            }
        }
        else if (Utils.isFunction(t)) {
            if (stack.size < 1) {
                throw Exception("Too few arguments for function")
            }

            when (base) {
                Base.DECIMAL -> {
                    val o1: BigDecimal = BigDecimal(stack.pop(), Utils.mathContext)

                    stack.push(Function.evaluate(t, o1))
                }
                else -> {
                    throw Exception("Functions are only supported in DECimal mode")
                }
            }
        }
        else {
            throw Exception("Invalid token $t on queue")
        }
    }

    /*
    ** If there is one and only one item left on the stack,
    ** it is the result of the calculation. Otherwise, we
    ** have too many tokens and therefore an error...
    */
    var result: String

    if (stack.size == 1) {
        result = BigDecimal(stack.pop()).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
    }
    else {
        throw Exception("Too many arguments left on stack")
    }

    return result
}
