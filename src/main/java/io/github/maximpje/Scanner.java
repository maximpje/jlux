package io.github.maximpje;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static io.github.maximpje.TokenType.*;

public class Scanner {

    private static final Map<String, TokenType> keywords;

    // hashmap of reserved keywords
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    private final String source; // stores source code as a String
    private final List<Token> tokens = new ArrayList<>(); // ArrayList to dump all the tokens

    // tracking fields
    private int start = 0; // points to the first character in the scanned lexeme
    private int current = 0; // points at the currently being scanned lexeme
    private int line = 1; // points to the line containing the currently scanned lexeme

    public Scanner(String source) {
        this.source = source; // stores source code in local variable
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

            // single char tokens
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

            //
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // SLASH is special need as comments start with a slash too
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance(); // ignores comment
                } else {
                    addToken(SLASH);
                }
                break;


            // ignore the weird kids
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            case '"': string(); break; // calls method to extract the string and turn it into a token

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Main.error(line, "Unexpected character.");
                }
                break;
        }
    }


    // helper methods

    private void identifier() {
        while (isAlphaNumeric(peek())) advance(); // looks if next number is alphanumeric if it is advance
        addToken(IDENTIFIER);

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();
        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            do advance();
            while (isDigit(peek()));
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    // return true if char is a digit.
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string(){
        // loops through the string until '='
        while(peek() != '=' && !isAtEnd()) {
            if(peek() == '\n') line++;
        }

        // throw an error if it does not find a '='
        if(isAtEnd()){
            Main.error(line, "Unterminated string. ");
        }

        advance();

        //trim quotes
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);

    }



    private char peek() { // peeks next character
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() { // peeks 2 characters ahead
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    // returns true if character is either a letter or an underscore
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    // returns true if character is either a letter, number or an underscore
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean match(char expected) { // returns true if next char is =, useful for tokens where the sign changes the token
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private boolean isAtEnd() {
        return current >= source.length(); // returns a true boolean value if the code is at the end
    }

    private char advance() { // returns the next character
        current++;
        return source.charAt(current - 1);
    }
    private void addToken(TokenType type) { // small wrapper for Token
        addToken(type, null);
    }
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
