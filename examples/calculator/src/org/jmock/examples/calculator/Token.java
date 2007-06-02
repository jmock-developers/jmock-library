/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

public class Token
{

    public static final int END = 0;
    public static final int NUMBER = 1;
    public static final int IDENTIFIER = 2;
    public static final int ADD = 3;
    public static final int SUBTRACT = 4;
    public static final int MULTIPLY = 5;
    public static final int DIVIDE = 6;
    public static final int POWER = 7;
    public static final int PAREN_OPEN = 8;
    public static final int PAREN_CLOSE = 9;

    private int type;
    private String value;

    public Token( int type, String value ) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
