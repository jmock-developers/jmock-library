package test.jmock.examples.bananashop;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.bananashop.*;

public class BananaStoreTest extends MockObjectTestCase {
    public void testTransfersMoneyFromBankAndInformsCustomerOnSuccessfulBuy() { 
        Amount orderCost = (Amount)newDummy(Amount.class, "total cost"); 
        BankAccount storeBankAccount = (BankAccount)newDummy(BankAccount.class);

        Mock mockOrderTracker  = new Mock(OrderTracker.class);
        Mock mockPayment = new Mock(Payment.class);
        Mock mockOrder = new Mock(Order.class);    
        final Order order = (Order)mockOrder.proxy();

        BananaStore store = new BananaStore(storeBankAccount);

        mockOrder.stub().method("getTotalCost").withNoArguments()
            .will(returnValue(orderCost));
        
        mockPayment.expect(once()).method("transferTo").with( same(storeBankAccount), eq(orderCost) );
        mockOrderTracker.expect(once()).method("orderCompleted").with( same(order) );
        
        store.buy( order, 
            (Payment)mockPayment.proxy(), 
            (OrderTracker)mockOrderTracker.proxy() );

        mockPayment.verify();
        mockOrderTracker.verify();
    }
}

