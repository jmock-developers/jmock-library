package test.jmock.core.stub;

import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.stub.CustomStub;


public class CustomStubTest extends TestCase
{
    static class ConcreteSideEffect extends CustomStub
    {
        public ConcreteSideEffect( String description ) {
            super(description);
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return null;
        }
    }

    public void testWritesDescriptionToStringBuffer() {
        String description = "DESCRIPTION";
        CustomStub sideEffect = new ConcreteSideEffect(description);

        StringBuffer buf = new StringBuffer();

        assertSame("should return buffer", buf, sideEffect.describeTo(buf));

        assertEquals("should be description", description, buf.toString());
    }

}
