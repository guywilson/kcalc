package com.guy.calc

import kotlin.io.*
import kotlin.collections.*

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp.Capability
import java.math.BigDecimal
import java.math.RoundingMode
import java.math.BigInteger

enum class Base(val radix: Int) {
    DECIMAL(10),
    HEXADECIMAL(16),
    OCTAL(8),
    BINARY(2)
}

fun printHelp() {

}

fun main() {
    println("Welcome to Calc. A cmd line scientific calculator. Copyright © Guy Wilson ")
    println("Type a calculation or command at the prompt, type 'help' for info.")
    println("")

    val terminal: Terminal = TerminalBuilder.builder().build()

    var reader: LineReader = 
        LineReaderBuilder.builder()
            .terminal(terminal)
            .parser(DefaultParser())
            .build()
    
    reader.setOpt(LineReader.Option.AUTO_FRESH_LINE)

    var go: Boolean = true
    var base: Base = Base.DECIMAL

    while (go) {
        val baseString: String = 
            when (base) {
                Base.DECIMAL -> "DEC"
                Base.HEXADECIMAL -> "HEX"
                Base.OCTAL -> "OCT"
                Base.BINARY -> "BIN"
            }

        val calculation: String = reader.readLine("calc [$baseString]> ")

        if (calculation == "exit" || calculation.startsWith('q')) {
            go = false
        }
        else if (calculation.startsWith("help", false) || calculation.startsWith("?")) {
            printHelp()
        }
        else if (calculation.startsWith("dec", false)) {
            base = Base.DECIMAL
        }
        else if (calculation.startsWith("hex", false)) {
            base = Base.HEXADECIMAL
        }
        else if (calculation.startsWith("oct", false)) {
            base = Base.OCTAL
        }
        else if (calculation.startsWith("bin", false)) {
            base = Base.BINARY
        }
        else if (calculation.startsWith("setp", false)) {
            var s: Int? = calculation.substring(4).toIntOrNull()

            if (s != null) {
                Utils.scale = s
            }
        }
        else {
            var result: String = evaluate(calculation, base)

            println("$calculation = $result")
        }
    }
}

fun convertToRPN(expr: String, outQueue: ArrayDeque<String>) {
    var operatorStack: ArrayDeque<Char> = ArrayDeque<Char>(24)

    var tok: Tokeniser = Tokeniser(expr)

    while (tok.hasMoreTokens()) {
        val token: String = tok.nextToken()

//        println("Got token: '$token'")

        if (Utils.isOperand(token)) {
            outQueue.add(token)
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
            val o1: Char = token[0]

            while (!operatorStack.isEmpty()) {
                var stackToken: Char = operatorStack.peek()

                if (!Utils.isOperator(stackToken)) {
                    break
                }

                var o2: Char = stackToken

                if (OperationUtils.getPrecedence(OperationUtils.getOperation(o1)) <= OperationUtils.getPrecedence(OperationUtils.getOperation(o2))) {
                    o2 = operatorStack.pop()
                    outQueue.add(o2.toString())
                }
                else {
                    break
                }
            }

            operatorStack.push(o1)
        }
        else if (Utils.isBrace(token[0])) {
            /*
            ** If the token is a left parenthesis (i.e. "("), then push it onto the stack.
            */
            if (Utils.isBraceLeft(token[0])) {
                operatorStack.push(token[0])
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
                    val stackToken: Char = operatorStack.pop()

//                    val ch: Char = stackToken.toChar()
//                    println("Popped token '$ch' off the stack")

                    if (Utils.isBrace(stackToken)) {
                        if (Utils.isBraceLeft(stackToken)) {
                            foundLeftParenthesis = true
                            break
                        }
                    }
                    else {
                        outQueue.add(stackToken.toString())
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
        val op: Char = operatorStack.pop()

        if (Utils.isBrace(op)) {
            /*
            ** If we've got here, we must have unmatched parenthesis...
            */
            throw Exception("Found too many parenthesis on operator stack")
        }
        else {
            outQueue.add(op.toString())
        }
    }
}

fun evaluate(expression: String, base: Base) : String {
    var queue: ArrayDeque<String> = ArrayDeque<String>(25)

    convertToRPN(expression, queue)

    var stack: ArrayDeque<String> = ArrayDeque<String>(25)

    while (!queue.isEmpty()) {
        val t: String = queue.poll()

        if (Utils.isOperand(t)) {
            stack.push(t)
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
    }

    /*
    ** If there is one and only one item left on the stack,
    ** it is the result of the calculation. Otherwise, we
    ** have too many tokens and therefore an error...
    */
    var result: String

    if (stack.size == 1) {
        result = stack.pop()
    }
    else {
        throw Exception("Too many arguments left on stack")
    }

    return result
}
