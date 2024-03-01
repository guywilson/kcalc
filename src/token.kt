package com.guy.calc

abstract class Token constructor(token: Char) {
    val value: Char = token

    constructor() : this('0') {
    }

    public abstract fun toChar() : Char
}
