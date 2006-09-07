package org.jmock.test.example.sniper;

import java.math.BigDecimal;

public class Bid implements Comparable<Bid> {
    private BigDecimal amount;

    public Bid(BigDecimal amount) {
        this.amount = amount;
    }

    public Bid(int amount) {
        this(new BigDecimal(amount));
    }

    public Bid(double amount) {
        this(new BigDecimal(amount));
    }

    public Bid add(Bid other) {
        return new Bid(amount.add(other.amount));
    }

    public Bid subtract(Bid other) {
        return new Bid(amount.subtract(other.amount));
    }

    public String toString() {
        return "£" + amount;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return amount.equals(((Bid) o).amount);
    }

    public int hashCode() {
        return amount.hashCode();
    }

    public int compareTo(Bid other) {
        return amount.compareTo(other.amount);
    }

    public static Bid min(Bid m1, Bid m2) {
        return new Bid(m1.amount.min(m2.amount));
    }
}
