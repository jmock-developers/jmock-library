/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package atest.jmock.easy;

import java.io.IOException;

public interface IMethods {

    boolean booleanReturningMethod(int index);
    byte byteReturningMethod(int index);
    short shortReturningMethod(int index);
    char charReturningMethod(int index);
    int intReturningMethod(int index);
    long longReturningMethod(int index);
    float floatReturningMethod(int index);
    double doubleReturningMethod(int index);
    Object objectReturningMethod(int index);

    String oneArgumentMethod(boolean value);
    String oneArgumentMethod(byte value);
    String oneArgumentMethod(short value);
    String oneArgumentMethod(char value);
    String oneArgumentMethod(int value);
    String oneArgumentMethod(long value);
    String oneArgumentMethod(float value);
    String oneArgumentMethod(double value);
    String oneArgumentMethod(Object value);

    public String throwsNothing(boolean value);
    public String throwsIOException(int count) throws IOException;
    public String throwsError(int count) throws Error;

    void simpleMethod();
    void simpleMethodWithArgument(String argument);

    Object threeArgumentMethod(
        int valueOne,
        Object valueTwo,
        String valueThree);
    void twoArgumentMethod(int one, int two);

    void arrayMethod(String[] strings);
}
