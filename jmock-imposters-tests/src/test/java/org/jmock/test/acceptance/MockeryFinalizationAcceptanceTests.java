package org.jmock.test.acceptance;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

import org.jmock.Mockery;
import org.jmock.api.Imposteriser;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.test.unit.lib.legacy.CodeGeneratingImposteriserParameterResolver;
import org.jmock.test.unit.lib.legacy.ImposteriserParameterResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
/**
 * Nasty test to show GitHub #36 is fixed.
 */
public class MockeryFinalizationAcceptanceTests
{
    private static final int FINALIZE_COUNT = 10; // consistently shows a problem before GitHub #36 was fixed

    private final Mockery mockery = new Mockery() {{
        setThreadingPolicy(new Synchroniser());
    }};
    private final ErrorStream capturingErr = new ErrorStream();

    @BeforeAll
    public static void clearAnyOutstandingMessages() {
        ErrorStream localErr = new ErrorStream();
        localErr.install();
        String error = null;
        try {
            finalizeUntilMessageOrCount(localErr, FINALIZE_COUNT);
            error = localErr.output();
        } finally {
            localErr.uninstall();
        }
        if (error != null)
            System.err.println("WARNING - a previous test left output in finalization [" + error + "]");
    }

    @BeforeEach
    public void captureSysErr() {
        capturingErr.install();
    }

    @AfterEach
    public void replaceSysErr() {
        capturingErr.uninstall();
    }

    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void mockedInterfaceDoesntWarnOnFinalize(Imposteriser imposterImpl) {
        mockery.setImposteriser(imposterImpl);
        
        checkNoFinalizationMessage(mockery, CharSequence.class);
    }

    @ParameterizedTest
    @ArgumentsSource(CodeGeneratingImposteriserParameterResolver.class)
    public void mockedClassDoesntWarnOnFinalize(Imposteriser imposterImpl) {
        mockery.setImposteriser(imposterImpl);

        checkNoFinalizationMessage(mockery, Object.class);
    }

    public interface TypeThatMakesFinalizePublic {
        public void finalize();
    }

    @ParameterizedTest
    @ArgumentsSource(ImposteriserParameterResolver.class)
    public void mockedTypeThatMakesFinalizePublicDoesntWarnOnFinalize(Imposteriser imposterImpl) {
        mockery.setImposteriser(imposterImpl);

        checkNoFinalizationMessage(mockery, TypeThatMakesFinalizePublic.class);
    }

    private void checkNoFinalizationMessage(Mockery mockery, Class<?> typeToMock) {
        WeakReference<Object> mockHolder = new WeakReference<Object>(mockery.mock(typeToMock));
        while (mockHolder.get() != null) {
            System.gc();
            System.runFinalization();
        }
        finalizeUntilMessageOrCount(capturingErr, FINALIZE_COUNT);
        assertThat(capturingErr.output(), isEmptyOrNullString());
    }


    private static void finalizeUntilMessageOrCount(ErrorStream capturingErr, int count) {
        for (int i = 0; i < count && capturingErr.output().isEmpty(); i++) {
            System.gc();
            System.runFinalization();
        }
    }

    private static class ErrorStream extends PrintStream {

        private PrintStream oldSysErr;

        public ErrorStream() {
            super(new ByteArrayOutputStream());
        }

        public void install() {
            oldSysErr = System.err;
            System.setErr(this);
        }

        public void uninstall() {
            System.setErr(oldSysErr);
        }

        public String output() {
            return new String(((ByteArrayOutputStream) out).toByteArray());
        }
    }
}