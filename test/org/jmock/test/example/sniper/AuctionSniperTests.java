package org.jmock.test.example.sniper;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.InAnyOrder;
import org.jmock.integration.junit3.MockObjectTestCase;

public class AuctionSniperTests extends MockObjectTestCase {
    Bid increment = new Bid(2);
    Bid maximumBid = new Bid(20);
    Bid beatableBid = new Bid(10);
    Bid unbeatableBid = maximumBid.add(new Bid(1));

    Lot lot = mock(Lot.class);
    AuctionSniperListener listener = mock(AuctionSniperListener.class, "listener");

    AuctionSniper sniper = new AuctionSniper(lot, increment, maximumBid, listener);

    public void testTriesToBeatTheLatestHighestBid() throws Exception {
        final Bid expectedBid = beatableBid.add(increment);

        expects(new InAnyOrder() {{
            exactly(1).of (lot).bid(expectedBid);
        }});

        sniper.bidAccepted(lot, beatableBid);
    }

    public void testWillNotBidPriceGreaterThanMaximum() throws Exception {
        expects(new InAnyOrder() {{
            ignoring (listener);
            never (lot).bid(with(any(Bid.class)));
        }});
        sniper.bidAccepted(lot, unbeatableBid);
    }

    public void testWillLimitBidToMaximum() throws Throwable {
        expects(new InAnyOrder() {{
            exactly(1).of (lot).bid(maximumBid);
        }});

        sniper.bidAccepted(lot, maximumBid.subtract(new Bid(1)));
    }

    public void testWillNotBidWhenToldAboutBidsOnOtherItems() throws Throwable {
        final Lot otherLot = mock(Lot.class, "otherLot");

        expects(new InAnyOrder() {{
           never (otherLot).bid(new Bid(10));
        }});

        sniper.bidAccepted(otherLot, beatableBid);
    }

    public void testWillAnnounceItHasFinishedIfPriceGoesAboveMaximum() {
        expects(new InAnyOrder() {{
            exactly(1).of (listener).sniperFinished(sniper);
        }});

        sniper.bidAccepted(lot, unbeatableBid);
    }

    public void testCatchesExceptionsAndReportsThemToErrorListener() throws Exception {
        final AuctionException exception = new AuctionException("test");

        expects(new InAnyOrder() {{
            allowing (lot).bid(with(anyBid)); will(throwException(exception));
            exactly(1).of (listener).sniperBidFailed(sniper, exception);
        }});

        sniper.bidAccepted(lot, beatableBid);
    }

    private final Matcher<Bid> anyBid = IsAnything.anything();
}
