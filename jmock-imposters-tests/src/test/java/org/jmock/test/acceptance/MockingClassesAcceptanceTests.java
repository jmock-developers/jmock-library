package org.jmock.test.acceptance;

import static org.junit.Assert.assertSame;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.jmock.test.unit.lib.legacy.ImposteriserParameterResolver;
import org.jmock.testjar.InterfaceFromOtherClassLoader;
import org.junit.Assert;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class MockingClassesAcceptanceTests {

    public static final class FinalClass {
    }

    public static class ClassToMock {
        public FinalClass returnInstanceOfFinalClass() {
            return null;
        }
    }

    Mockery context = new Mockery();

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void testCanMockClassesWithMethodsThatReturnFinalClasses(Imposteriser imposteriserImpl) {
        context.setImposteriser(imposteriserImpl);

        final ClassToMock mock = context.mock(ClassToMock.class);
        final FinalClass result = new FinalClass();

        context.checking(new Expectations() {
            {
                oneOf(mock).returnInstanceOfFinalClass();
                will(returnValue(result));
            }
        });

        // This should not crash

        assertSame(result, mock.returnInstanceOfFinalClass());
    }
    
    @SuppressWarnings("rawtypes")
    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void testMockClassIsCached(Imposteriser imposteriserImpl) {
        Class class1 = context.mock(InterfaceFromOtherClassLoader.class,"1").getClass();
        Class class2 = context.mock(InterfaceFromOtherClassLoader.class,"2").getClass();
        Assert.assertEquals("Type should be cached", class1, class2);
    }
}
