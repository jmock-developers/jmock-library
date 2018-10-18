package org.jmock.test.acceptance;

import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class FinalizerIsIgnoredAcceptanceTests {

    public static class ClassWithFinalizer {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    Mockery mockery = new Mockery();

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testIgnoresFinalizerInMockedClasses(Imposteriser imposteriserImpl) throws Throwable {
        mockery.setImposteriser(imposteriserImpl);
        ClassWithFinalizer mock = mockery.mock(ClassWithFinalizer.class, "mock");
        mock.finalize();
    }
}
