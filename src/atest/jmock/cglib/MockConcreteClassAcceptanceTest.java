/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock.cglib;

import java.util.ArrayList;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.jmock.util.NotImplementedException;


public class MockConcreteClassAcceptanceTest extends MockObjectTestCase
{
    public void testCanMockConcreteClasses() throws Exception {
        Mock listMock = mock(ArrayList.class, "listMock");

        assertTrue("proxy is an ArrayList", listMock.proxy() instanceof ArrayList);

        ArrayList proxy = (ArrayList)listMock.proxy();
        Object newElement = newDummy("newElement");

        listMock.expects(once()).method("add").with(eq(newElement)).will(returnValue(true));

        proxy.add(newElement);
        listMock.verify();
    }
    
    public static class ClassWithConstructorArguments {
        public ClassWithConstructorArguments(int arg1, String arg2) {
            // do nothing
        }
        
        public String mockedMethod() { throw new NotImplementedException("not implemented"); }
    }
    
    public void testCanMockConcreteClassesWithConstructorArguments() throws Exception {
        Mock mock = mock(ClassWithConstructorArguments.class, 
                		 new Class[]{int.class,String.class}, 
                		 new Object[]{new Integer(1), ""});
        
        mock.expects(once()).method("mockedMethod").withNoArguments().will(returnValue("result"));
        
        assertEquals("result", ((ClassWithConstructorArguments)mock.proxy()).mockedMethod());
        
        mock.verify();
    }
    
    public void testCanGiveExplicitNameToMockOfConcreteClassesWithConstructorArguments() throws Exception {
        String mockName = "MOCK_NAME";
        Mock mock = mock(ClassWithConstructorArguments.class,
                		 mockName,
                		 new Class[]{int.class,String.class}, 
                		 new Object[]{new Integer(1), ""});
        
        assertEquals(mockName, mock.toString());
        
        mock.verify();
    }
    
    public static class ClassWithComplexConstructor {
        public String resultOfMockedMethodCalledInConstructor;
        
        public ClassWithComplexConstructor() {
            resultOfMockedMethodCalledInConstructor = mockedMethod();
        }
        
        public String mockedMethod() { return "ORIGINAL_RESULT"; }
    }
    
    public void testCanMockClassesWithComplexConstructors() throws Exception {
        String mockName = "MOCK_NAME";
        Mock mock = mock(ClassWithComplexConstructor.class,mockName);
        
        mock.expects(once()).method("mockedMethod").withNoArguments().will(returnValue("result"));
        
        assertEquals("mocked result",
                	 "result", ((ClassWithComplexConstructor)mock.proxy()).mockedMethod());
        assertEquals("original result",
                	 "ORIGINAL_RESULT", ((ClassWithComplexConstructor)mock.proxy()).resultOfMockedMethodCalledInConstructor);
        
        mock.verify();
    }
}


