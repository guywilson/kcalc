package com.guy.calc

class Tokeniser constructor(expression: String) {
    private val expr: String = expression
    private val exprLen: Int = expression.length

    private var startIndex: Int = 0
    private var endIndex: Int = 0

    private fun findNextTokenPos() : Int {
        var i: Int = 0

        for (i in startIndex..exprLen) {

        }

        return (i + 1)
    }
}
