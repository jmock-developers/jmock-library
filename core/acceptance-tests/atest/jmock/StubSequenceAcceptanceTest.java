package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


public class StubSequenceAcceptanceTest extends MockObjectTestCase
{
	public interface Greeter
	{
		public String greeting();
	}

	public void testCanEasilySpecifySequenceOfStubsForSameMethod() {
		Mock mock = mock(Greeter.class);
		Greeter greeter = (Greeter)mock.proxy();

		mock.expects(atLeastOnce()).method("greeting").withNoArguments()
		        .will(onSubsequentCalls(returnValue("hello"),
		                                returnValue("bonjour"),
		                                returnValue("guten Tag")));

		assertEquals("hello", greeter.greeting());
		assertEquals("bonjour", greeter.greeting());
		assertEquals("guten Tag", greeter.greeting());
	}
}
