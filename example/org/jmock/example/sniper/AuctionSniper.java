package org.jmock.example.sniper;

public class AuctionSniper implements AuctionListener {
    private Auction lotToBidFor;
    private Money bidIncrement;
    private Money maximumBid;
    private AuctionSniperListener listener;

    public AuctionSniper(Auction lotToBidFor, Money bidIncrement, Money maximumBid, AuctionSniperListener listener) {
        this.lotToBidFor = lotToBidFor;
        this.bidIncrement = bidIncrement;
        this.maximumBid = maximumBid;
        this.listener = listener;
    }

    public void bidAccepted(Auction lot, Money amount) {
        if (lot != lotToBidFor) return;

        if (amount.compareTo(maximumBid) <= 0) {
            placeBid(lot, amount);
        }
        else {
            listener.sniperFinished(this);
        }
    }

    private void placeBid(Auction item, Money amount) {
        try {
            item.bid(Money.min(maximumBid, amount.add(bidIncrement)));
        }
        catch (AuctionException ex) {
            listener.sniperBidFailed(this, ex);
        }
    }
}
