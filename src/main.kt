package com.guy.calc

import kotlin.io.*
import kotlin.collections.*

fun main() {
    println("Welcome to Calc. A cmd line scientific calculator. Copyright © Guy Wilson ")
    println("Type a calculation or command at the prompt, type 'help' for info.")
    println("")

    var go: Boolean = true

    while (go) {
        print("calc> ")

        val calculation = readln()

        if (calculation == "exit") {
            go = false;
        }
        else {
            println("You entered calculation '$calculation'")
        }
    }
}

fun convertToRPN(expr: String, outQueue: ArrayDeque<String>) {
    var operatorStack: ArrayDeque<Operator> = ArrayDeque<Operator>(24)

    var tok: Tokeniser = Tokeniser(expr)

    while (tok.hasMoreTokens()) {
        val token: String = tok.nextToken()

        if (Utils.isOperand(token)) {
            outQueue.addLast(token)
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
                var o2: Operator = operatorStack.first()

                if (!Utils.isOperator(o2.toChar())) {
                    break
                }

                if (o1.getPrecedence() <= o2.getPrecedence()) {
                    o2 = operatorStack.removeFirst()
                    outQueue.addLast(o2.toString())
                }
                else {
                    break
                }
            }

            operatorStack.addFirst(o1)
        }
        else if (Utils.isBrace(token[0])) {
            /*
            ** If the token is a left parenthesis (i.e. "("), then push it onto the stack.
            */
            if (Utils.isBraceLeft(token[0])) {
                operatorStack.addFirst(Operator(token[0]))
            }
            else {
                /*
                If the token is a right parenthesis (i.e. ")"):
                    Until the token at the top of the stack is a left parenthesis, pop
                    operators off the stack onto the output queue.
                    Pop the left parenthesis from the stack, but not onto the output queue.
                    If the token at the top of the stack is a function token, pop it onto
                    the output queue.
                    If the stack runs out without finding a left parenthesis, then there
                    are mismatched parentheses.
                */
                var foundLeftParenthesis: Boolean = false

                while (!operatorStack.isEmpty()) {
                    val stackToken: Operator = operatorStack.removeFirst()

                    if (Utils.isBrace(stackToken.toChar())) {
                        if (Utils.isBraceLeft(stackToken.toChar())) {
                            foundLeftParenthesis = true
                            break
                        }
                    }
                    else {
                        outQueue.addLast(stackToken.toString())
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
}

fun evaluate(expression: String) : Operand {
    val result = Operand(0.0)

    return result
}
