/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import org.jmock.C;
import org.jmock.Constraint;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.ArgumentsMatcher;

import test.jmock.AbstractTestCase;


public class ArgumentsMatcherTest extends AbstractTestCase {
	private Invocation emptyInvocation =
            new Invocation(Void.class, "example", new Class[0], Void.class, new Object[0]);

    private final String exampleArg1 = "arg1";
    private final String exampleArg2 = "arg2";
    private Invocation exampleInvocation =
            new Invocation(Void.class, "example", new Class[]{String.class}, Void.class,
                    new Object[]{exampleArg1, exampleArg2});

    public void testMatchWhenNoArgumentsOrConstraints() throws Throwable {
        ArgumentsMatcher matcher = new ArgumentsMatcher(new Constraint[0]);

        assertTrue("No arguments", matcher.matches(emptyInvocation));
    }

    public void testNoMatchWhenTooManyArguments() throws Throwable {
        ArgumentsMatcher matcher = new ArgumentsMatcher(new Constraint[0]);

        assertFalse("Too many arguments", matcher.matches(exampleInvocation));
    }

    public void testNoMatchWhenTooFewArguments() throws Throwable {
        ArgumentsMatcher matcher =
                new ArgumentsMatcher(
                        new Constraint[]{C.IS_ANYTHING, C.IS_ANYTHING, C.IS_ANYTHING});

        assertFalse("Too many arguments", matcher.matches(exampleInvocation));
    }

    public void testNoMatchWhenAnyArgumentDoNotConform() throws Throwable {
        ArgumentsMatcher matcher =
                new ArgumentsMatcher(
                        new Constraint[]{C.IS_ANYTHING, C.eq("wrong")});

        assertFalse("Incorrect argument", matcher.matches(exampleInvocation));
    }

    public void testArgumentsMatchWhenAllValuesMatch() throws Throwable {
        ArgumentsMatcher matcher =
                new ArgumentsMatcher(
                        new Constraint[]{C.IS_ANYTHING, C.eq(exampleArg2)});

        assertTrue("Arguments match", matcher.matches(exampleInvocation));
    }
    
    public void testEncapsulatesArrayOfConstraints() {
        Constraint[] constraintArray = { C.eq(exampleArg1), C.eq(exampleArg2) };
        
        ArgumentsMatcher matcher = new ArgumentsMatcher( constraintArray );
        
        constraintArray[0] = C.IS_FALSE;
        assertTrue( "arguments should match", matcher.matches(exampleInvocation) );
        
        matcher.getConstraints()[0] = C.IS_FALSE;
        assertTrue( "arguments should match", matcher.matches(exampleInvocation) );
    }
}
