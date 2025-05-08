package io.github.maximpje;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static io.github.maximpje.TokenType.*;

public class Scanner {

    private final String source; // stores source code as a String
    private final List<Token> tokens = new ArrayList<>(); // ArrayList to dump all the tokens

    // tracking fields
    private int start = 0; // points to the first character in the scanned lexeme
    private int current = 0; // points at the currently being scanned lexeme
    private int line = 1; // points to the line containing the currently scanned lexeme

    public Scanner(String source) {
        this.source = source; // stores source code in local variable
    }

    private boolean isAtEnd() {
        return current >= source.length(); // returns a true boolean value if the code is at the end
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) { // loop through the source code and extract all tokens

            start = current;
            scanToken();

        }

        // adds EOF token to ease parsing
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
        }
    }

}
