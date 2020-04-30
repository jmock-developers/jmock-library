package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.jmock.testdata.scalaexample.BadlyAnnotated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.Assert.assertSame;

public class BadAnnotationsAcceptanceTests {
    Mockery context = new Mockery();

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void shouldMockScalaCaseClassWithFieldAnnotationOnParameter(Imposteriser imposteriser) {
        context.setImposteriser(imposteriser);

        final BadlyAnnotated mock = context.mock(BadlyAnnotated.class);
        final String result = "a mock result";
        context.checking(new Expectations() {
            {
                oneOf(mock).badlyAnnotated();
                will(returnValue(result));
            }
        });

        assertSame(mock.badlyAnnotated(), result);
    }
}
