package com.guy.calc

fun getBraceType(ch: Char) : BraceType {
    if (ch in "({[") {
        return BraceType.OPEN
    }
    else if (ch in ")}]") {
        return BraceType.CLOSE
    }
    else {
        return BraceType.UNKNOWN
    }
}

enum class BraceType {
    OPEN,
    CLOSE,
    UNKNOWN
}

open class Brace constructor(token: Char) : Token(token) {
    companion object StaticMembers {
        public var toChar: (BraceType) -> Char = {
            braceType: BraceType -> 
                when (braceType) {
                    BraceType.OPEN      -> '('
                    BraceType.CLOSE     -> ')'
                    else                -> '0'
                }
        }
    }

    val braceType: BraceType = getBraceType(token)

    public override fun toChar() : Char {
        return Brace.toChar(this.braceType)
    }
}
