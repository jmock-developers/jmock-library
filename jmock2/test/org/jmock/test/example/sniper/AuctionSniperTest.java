package org.jmock.test.example.sniper;

import junit.framework.TestCase;

public class AuctionSniperTest extends TestCase {
    FakeLot lot = new FakeLot();
    Bid increment = new Bid(2);
    Bid maximumBid = new Bid(20);
    Bid beatableBid = new Bid(10);
    Bid unbeatableBid = maximumBid.add(new Bid(1));

    FakeSniperListener listener = new FakeSniperListener();
    AuctionSniper sniper = new AuctionSniper(lot, increment, maximumBid, listener);


    public void testTriesToBeatTheLatestHighestBid() {
        lot.expectedBidAmount = beatableBid.add(increment);

        sniper.bidAccepted(lot, beatableBid);

        assertTrue("sniper bid for item", lot.bidWasCalled);
    }

    public void testWillNotBidPriceGreaterThanMaximum() {
        sniper.bidAccepted(lot, unbeatableBid);

        assertFalse("sniper must not bid for item", lot.bidWasCalled);
    }

    public void testWillAnnounceItHasFinishedIfPriceGoesAboveMaximum() {
        lot.expectedBidAmount = beatableBid.add(increment);
        sniper.bidAccepted(lot, beatableBid);
        assertTrue("has not finished", !listener.hasFinished);

        lot.expectedBidAmount = null;
        sniper.bidAccepted(lot, unbeatableBid);
        assertTrue("has finished", listener.hasFinished);
    }

    public void testWillLimitBidToMaximum() {
        lot.expectedBidAmount = maximumBid;

        sniper.bidAccepted(lot, maximumBid.subtract(new Bid(1)));

        assertTrue("sniper bid for item", lot.bidWasCalled);
    }

    public void testWillNotBidWhenToldAboutBidsOnOtherItems() {
        FakeLot otherItem = new FakeLot();

        sniper.bidAccepted(otherItem, beatableBid);

        assertFalse("sniper must not bid for item", lot.bidWasCalled);
        assertFalse("sniper must not bid for other item", otherItem.bidWasCalled);
    }

    public void testCatchesExceptionsAndReportsThemToSniperListener() {
        lot.bidException = new AuctionException("test");

        sniper.bidAccepted(lot, beatableBid);

        assertSame(lot.bidException, listener.failure);
    }

    static class FakeLot implements Lot {
        public Bid expectedBidAmount = null;
        public boolean bidWasCalled = false;
        public AuctionException bidException = null;

        public void bid(Bid amount) throws AuctionException {
            bidWasCalled = true;
            if (bidException != null) {
                throw bidException;
            }
            assertEquals("amount", expectedBidAmount, amount);
        }
    }

    static class FakeSniperListener implements AuctionSniperListener {
        public Exception failure = null;
        public boolean hasFinished = false;

        public void sniperBidFailed(AuctionSniper sniper, AuctionException failure) {
            this.failure = failure;
        }

        public void sniperFinished(AuctionSniper sniper) {
            hasFinished = true;
        }
    }
}
