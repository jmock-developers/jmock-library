/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;


public class Lexer
{
    private static final int END_OF_STREAM = -1;
    private static final char DECIMAL_PLACE = '.';

    private PushbackReader input;
    private StringBuffer tokenValue = new StringBuffer();
    private Stack pushBack = new Stack();

    public Lexer( Reader input ) {
        this.input = new PushbackReader(input);
    }

    public Lexer( String inputString ) {
        this(new StringReader(inputString));
    }

    public void pushBack( Token token ) {
        pushBack.push(token);
    }

    public Token nextToken() throws IOException, ParseException {
        if (pushBack.isEmpty()) {
            return readToken();
        } 
        return (Token)pushBack.pop();
    }

    private Token readToken() throws IOException, ParseException {
        tokenValue.setLength(0);

        skipWhitespace();

        int ch = peekChar();

        if (ch == END_OF_STREAM) {
            return new Token(Token.END, null);
        } else if (isDigit(ch)) {
            return readNumber();
        } else if (isIdentifierStart(ch)) {
            return readIdentifier();
        } else {
            return readOperator();
        }
    }

    public void skipWhitespace() throws IOException {
        int ch;

        do {
            ch = readChar();
        }
        while (ch != END_OF_STREAM && Character.isWhitespace((char)ch));

        unreadChar(ch);
    }

    private boolean isDigit( int ch ) {
        return ch != END_OF_STREAM && Character.isDigit((char)ch);
    }

    private Token readNumber() throws IOException {
        collectDigits();
        if (peekChar() == DECIMAL_PLACE) {
            tokenValue.append((char)readChar());
            collectDigits();
        }

        return createToken(Token.NUMBER);
    }

    private void collectDigits() throws IOException {
        int ch;

        while (isDigit(ch = readChar())) {
            tokenValue.append((char)ch);
        }

        unreadChar(ch);
    }

    private boolean isIdentifierStart( int ch ) {
        return Character.isUnicodeIdentifierStart((char)ch);
    }

    private boolean isIdentifierPart( int ch ) {
        return ch != -1 && Character.isUnicodeIdentifierPart((char)ch);
    }

    private Token readIdentifier() throws IOException {
        tokenValue.append((char)readChar());

        int ch;
        while (isIdentifierPart((ch = readChar()))) {
            tokenValue.append((char)ch);
        }
        unreadChar(ch);

        return createToken(Token.IDENTIFIER);
    }

    private Token readOperator() throws IOException, ParseException {
        int ch = readChar();
        tokenValue.append((char)ch);

        switch (ch) {
        case '+':
            return createToken(Token.ADD);
        case '-':
            return createToken(Token.SUBTRACT);
        case '*':
            return createToken(Token.MULTIPLY);
        case '/':
            return createToken(Token.DIVIDE);
        case '^':
            return createToken(Token.POWER);
        case '(':
            return createToken(Token.PAREN_OPEN);
        case ')':
            return createToken(Token.PAREN_CLOSE);

        default:
            throw new ParseException("unrecognised character '" + (char)ch + "'");
        }
    }


    private Token createToken( int tokenType ) {
        return new Token(tokenType, tokenValue.toString());
    }

    private int peekChar() throws IOException {
        int ch = readChar();
        unreadChar(ch);
        return ch;
    }

    private int readChar() throws IOException {
        return input.read();
    }

    private void unreadChar( int ch ) throws IOException {
        if (ch != END_OF_STREAM) input.unread(ch);
    }
}
