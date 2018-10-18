package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class MockingImplementationOfGenericTypeAcceptanceTests {
    private Mockery context = new JUnit4Mockery();

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testWhenDefinedAndInvokedThroughClass(Imposteriser imposteriserImpl) throws Exception {
        context.setImposteriser(imposteriserImpl);
        final AnImplementation mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith("a");
            }
        });

        mock.doSomethingWith("a");
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testWhenDefinedThroughClassAndInvokedThroughMethod(Imposteriser imposteriserImpl) throws Exception {
        context.setImposteriser(imposteriserImpl);
        final AnImplementation mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith("a");
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ((AnInterface<String>) mock).doSomethingWith("a");
    }

    @Disabled
    public void DONTtestAndBoxedNativeParameterIgnoingIsADocumentationWhenDefinedThroughClassAndInvokedThroughMethod()
            throws Exception {
        final AnImplementation mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith(with(any(Integer.class)));
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ((AnInterface<String>) mock).doSomethingWith("a");
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testWhenDefinedAndInvokedThroughInterface(Imposteriser imposteriserImpl) throws Exception {
        context.setImposteriser(imposteriserImpl);
        final AnInterface<String> mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith("a");
            }
        });

        mock.doSomethingWith("a");
    }

    public interface AnInterface<T> {
        void doSomethingWith(T arg);

        void doSomethingWith(int arg);
    }

    public static class AnImplementation implements AnInterface<String> {
        public void doSomethingWith(String arg) {
        }

        public void doSomethingWith(int arg) {
        }
    }
}
