package atest.jmock.builder;

import org.jmock.builder.Mock;

import junit.framework.TestCase;


public class ReturnTypeAcceptanceTest extends TestCase {
    public interface ReturnTypes {
    	String returnString();
        boolean returnBoolean();
        byte returnByte();
        char returnChar();
        short returnShort();
        int returnInt();
        long returnLong();
        float returnFloat();
        double returnDouble();
    }
    
    private Mock mock;
    private ReturnTypes proxy;
    
    public void setUp() {
    	mock = new Mock( ReturnTypes.class, "theMock" );
        proxy = (ReturnTypes)mock.proxy();
    }
    
    public void testCanReturnObjectReferences() {
    	String result = new String("RESULT");
        
        mock.method("returnString").willReturn(result);
        
        assertSame( "should be same result", result, proxy.returnString() );
    }
    
    public void testCanReturnBooleanValues() {
        mock.method("returnBoolean").willReturn(true);
        assertTrue( "should be true", proxy.returnBoolean() );
        
        mock.method("returnBoolean").willReturn(false);
        assertFalse( "should be false", proxy.returnBoolean() );
    }
    
    public void testCanReturnByteValues() {
        byte result = 123;
        
        mock.method("returnByte").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnByte() );
    }
    
    public void testCanReturnCharValues() {
        char result = '\u1234';
        
        mock.method("returnChar").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnChar() );
    }
    
    public void testCanReturnShortValues() {
        short result = 12345;
        
        mock.method("returnShort").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnShort() );
    }
    
    public void testCanReturnIntValues() {
    	int result = 1234567890;
        
        mock.method("returnInt").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnInt() );
    }
    
    public void testCanReturnLongValues() {
        long result = 1234567890124356789L;
        
        mock.method("returnLong").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnLong() );
    }
    
    public void testCanReturnFloatValues() {
        float result = 12345.67890f;
        
        mock.method("returnFloat").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnFloat(), 0.0 );
    }

    public void testCanReturnDoubleValues() {
        double result = 1234567890.1234567890;
        
        mock.method("returnDouble").willReturn(result);
        
        assertEquals( "should be same result", result, proxy.returnDouble(), 0.0 );
    }
}
