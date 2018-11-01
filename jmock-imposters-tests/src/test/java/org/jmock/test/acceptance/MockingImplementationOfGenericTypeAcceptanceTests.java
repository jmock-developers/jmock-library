package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

public class MockingImplementationOfGenericTypeAcceptanceTests {

    @Rule
    public final JUnitRuleMockery context = new ImposterisingMockery();

    @Mock
    public AnInterface<ABean> mock;

    @Test
    public void testWhenDefinedAndInvokedThroughClass() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        ABean result1 = mock.doSomethingWith(ABEAN);
        ABean result2 = mock.doSomethingWith(ABEAN);
    }

    @Test
    public void testWhenDefinedThroughClassAndInvokedThroughMethod() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ABean result1 = ((AnInterface<ABean>) mock).doSomethingWith(ABEAN);
        ABean result2 = ((AnInterface<ABean>) mock).doSomethingWith(ABEAN);
    }

    @Disabled
    public void DONTtestAndBoxedNativeParameterIgnoingIsADocumentationWhenDefinedThroughClassAndInvokedThroughMethod()
            throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith(with(any(Integer.class)));
                will(returnValue(ABEAN));
            }
        });

        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ((AnInterface<ABean>) mock).doSomethingWith(ABEAN);
    }

    @Test
    public void testWhenDefinedAndInvokedThroughInterface() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(mock).doSomethingWith(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        ABean result1 = mock.doSomethingWith(ABEAN);
        ABean result2 = mock.doSomethingWith(ABEAN);
    }

    private static final class ImposterisingMockery extends JUnitRuleMockery {
        private Synchroniser synchroniser;

        public ImposterisingMockery() {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
            this.synchroniser = new Synchroniser();
            setThreadingPolicy(synchroniser);
        }
    }

    public interface AnInterface<T> {
        T doSomethingWith(T arg);

        void doSomethingWith(int arg);
    }

    public static abstract class ABean {
    }

    public ABean ABEAN = new ABean() {};

    public static class AnImplementation implements AnInterface<ABean> {
        @Override
        public ABean doSomethingWith(ABean arg) {
            return new ABean() {};
        }

        @Override
        public void doSomethingWith(int arg) {}
    }
}
