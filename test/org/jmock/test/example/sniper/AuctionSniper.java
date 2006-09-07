package org.jmock.test.example.sniper;

public class AuctionSniper implements AuctionListener {
    private Lot lotToBidFor;
    private Bid bidIncrement;
    private Bid maximumBid;
    private AuctionSniperListener listener;

    public AuctionSniper(Lot lotToBidFor, Bid bidIncrement, Bid maximumBid, AuctionSniperListener listener) {
        this.lotToBidFor = lotToBidFor;
        this.bidIncrement = bidIncrement;
        this.maximumBid = maximumBid;
        this.listener = listener;
    }

    public void bidAccepted(Lot lot, Bid amount) {
        if (lot != lotToBidFor) return;

        if (amount.compareTo(maximumBid) <= 0) {
            placeBid(lot, amount);
        }
        else {
            listener.sniperFinished(this);
        }
    }

    private void placeBid(Lot item, Bid amount) {
        try {
            item.bid(Bid.min(maximumBid, amount.add(bidIncrement)));
        }
        catch (AuctionException ex) {
            listener.sniperBidFailed(this, ex);
        }
    }
}
