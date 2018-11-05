package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.testjar.InterfaceFromOtherClassLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

public class MockingGenericsFromOtherClassLoadersAcceptanceTests {

    @Rule
    public final JUnitRuleMockery context = new ImposterisingMockery();

    // Method return types for generics become object
    @Mock
    public InterfaceFromOtherClassLoader<ABean> mock;

    @SuppressWarnings("unused")
    @Test
    public void testWhenDefinedAndInvokedThroughClass() throws Exception {

        context.checking(new Expectations() {
            {
                ABean bean = oneOf(mock).stir(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        ABean result1 = mock.stir(ABEAN);
    }

    @SuppressWarnings("unused")
    @Test
    public void testWhenDefinedThroughClassAndInvokedThroughMethod() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).stir(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ABean result1 = ((InterfaceFromOtherClassLoader<ABean>) mock).stir(ABEAN);
    }

    @Disabled
    public void DONTtestAndBoxedNativeParameterIgnoingIsADocumentationWhenDefinedThroughClassAndInvokedThroughMethod()
            throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).stir(with(any(Integer.class)));
                will(returnValue(ABEAN));
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ((InterfaceFromOtherClassLoader<ABean>) mock).stir(ABEAN);
    }

    @SuppressWarnings("unused")
    @Test
    public void testWhenDefinedAndInvokedThroughInterface() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).stir(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        ABean result1 = mock.stir(ABEAN);
    }

    private static final class ImposterisingMockery extends JUnitRuleMockery {
        private Synchroniser synchroniser;

        public ImposterisingMockery() {
            setImposteriser(ClassImposteriser.INSTANCE);
            this.synchroniser = new Synchroniser();
            setThreadingPolicy(synchroniser);
        }
    }

    public static abstract class ABean {
    }

    public ABean ABEAN = new ABean() {};
}
