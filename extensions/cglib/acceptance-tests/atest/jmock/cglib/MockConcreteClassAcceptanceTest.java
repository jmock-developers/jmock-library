package atest.jmock.cglib;

import java.util.ArrayList;
import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;


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
}
