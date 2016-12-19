package org.jmock.test.acceptance;

import org.jmock.Expectations;
import org.jmock.Mockery;
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

    public final Mockery context = new Mockery();
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
        
        context.assertIsSatisfied();
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

        context.assertIsSatisfied();
    }
    
    /**
     * Will fail unless ExpectationsCreator has added generic overloaded byte code
     */
    @Test
    public void testNonNullNativeIgnoringDocumentationParameterMatcher() {
        context.checking(new Expectations() {{
            exactly(1).of(mock).withBoolean(with.booleanIs(anything()));
            exactly(1).of(mock).withByte(with.byteIs(anything()));
            exactly(1).of(mock).withShort(with.shortIs(anything()));
            exactly(1).of(mock).withInt(with.intIs(anything()));
            exactly(1).of(mock).withLong(with.longIs(anything()));
            exactly(1).of(mock).withFloat(with.floatIs(anything()));
            exactly(1).of(mock).withDouble(with.doubleIs(anything()));
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
