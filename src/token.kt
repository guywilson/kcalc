package com.guy.calc

abstract class Token constructor(token: Char) {
    val value: Char = token
    
    public abstract fun toChar() : Char
}
