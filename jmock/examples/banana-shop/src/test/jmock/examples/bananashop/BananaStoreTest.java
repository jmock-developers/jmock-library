package test.jmock.examples.bananashop;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.bananashop.*;

public class BananaStoreTest extends MockObjectTestCase {
    public void testTransfersMoneyFromBankAndInformsCustomerOnSuccessfulBuy() {
        Amount totalCost = (Amount)newDummy(Amount.class, "total cost"); 
        BankAccount shopAccount = (BankAccount)newDummy(BankAccount.class,"shopAccount");

        Mock mockOrderTracker  = new Mock(OrderTracker.class);
        Mock mockPayment = new Mock(Payment.class);
        Mock mockOrder = new Mock(Order.class);    
        final Order order = (Order)mockOrder.proxy();

        BananaStore shop = new BananaStore(shopAccount);

        mockOrder.stub().method("getTotalCost").withNoArguments()
            .will(returnValue(totalCost));
        
        mockPayment.expect(once()).method("transferTo").with(same(shopAccount),eq(totalCost));
        mockOrderTracker.expect(once()).method("orderCompleted").with(same(order));
        mockOrderTracker.expect(once()).method("orderCompleted").with(same(order));
        
        shop.buy( order, 
            (Payment)mockPayment.proxy(), 
            (OrderTracker)mockOrderTracker.proxy() );

        mockPayment.verify();
        mockOrderTracker.verify();
    }
}

