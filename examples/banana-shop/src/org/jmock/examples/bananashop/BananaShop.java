package org.jmock.examples.bananashop;

public class BananaShop {
    private BankAccount account;

    public BananaShop(BankAccount account) {
        this.account = account;
    }

    public void buy(Order order, BankAccount customerAccount, OrderTracker tracker) {
        customerAccount.transferTo(account, order.getTotalCost());
        tracker.orderCompleted(order);
    }

}
