/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package atest.jmock.builder;

import org.jmock.builder.Mock;
import org.jmock.util.MockObjectSupportTestCase;


public class DynamicMockExample extends MockObjectSupportTestCase {
    public interface Market {
    	String[] listStocks();
    	int getPrice( String ticker );
    	void buyStock(String ticker, int quantity);
    }

    public class Agent {
    	Market market;
    	
        public Agent(Market market) {
        	this.market = market;
        }

        public void buyLowestPriceStock(int cost) {
        	String[] stocks = market.listStocks();
        	int cheapestPrice = Integer.MAX_VALUE;
        	String cheapestStock = null;
        	
        	for (int i = 0; i < stocks.length; ++i) {
	        	int price = market.getPrice(stocks[i]);
	        	if (price < cheapestPrice) {
	        		cheapestPrice = price;
	        		cheapestStock = stocks[i];
	        	}
        	}
        	market.buyStock(cheapestStock, cost / cheapestPrice);
        }

    }

    public void testBuilderExample() {
    	Mock mockMarket = new Mock(Market.class);
        Agent agent = new Agent((Market) mockMarket.proxy());
        
        mockMarket.method("listStocks").noParams().willReturn(new String[]{"IBM","ORCL"});
        mockMarket.method("getPrice").with(eq("IBM")).willReturn(10)
        	.expectOnce();
        mockMarket.method("getPrice").with(eq("ORCL")).willReturn(25)
        	.expectOnce();
        mockMarket.method("buyStock").with(eq("IBM"), eq(2)).isVoid()
        	.expectOnce();
        
        agent.buyLowestPriceStock(20);

        mockMarket.verify();
    }

    public void xtestDynaMockExample() {
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
