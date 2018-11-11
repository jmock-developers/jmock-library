package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.testjar.InterfaceFromOtherClassLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

/**
 * This tests the solution to a longstanding jMock issue. Mock return values for
 * generic interfaces loaded by other class loaders had the type constraint of
 * the source interface not the runtime type. The ByteBuddyClassImposteriser
 * oxposed this issue as it cast mocked method return values to their target
 * type. This caused ClassCastExceptions. Returning null in the cases where
 * we're building expectations is the simple solution. Once will(returnValue())
 * has bee called then the actual mock reply is known and can be returned at
 * expectation assertion time.
 * 
 * @author oliverbye
 *
 */
public class MockingGenericsFromOtherClassLoadersAcceptanceTests {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery() {
        {
            setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
        }
    };

    /**
     * This test case requires a generic mock.
     */
    @Mock
    public InterfaceFromOtherClassLoader<ABean> mock;

    @SuppressWarnings("unused")
    @Test
    public void testWhenDefinedAndInvokedThroughClass() throws Exception {

        context.checking(new Expectations() {
            {
                // Assigning the mock is not normal
                // We do this to prove that the result is assignable to the generic interfaces
                // runtime type (ABean)
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
                ABean bean = oneOf(mock).stir(ABEAN);
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
                ABean bean = oneOf(mock).stir(ABEAN);
                will(returnValue(ABEAN));
            }
        });

        ABean result1 = mock.stir(ABEAN);
    }

    public static abstract class ABean {
    }

    public ABean ABEAN = new ABean() {
    };
}
