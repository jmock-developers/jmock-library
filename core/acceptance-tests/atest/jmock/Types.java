/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

/**
 * Used as namespace for example types used in integration tests
 */
public interface Types
{
    public interface WithTwoMethods
    {
        int twoArgsReturnsInt( String arg1, Object arg2 );

        void noArgsReturnsNothing();
    }
}
