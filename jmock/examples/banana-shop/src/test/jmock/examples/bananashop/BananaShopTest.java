package test.jmock.examples.bananashop;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.bananashop.*;

public class BananaShopTest extends MockObjectTestCase {
    public void testTransfersMoneyFromBankAndInformsCustomerOnSuccessfulBuy() {
        Amount totalCost = (Amount)newDummy(Amount.class, "total cost"); 
        BankAccount shopAccount = (BankAccount)newDummy(BankAccount.class,"shopAccount");

        Mock mockOrderTracker  = new Mock(OrderTracker.class);
        Mock mockCustomerAccount = new Mock(BankAccount.class,"mockCustomerAccount");
        Mock mockOrder = new Mock(Order.class);    
        final Order order = (Order)mockOrder.proxy();

        BananaShop shop = new BananaShop(shopAccount);

        mockOrder.stub().method("getTotalCost").withNoArguments()
            .will(returnValue(totalCost));
        
        mockCustomerAccount.expect(once()).method("transferTo").with(same(shopAccount),eq(totalCost));
        mockOrderTracker.expect(once()).method("orderCompleted").with(same(order));
        
        shop.buy( order, 
            (BankAccount)mockCustomerAccount.proxy(), 
            (OrderTracker)mockOrderTracker.proxy() );

        mockCustomerAccount.verify();
        mockOrderTracker.verify();
    }
}

