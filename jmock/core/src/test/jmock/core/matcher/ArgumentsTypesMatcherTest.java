/*  Copyright (c) 2004 jMock.org
 */
package test.jmock.core.matcher;

import org.jmock.core.Invocation;
import org.jmock.core.matcher.ArgumentTypesMatcher;

import junit.framework.TestCase;

public class ArgumentsTypesMatcherTest extends TestCase {
	private interface TargetType {
        void twoArguments(String one, String two);
        void withPrimitive(int i);
        void noArguments();
    }
    
    public void tesTrueIfMatchesMultipleArguments() throws SecurityException, NoSuchMethodException {
        ArgumentTypesMatcher matcher = 
            new ArgumentTypesMatcher(new Class[] {String.class, String.class});
        
    	assertTrue("multiple args", 
                matcher.matches(
                        invocation("twoArguments", new Class[] {String.class, String.class})));
    }
    public void testTrueIfMatchesPrinitiveType() throws SecurityException, NoSuchMethodException {
        ArgumentTypesMatcher matcher = new ArgumentTypesMatcher(new Class[] {int.class});
        
        assertTrue("primitive arg", 
                matcher.matches(
                        invocation("withPrimitive", new Class[] {int.class})));
    }
    public void testTrueIfMatchesVoidMethod() throws SecurityException, NoSuchMethodException {
        ArgumentTypesMatcher matcher = new ArgumentTypesMatcher(new Class[0]);
        
        assertTrue("no arguments", 
                matcher.matches(
                        invocation("noArguments", new Class[0])));
    }
    public void testFalseIfDoesNotMatch() throws SecurityException, NoSuchMethodException {
        ArgumentTypesMatcher matcher = new ArgumentTypesMatcher(new Class[] {String.class});
        
        assertFalse("not primitive arg", 
                matcher.matches(
                        invocation("withPrimitive", new Class[] {int.class})));        
    }
    public void testPrimitivesDoNotMatchBoxedTypes() throws SecurityException, NoSuchMethodException {
        ArgumentTypesMatcher matcher = new ArgumentTypesMatcher(new Class[] {Integer.class});
        
        assertFalse("not primitive arg", 
                matcher.matches(
                        invocation("withPrimitive", new Class[] {int.class})));                
    }
    
    private Invocation invocation(String methodName, Class[] types) throws SecurityException, NoSuchMethodException {
        return new Invocation("Invoked object",
                TargetType.class.getMethod(methodName, types),
                new Object[0]);
	}
}
