package org.jmock.test.acceptance;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.api.Imposteriser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.jmock.test.unit.lib.legacy.ImposteriserParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

// Fixes issue JMOCK-96
public class RedeclaredObjectMethodsAcceptanceTests {

    Mockery context = new Mockery();

    public interface MockedInterface {
        String toString();
    }

    public static class MockedClass {
        @Override
        public String toString() {
            return "not mocked";
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void testCanRedeclareObjectMethodsInMockedInterfaces(Imposteriser imposteriserImpl) {
        context.setImposteriser(imposteriserImpl);
        MockedInterface mock = context.mock(MockedInterface.class, "X");

        assertEquals("X", mock.toString());
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testCanRedeclareObjectMethodsInMockedClasses(Imposteriser imposteriserImpl) {

        context.setImposteriser(imposteriserImpl);
        MockedClass mock = context.mock(MockedClass.class, "X");

        assertEquals("X", mock.toString());
    }

    /*
     * Adapted from Jira issue JMOCK-96
     */
    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testUseMockObjectHangs1(Imposteriser imposteriserImpl) {

        context.setImposteriser(imposteriserImpl);
        final Vector<Object> mock = (Vector<Object>) context.mock(Vector.class);

        context.checking(new Expectations() {
            {
                atLeast(1).of(mock).size();
                will(returnValue(2));
            }
        });

        try {
            for (int i = 0; i < mock.size(); i++) {
                System.out.println("Vector entry " + i + " = " + mock.get(i));
            }
        } catch (ExpectationError error) {
            // expected
        }
    }
}
