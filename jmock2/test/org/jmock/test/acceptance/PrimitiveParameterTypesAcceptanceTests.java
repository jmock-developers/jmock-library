package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;

import junit.framework.TestCase;

public class PrimitiveParameterTypesAcceptanceTests extends TestCase {
    public interface MethodsWithPrimitiveTypes {
        void withBoolean(boolean b);
        void withByte(byte b);
        void withShort(short s);
        void withInt(int i);
        void withLong(long l);
        void withFloat(float f);
        void withDouble(double d);
    }
    
    Mockery context = new Mockery();
    MethodsWithPrimitiveTypes mock = context.mock(MethodsWithPrimitiveTypes.class, "mock");
    
    public void testCanSetExpectationsWithMatchersForMethodsWithArgumentsOfPrimitiveTypes() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).withBoolean(with(equal(true)));
            exactly(1).of (mock).withByte(with(equal((byte)10)));
            exactly(1).of (mock).withShort(with(equal((short)10)));
            exactly(1).of (mock).withInt(with(equal(10)));
            exactly(1).of (mock).withLong(with(equal(10L)));
            exactly(1).of (mock).withFloat(with(equal(10.0f)));
            exactly(1).of (mock).withDouble(with(equal(10.0)));
        }});
        
        mock.withBoolean(true);
        mock.withByte((byte)10);
        mock.withShort((short)10);
        mock.withInt(10);
        mock.withLong(10L);
        mock.withFloat(10.0f);
        mock.withDouble(10.0);
        
        context.assertIsSatisfied();
    }

    public void testCanSetExpectationsWithLiteralsForMethodsWithArgumentsOfPrimitiveTypes() {
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
        
        context.assertIsSatisfied();
    }
}
