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
import java.util.Calendar
import java.time.Year
//import com.guy.calc.Associativity

enum class Base(val radix: Int) {
    DECIMAL(10),
    HEXADECIMAL(16),
    OCTAL(8),
    BINARY(2)
}

fun printWarranty() {
    println("This program comes with ABSOLUTELY NO WARRANTY.")
    println("This is free software, and you are welcome to redistribute it")
    println("under certain conditions.")
    println()
}

fun printBanner() {
    val cal:Calendar = Calendar.getInstance()
    val year: Int = cal.get(Calendar.YEAR)

    println("Welcome to Calc. A cmd line scientific calculator. Copyright © Guy Wilson $year")
    println("Type a calculation or command at the prompt, type 'help' for info.")
    println()
}

fun printHelp() {
    printBanner()
    printWarranty()

    println("Operators supported:")
    println("\t+, -, *, /, %% (Modulo)")
    println("\t& (AND), | (OR), ~ (XOR)")
    println("\t< (left shift), > (right shift)")
    println("\t^ (power, e.g. x to the power of y)")
    println()
    println("\tNesting is achieved with braces ()")
    println()
    println("Functions supported:")
    println("\tsin(x)\treturn the sine of the angle x degrees")
    println("\tcos(x)\treturn the cosine of the angle x degrees")
    println("\ttan(x)\treturn the tangent of the angle x degrees")
    println("\tasin(x)\treturn the angle in degrees of arcsine(x)")
    println("\tacos(x)\treturn the angle in degrees of arccosine(x)")
    println("\tatan(x)\treturn the angle in degrees of arctangent(x)")
    println("\tsinh(x)\treturn the hyperbolic sine of the angle x radians")
    println("\tcosh(x)\treturn the hyperbolic cosine of the angle x radians")
    println("\ttanh(x)\treturn the hyperbolic tangent of the angle x radians")
    println("\tasinh(x) return the inverse hyperbolic sine of angle x in radians")
    println("\tacosh(x) return the inverse hyperbolic cosine of angle x in radians")
    println("\tatanh(x) return the inverse hyperbolic tangent of angle x in radians")
    println("\tsqrt(x)\treturn the square root of x")
    println("\tlog(x)\treturn the log of x")
    println("\tln(x)\treturn the natural log of x")
    println("\tfact(x)\treturn the factorial of x")
    println("\tmem(n)\tthe value in memory location n, where n is 0 - 9")
    println()
    println("Constants supported:")
    println("\tpi\tthe ratio pi")
    println("\teu\tEulers constant")
    println("\tg\tThe gravitational constant G")
    println("\tc\tthe speed of light in a vacuum")
    println()
    println("Commands supported:")
    println("\tmemstn\tStore the last result in memory location n (0 - 9)")
    println("\tdec\tSwitch to decimal mode")
    println("\thex\tSwitch to hexadecimal mode")
    println("\tbin\tSwitch to binary mode")
    println("\toct\tSwitch to octal mode")
    println("\tsetpn\tSet the precision to n")
    println("\thelp\tThis help text")
    println("\ttest\tRun a self test of the calculator")
    println("\tversion\tPrint the calculator version")
    println("\texit\tExit the calculator")
    println()
}

fun main() {
    printBanner()

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

fun convertToRPN(expr: String, base: Base, outQueue: ArrayDeque<String>) {
    var operatorStack: ArrayDeque<String> = ArrayDeque<String>(24)

    var tok: Tokeniser = Tokeniser(expr)

    while (tok.hasMoreTokens()) {
        val token: String = tok.nextToken()

        // println("Got token: '$token'")

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
            val o1: Char = token[0]

            while (!operatorStack.isEmpty()) {
                var stackToken: Char = operatorStack.peek()[0]

                if (!Utils.isOperator(stackToken)) {
                    break
                }

                var o2: Char = stackToken

                val assoc: Associativity = 
                                OperationUtils.getAssociativity(OperationUtils.getOperation(o1))
                val o1Prescedence: Int = OperationUtils.getPrecedence(OperationUtils.getOperation(o1))
                val o2Prescedence: Int = OperationUtils.getPrecedence(OperationUtils.getOperation(o2))

                if ((assoc == Associativity.LEFT && o1Prescedence <= o2Prescedence) ||
                    (assoc == Associativity.RIGHT && o1Prescedence < o2Prescedence))
                {
                    o2 = operatorStack.pop()[0]
                    outQueue.add(o2.toString())
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
                    val stackToken: Char = operatorStack.pop()[0]

                    // val ch: Char = stackToken.toChar()
                    // println("Popped token '$ch' off the stack")

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
