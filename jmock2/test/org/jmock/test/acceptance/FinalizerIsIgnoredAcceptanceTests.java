package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

public class FinalizerIsIgnoredAcceptanceTests extends TestCase {
    public static class ClassWithFinalizer {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
    
    Mockery mockery = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    ClassWithFinalizer mock = mockery.mock(ClassWithFinalizer.class, "mock");
    
    public void testIgnoresFinalizerInMockedClasses() throws Throwable {
        mock.finalize();
    }
}
