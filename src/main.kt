package com.guy.calc

import kotlin.io.*

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

fun evaluate(expression: String) : Operand {
    
}