package org.jmock.test.acceptance;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.ref.WeakReference;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

/**
 * Nasty test to show GitHub #36 is fixed.
 */
@Ignore("Fails in ant, passes in IntelliJ")
public class MockeryFinalizationAcceptanceTests
{
    private static final int FINALIZE_COUNT = 10; // consistently shows a problem before GitHub #36 was fixed

    private final PrintStream oldSysErr = System.err;
    private final ByteArrayOutputStream capturingErr = new ByteArrayOutputStream();

    @Before
    public void captureSysErr() {
        System.setErr(new PrintStream(capturingErr));
    }

    @After
    public void replaceSysErr() {
        System.setErr(oldSysErr);
    }

    @Test
    public void mockedInterfaceDoesntWarnOnFinalize() {
        Mockery mockery = new Mockery();

        checkNoFinalizationMessage(mockery, CharSequence.class);
    }

    @Test
    public void mockedInterfaceFromClassImposteriserDoesntWarnOnFinalize() {
        Mockery mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE);

        checkNoFinalizationMessage(mockery, CharSequence.class);
    }

    @Test
    public void mockedClassDoesntWarnOnFinalize() {
        Mockery mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE);

        checkNoFinalizationMessage(mockery, Object.class);
    }

    private void checkNoFinalizationMessage(Mockery mockery, Class<?> typeToMock) {
        WeakReference<Object> mockHolder = new WeakReference<Object>(mockery.mock(typeToMock));
        while (mockHolder.get() != null) {
            System.gc();
            System.runFinalization();
        }
        finalizeUntilMessageOrCount(FINALIZE_COUNT);
        assertThat(errorMessage(), isEmptyOrNullString());
    }


    private void finalizeUntilMessageOrCount(int count) {
        for (int i = 0; i < count && errorMessage().isEmpty(); i++) {
            System.gc();
            System.runFinalization();
        }
    }

    private String errorMessage() {
        return new String(capturingErr.toByteArray());
    }
}