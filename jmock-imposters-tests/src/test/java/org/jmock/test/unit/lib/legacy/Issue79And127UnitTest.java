package org.jmock.test.unit.lib.legacy;

import java.io.File;

import javax.sql.DataSource;

import org.jmock.api.Imposteriser;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.junit5.JUnit5Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.test.acceptance.AbstractImposteriserParameterResolver;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * This used to fail in Java 8 as cglib-nodep used asm:5.0.3 which required
 * jdk<=7 Now we use cglib (not cglib-nodep) and override asm to 5.0.4
 * 
 * @see https://github.com/jmock-developers/jmock-library/issues/79
 * @see https://github.com/cglib/cglib/issues/20
 * 
 */
public class Issue79And127UnitTest {
    
    @RegisterExtension
    public final JUnit5Mockery context = new JUnit5Mockery();

    /**
     * However it fails in java 11 as asm and cglib do not appear to support java
     * 11 yet. Migrate all code to ByteBuddyClassImposteriser to solve this issue.
     * 
     * java.lang.UnsupportedOperationException at
     * org.objectweb.asm.ClassVisitor.visitNestMemberExperimental(ClassVisitor.java:248)
     * 
     * @param imposteriserImpl
     */
    @DisabledOnJre({ JRE.JAVA_11 })
    @ParameterizedTest
    @ArgumentsSource(ClassImposteriserParameterResolver.class)
    public void testMock(Imposteriser imposteriserImpl) {
        context.setImposteriser(imposteriserImpl);
        context.mock(File.class);
    }

    @ParameterizedTest
    @ArgumentsSource(ByteBuddyImposteriserParameterResolver.class)
    public void testByteBuddyImposteriser(Imposteriser imposteriserImpl) {
        context.setImposteriser(imposteriserImpl);
        context.mock(File.class);
        context.mock(DataSource.class);
    }

    public static class ByteBuddyImposteriserParameterResolver extends AbstractImposteriserParameterResolver {
        public ByteBuddyImposteriserParameterResolver() {
            super(ByteBuddyClassImposteriser.INSTANCE);
        }
    }

    public static class ClassImposteriserParameterResolver extends AbstractImposteriserParameterResolver {
        public ClassImposteriserParameterResolver() {
            super(ClassImposteriser.INSTANCE);
        }
    }
}
