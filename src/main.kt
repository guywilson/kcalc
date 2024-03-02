package com.guy.calc

import kotlin.io.*
import kotlin.collections.*

import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp.Capability

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

    while (go) {
        val calculation = reader.readLine("calc > ")

        if (calculation == "exit" || calculation.startsWith('q')) {
            go = false;
        }
        else if (calculation.startsWith("setp", false)) {
            var s: Int? = calculation.substring(4).toIntOrNull()

            if (s != null) {
                Utils.scale = s
            }
        }
        else {
            var result: Operand = evaluate(calculation)
            var resultString: String = result.toString()

            println("$calculation = $resultString")
        }
    }
}

fun convertToRPN(expr: String, outQueue: ArrayDeque<String>) {
    var operatorStack: ArrayDeque<Token> = ArrayDeque<Token>(24)

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
            val o1: Operator = Operator(token[0])

            while (!operatorStack.isEmpty()) {
                var stackToken: Token = operatorStack.peek()

                if (stackToken !is Operator) {
                    break
                }

                var o2: Operator = stackToken

                if (o1.getPrecedence() <= o2.getPrecedence()) {
                    o2 = operatorStack.pop() as Operator
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
                operatorStack.push(Brace(token[0]))
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
                    val stackToken: Token = operatorStack.pop()

//                    val ch: Char = stackToken.toChar()
//                    println("Popped token '$ch' off the stack")

                    if (Utils.isBrace(stackToken.toChar())) {
                        if (Utils.isBraceLeft(stackToken.toChar())) {
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
        val op: Operator = operatorStack.pop() as Operator

        if (Utils.isBrace(op.toChar())) {
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

fun evaluate(expression: String) : Operand {
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

            val o2: Operand = Operand(stack.pop())
            val o1: Operand = Operand(stack.pop())

            val op: Operator = Operator(t[0])

            val r: Operand = op.evaluate(o1, o2)

            stack.push(r.toString())
        }
    }

    /*
    ** If there is one and only one item left on the stack,
    ** it is the result of the calculation. Otherwise, we
    ** have too many tokens and therefore an error...
    */
    var result: Operand

    if (stack.size == 1) {
        result = Operand(stack.pop())
    }
    else {
        throw Exception("Too many arguments left on stack")
    }

    return result
}
