package org.jmock.examples.bananashop;

public class BananaStore {
    private BankAccount account;

    public BananaStore(BankAccount account) {
        this.account = account;
    }

    public void buy(Order order, Payment payment, OrderTracker tracker) {
        payment.transferTo(account, order.getTotalCost());
        tracker.orderCompleted(order);
    }

}
