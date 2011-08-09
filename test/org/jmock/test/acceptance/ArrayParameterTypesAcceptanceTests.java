package org.jmock.test.acceptance;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;

public class ArrayParameterTypesAcceptanceTests extends TestCase {
    public interface MethodsWithPrimitiveTypes {
        void withPrimitiveArray(boolean[] boolAr, byte[] byteAr, short[] shortAr, int[] intAr, long[] longAr, float[] floatAr, double[] doubleAr);
        void withObjectArray(Object objectAr[]);
    }
    
    Mockery context = new Mockery();
    MethodsWithPrimitiveTypes mock = context.mock(MethodsWithPrimitiveTypes.class, "mock");

    private final boolean[] boolAr = new boolean[]{true, false};
    private final byte[] byteAr = new byte[]{42, 43};
    private final short[] shortAr = new short[]{256, 300};
    private final int[] intAr = new int[]{65598, 7878787};
    private final long[] longAr = new long[]{Long.MAX_VALUE, Long.MIN_VALUE};
    private final float[] floatAr = new float[]{1.0f, 2.0f};
    private final double[] doubleAr = new double[]{3.0d, 4.0d};
    private final Object[] objectAr = new Object[]{new Object(), new Object()};
    
    public void testCanSetExpectationsWithMatchersForMethodsWithArgumentsOfPrimitiveTypes() {
        context.checking(new Expectations() {protected void expect() throws Exception {
            exactly(1).of (mock).withPrimitiveArray(
                    with(equal(boolAr)),
                    with(equal(byteAr)),
                    with(equal(shortAr)),
                    with(equal(intAr)),
                    with(equal(longAr)),
                    with(equal(floatAr)),
                    with(equal(doubleAr)));
            exactly(1).of(mock).withObjectArray(with(equal(objectAr)));
        }});
        
        mock.withPrimitiveArray(boolAr, byteAr, shortAr, intAr, longAr, floatAr, doubleAr);
        mock.withObjectArray(objectAr);
        
        context.assertIsSatisfied();
    }

    public void testCanSetExpectationsWithLiteralsForMethodsWithArgumentsOfPrimitiveTypes() {
        context.checking(new Expectations() {protected void expect() throws Exception {
            exactly(1).of (mock).withPrimitiveArray(boolAr, byteAr, shortAr, intAr, longAr, floatAr, doubleAr);
            exactly(1).of(mock).withObjectArray(with(equal(objectAr)));
        }});

        mock.withPrimitiveArray(boolAr, byteAr, shortAr, intAr, longAr, floatAr, doubleAr);
        mock.withObjectArray(objectAr);
        
        context.assertIsSatisfied();
    }
}
