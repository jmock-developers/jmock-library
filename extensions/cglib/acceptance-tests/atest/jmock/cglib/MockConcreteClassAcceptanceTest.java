/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
package atest.jmock.cglib;

import java.util.ArrayList;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.cglib.CGLIBCoreMock;

public class MockConcreteClassAcceptanceTest extends MockObjectTestCase {
    public void testCanMockConcreteClasses() throws Exception {
        Mock listMock = new Mock(new CGLIBCoreMock(ArrayList.class,"listMock"));
        
        assertTrue( "proxy is an ArrayList", listMock.proxy() instanceof ArrayList );
        
        ArrayList proxy = (ArrayList)listMock.proxy();
        Object newElement = newDummy("newElement");
        
        listMock.expect(once()).method("add").with(eq(newElement)).will(returnValue(true));
        
        proxy.add(newElement);
        listMock.verify();
    }
}
