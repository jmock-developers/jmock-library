package org.jmock.examples.bananashop;

public interface Payment {
    void transferTo(BankAccount destination, Amount amount);
}
