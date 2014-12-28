package org.jmock.test.acceptance;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

/**
 * Nasty test to show GitHub #36 is fixed.
 */
public class MockeryFinalizationAcceptanceTests
{
    private static final int FINALIZE_COUNT = 10; // consistently shows a problem before GitHub #36 was fixed

    private final Mockery mockery = new Mockery();
    private final ErrorStream capturingErr = new ErrorStream();

    @BeforeClass
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

    @Before
    public void captureSysErr() {
        capturingErr.install();
    }

    @After
    public void replaceSysErr() {
        capturingErr.uninstall();
    }

    @Test
    public void mockedInterfaceDoesntWarnOnFinalize() {
        checkNoFinalizationMessage(mockery, CharSequence.class);
    }

    @Test
    public void mockedInterfaceFromClassImposteriserDoesntWarnOnFinalize() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);

        checkNoFinalizationMessage(mockery, CharSequence.class);
    }

    @Test
    public void mockedClassDoesntWarnOnFinalize() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);

        checkNoFinalizationMessage(mockery, Object.class);
    }

    public interface TypeThatMakesFinalizePublic {
        public void finalize();
    }

    @Ignore("TDB")
    @Test
    public void mockedTypeThatMakesFinalizePublicDoesntWarnOnFinalize() {
        checkNoFinalizationMessage(mockery, TypeThatMakesFinalizePublic.class);
    }

    @Test
    public void mockedTypeFromClassImposteriserThatMakesFinalizePublicDoesntWarnOnFinalize() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);

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