/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.builder;

import junit.framework.TestCase;

import org.jmock.Constraint;
import org.jmock.constraint.IsEqual;
import org.jmock.dynamic.framework.CoreMock;
import org.jmock.dynamic.framework.InvocationMocker;
import org.jmock.dynamic.framework.LIFOInvocationDispatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.dynamic.stub.VoidStub;


public class DynamicMockExample extends TestCase {
    public interface Market {
    }

    public class Agent {

        public Agent(Market market) {
        }

        public void buyLowestPriceStock(int cost) {
            // TODO Auto-generated method stub
        }

    }

    /* This demonstrates the core API, upon which the sugar methods are layered.
     */
    public void testCoreMockExample() {
    	CoreMock mockMarket = new CoreMock( Market.class, "mockMarket",
    										new LIFOInvocationDispatcher() );
    	Agent agent = new Agent( (Market)mockMarket.proxy() );
    	
    	InvocationMocker invocation = new InvocationMocker();
    	invocation.addMatcher( new MethodNameMatcher("buyStock") );
    	invocation.addMatcher( new ArgumentsMatcher( new Constraint[] {
			new IsEqual("IBM"), new IsEqual(new Integer(10))					
    	} ) );
    	invocation.setStub( new VoidStub() ); // Not really necessary as this is the default
    	mockMarket.add(invocation);
    	//Phew! That's why we need syntactic sugar!
    	
    	agent.buyLowestPriceStock(20);
    	
    	mockMarket.verify();
    }
    
    public void testBuilderExample() {
    	org.jmock.builder.Mock mockMarket = 
    		new org.jmock.builder.Mock(Market.class);
        Agent agent = new Agent((Market) mockMarket.proxy());

        mockMarket.method("buyStock", "IBM", new Integer(10));

        agent.buyLowestPriceStock(20);

        mockMarket.verify();
    }

    public void xtestDynaMockExample() {
        org.jmock.dynamock.Mock mockMarket = 
        	new org.jmock.dynamock.Mock(Market.class);
        Agent agent = new Agent((Market) mockMarket.proxy());
//	
//	 
//	    mockMarket.method("buyStock", "MSFT", new Integer(10)).void();
//
//		mockMarket.method("buyStock", "MSFT", new Integer(10)).returns(true)
//			.expectOnce();
//			//.expectNever();
//			//.addMatcher(new MyExpectation());
//			
//		mockMarket.method("listStocks").alwaysReturns(new Vector("MSFT", "ORCL"));
//		mockMarket.method("getPrice", "MSFT").alwaysReturns(10);
//		mockMarket.method("getPrice", "ORCL").alwaysReturns(50);
//		
//		mockMarket.method(C.equal("buyStock"), C.eq(1)).
//			
//		mockMarket.methodName("listStocks").noParams()
//			.alwaysReturns("MSFT");
//
//		InvocationHandler listInvocation = mockMarket.methodName("listStocks").noParams()
//			.returns("MSFT")
//			.returns("ORCL")
//			.throwsException(new ....);
//
//		mockMarket.methodName("buyStock").params("MSFT", 10).returns(900)
//			.calledOnce()
//			.before(listInvocation);
//		mockMarket.methodName("buyStock").params("ORCL", 2).returns(100)
//			.calledOnce()
//			.before(listInvocation);
//
//		mockMarket.newInvocationHandler().addMatcher( new NameMatcher(new IsEqual("buyStock"))
//			.addMatcher( new ActualParameterMatcher( new Constraint[] { new IsEqual("MSFT"), new IsEqual(new Integer(10)})))
//			.addStub( new ReturnStub( new Integer(900) )));
//			
//			
        agent.buyLowestPriceStock(1000);
    }
}
