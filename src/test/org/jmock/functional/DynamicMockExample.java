/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.functional;

import junit.framework.TestCase;
import org.jmock.Mock;
import org.jmock.dynamic.DynaMock;

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

    public void testWorkingExample() {
        DynaMock mockMarket = new DynaMock(Market.class);
        Agent agent = new Agent((Market) mockMarket.proxy());

        mockMarket.method("buyStock", "IBM", new Integer(10));

        agent.buyLowestPriceStock(20);

        mockMarket.verify();
    }

    public void xtestExample() {
        Mock mockMarket = new Mock(Market.class);
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
