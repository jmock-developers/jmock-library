package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class PrimitiveParameterTypesAcceptanceTests {
    public interface MethodsWithPrimitiveTypes {
        void withBoolean(boolean b);
        void withByte(byte b);
        void withShort(short s);
        void withInt(int i);
        void withLong(long l);
        void withFloat(float f);
        void withDouble(double d);
    }
    
    @Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
    private final MethodsWithPrimitiveTypes mock = context.mock(MethodsWithPrimitiveTypes.class, "mock");

    @Test
    public void canSetExpectationsWithMatchersForMethodsWithArgumentsOfPrimitiveTypes() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).withBoolean(with.booleanIs(equal(true)));
            exactly(1).of (mock).withByte(with.byteIs(equal((byte)10)));
            exactly(1).of (mock).withShort(with.shortIs(equal((short) 10)));
            exactly(1).of (mock).withInt(with.intIs(equal(10)));
            exactly(1).of (mock).withLong(with.longIs(equal(10L)));
            exactly(1).of (mock).withFloat(with.floatIs(equal(10.0f)));
            exactly(1).of (mock).withDouble(with.doubleIs(equal(10.0)));
        }});
        
        mock.withBoolean(true);
        mock.withByte((byte)10);
        mock.withShort((short)10);
        mock.withInt(10);
        mock.withLong(10L);
        mock.withFloat(10.0f);
        mock.withDouble(10.0);
    }

    @Test
    public void canSetExpectationsWithLiteralsForMethodsWithArgumentsOfPrimitiveTypes() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).withBoolean(true);
            exactly(1).of (mock).withByte((byte)10);
            exactly(1).of (mock).withShort((short)10);
            exactly(1).of (mock).withInt(10);
            exactly(1).of (mock).withLong(10L);
            exactly(1).of (mock).withFloat(10.0f);
            exactly(1).of (mock).withDouble(10.0);
        }});
        
        mock.withBoolean(true);
        mock.withByte((byte)10);
        mock.withShort((short)10);
        mock.withInt(10);
        mock.withLong(10L);
        mock.withFloat(10.0f);
        mock.withDouble(10.0);
    }
}
