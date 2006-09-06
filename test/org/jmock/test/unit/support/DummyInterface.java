/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.support;

public interface DummyInterface
{
    void noArgVoidMethod() throws Throwable;

    String noArgMethod();

    String oneArgMethod( String arg1 );

    String twoArgMethod( String arg1, String arg2 ) throws Throwable;

    final String METHOD_NOARGVOID_NAME = "noArgVoidMethod";
    final Object[] METHOD_NOARGVOID_ARGS = new Object[0];

    final String METHOD_NOARG_NAME = "noArgMethod";
    final Object[] METHOD_NOARG_ARGS = new Object[0];
    final String METHOD_NOARG_RESULT = "resultNoArgs";

    final String METHOD_ONEARG_NAME = "oneArgMethod";
    final String[] METHOD_ONEARG_ARGS = new String[]{"oneP1"};
    final String METHOD_ONEARG_RESULT = "result1Args";

    final String METHOD_TWOARG_NAME = "twoArgMethod";
    final String[] METHOD_TWOARG_ARGS = new String[]{"twoP1", "twoP2"};
    final String METHOD_TWOARG_RESULT = "resultTwoArgs";

}
