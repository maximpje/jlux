package io.github.maximpje;

public class Token {
    final TokenType type; // stores token as enum
    final String lexeme; // token as a String literal
    final Object literal; //
    final int line; // line where the token is located

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() { // converts token to string
        return type + " " + lexeme + " " + literal;
    }
}
