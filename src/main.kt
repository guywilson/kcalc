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

val cal:Calendar = Calendar.getInstance()
val year: Int = cal.get(Calendar.YEAR)

val banner: String = """
Welcome to Calc. A cmd line scientific calculator. Copyright © Guy Wilson $year
Type a calculation or command at the prompt, type 'help' for info."""

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
    println(banner)
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
    println("\trad(degrees)\treturn the value of degrees in radians")
    println("\tdeg(radians)\treturn the value of radians in degrees")
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
    println("\tmemstn\tStore the last result in memory location n")
    println("\tmemclrn\tClear the memory location n")
    println("\tlistmem\tList all memory locations")
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
    var result: String = "0.0"

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
        else if (calculation.startsWith("test", false)) {
            runTests()
            go = false
        }
        else if (calculation.startsWith("cls") || calculation.startsWith("clear")) {
            terminal.puts(Capability.clear_screen);

            reader.printAbove(banner);
            reader.printAbove("\n");
        }
        else if (calculation.startsWith("dec", false)) {
            result = BigDecimal(BigInteger(result, base.radix)).setScale(Utils.scale, RoundingMode.HALF_UP).toPlainString()
            println("result = $result")

            base = Base.DECIMAL
        }
        else if (calculation.startsWith("hex", false)) {
            result = result.substring(0, result.indexOf('.'))
            result = BigInteger(result, base.radix).toString(Base.HEXADECIMAL.radix).uppercase()
            println("result = $result")

            base = Base.HEXADECIMAL
        }
        else if (calculation.startsWith("oct", false)) {
            result = result.substring(0, result.indexOf('.'))
            result = BigInteger(result, base.radix).toString(Base.OCTAL.radix).uppercase()
            println("result = $result")

            base = Base.OCTAL
        }
        else if (calculation.startsWith("bin", false)) {
            result = result.substring(0, result.indexOf('.'))
            result = BigInteger(result, base.radix).toString(Base.BINARY.radix).uppercase()
            println("result = $result")

            base = Base.BINARY
        }
        else if (calculation.startsWith("setp", false)) {
            var s: Int? = calculation.substring(4).toIntOrNull()

            if (s != null) {
                if (s <= Utils.MAX_PRECISION) {
                    Utils.scale = s
                }
                else {
                    println("Precision must be between 1 and 80")
                    println()
                }
            }
        }
        else if (calculation.startsWith("memst", false)) {
            var m: Int? = calculation.substring(5).toIntOrNull()

            if (m != null) {
                if (m < Utils.MEMORY_SLOTS) {
                    Utils.memoryStore(result, m)
                }
            }
        }
        else if (calculation.startsWith("memclr", false)) {
            var m: Int? = calculation.substring(6).toIntOrNull()

            if (m != null) {
                if (m < Utils.MEMORY_SLOTS) {
                    Utils.memoryClear(m)
                }
            }
        }
        else if (calculation.startsWith("listmem", false)) {
            for (i in 0..Utils.MEMORY_SLOTS - 1) {
                val m: String = Utils.memoryRetrieve(i)

                println("\tmem $i -> $m")
            }
        }
        else {
            if (calculation.length > 0) {
                result = evaluate(calculation, base)

                println("$calculation = $result")
            }
        }
    }
}

fun testEvaluate(calculation: String, base: Base, expected: String) : Boolean {
    var result: String = evaluate(calculation, base)

    result = BigDecimal(result).setScale(2, RoundingMode.HALF_UP).toPlainString()

    if (result.compareTo(expected) == 0) {
        println("**** Success :) - '$calculation': Expected: '$expected', got: '$result'")
        return true
    }
    else {
        println("**** Failed :( - '$calculation': Expected: '$expected', got: '$result'")
        return false
    }
}

fun runTests() {
    var numTestsPassed: Int = 0
    var numTestsFailed: Int = 0
    var totalTestsRun: Int = 0

    val testMap = mapOf(
                        "2 + (3 * 4) ^ 2 - 13" to "133.00",
                        "12 - ((2 * 3) - (8 / 2) / 0.5) / 12.653" to "12.16",
                        "2 ^ 16 - 1" to "65535.00",
                        "(((((((1 + 2 * 3)-2)*4)/2)-12)+261)/12) - 5.25" to "16.33",
                        "pi + sin(45 + 45)" to "4.14",
                        "pi * (2 ^ 2)" to "12.57",
                        "3 + 4/(2 * 3 * 4) - 4/(4 * 5 * 6) + 4/(6 * 7 * 8) - 4/(8 * 9 * 10) + 4/(10 * 11 * 12)" to "3.14",
                        "84 * -15 + sin(47)" to "-1259.27",
                        "16 / (3 - 5 + 8) * (3 + 5 - 4)" to "10.67",
                        "sin(90) * cos(45) * tan(180) + asin(1) + acos(0) + atan(25)" to "267.71",
                        "asin(sin(90)) + acos(cos(90))" to "180.00")
    
    Utils.scale = 16

    for (t in testMap) {
        if (testEvaluate(t.key, Base.DECIMAL, t.value)) {
            numTestsPassed++
        }
        else {
            numTestsFailed++
        }

        totalTestsRun++
    }

    println("Test results: $numTestsFailed failed, $numTestsPassed passed out of $totalTestsRun total")
    println()
}
