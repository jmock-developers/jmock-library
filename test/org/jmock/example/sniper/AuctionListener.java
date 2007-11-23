package org.jmock.example.sniper;

public interface AuctionListener {
    void bidAccepted(Auction item, Money amount);
}
