/*
 * Created on Dec 17, 2003
 */
package atest.jmock;

/**
 * Used as namespace for example types used in integration tests
 */
public interface Types
{
    public interface WithTwoMethods
    {
        int twoArgsReturnInt( String arg1, Object arg2 );

        void noArgsReturnsNothing();
    }
}
