/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import junit.framework.TestCase;

import org.jmock.Constraint;
import org.jmock.constraint.IsSame;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.ArgumentsMatcher;

import test.jmock.dynamic.testsupport.AlwaysFalse;
import test.jmock.dynamic.testsupport.AlwaysTrue;


public class ArgumentsMatcherTest extends TestCase {
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
                        new Constraint[]{AlwaysTrue.INSTANCE, AlwaysTrue.INSTANCE, AlwaysTrue.INSTANCE});

        assertFalse("Too many arguments", matcher.matches(exampleInvocation));
    }

    public void testNoMatchWhenAnyArgumentDoNotConform() throws Throwable {
        ArgumentsMatcher matcher =
                new ArgumentsMatcher(
                        new Constraint[]{AlwaysTrue.INSTANCE, same("wrong")});

        assertFalse("Incorrect argument", matcher.matches(exampleInvocation));
    }

    public void testArgumentsMatchWhenAllValuesMatch() throws Throwable {
        ArgumentsMatcher matcher =
                new ArgumentsMatcher(
                        new Constraint[]{AlwaysTrue.INSTANCE, same(exampleArg2)});

        assertTrue("Arguments match", matcher.matches(exampleInvocation));
    }
    
    public void testEncapsulatesArrayOfConstraints() {
        Constraint[] constraintArray = { same(exampleArg1), same(exampleArg2) };
        
        ArgumentsMatcher matcher = new ArgumentsMatcher( constraintArray );
        
        constraintArray[0] = AlwaysFalse.INSTANCE;
        assertTrue( "arguments should match", matcher.matches(exampleInvocation) );
        
        matcher.getConstraints()[0] = AlwaysFalse.INSTANCE;
        assertTrue( "arguments should match", matcher.matches(exampleInvocation) );
    }
    
    private Constraint same( Object arg ) { 
        return new IsSame(arg);
    }
}
