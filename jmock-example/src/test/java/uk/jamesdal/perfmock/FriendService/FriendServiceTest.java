package uk.jamesdal.perfmock.FriendService;

import org.junit.Rule;
import org.junit.Test;
import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.integration.junit4.perf.PerfMockery;
import uk.jamesdal.perfmock.lib.concurrent.Synchroniser;
import uk.jamesdal.perfmock.perf.PerfRule;
import uk.jamesdal.perfmock.perf.PerfTest;
import uk.jamesdal.perfmock.perf.concurrent.PerfThreadFactory;
import uk.jamesdal.perfmock.perf.models.Normal;
import uk.jamesdal.perfmock.perf.postproc.reportgenerators.ConsoleReportGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FriendServiceTest {

    @Rule
    public PerfRule perfRule = new PerfRule(new ConsoleReportGenerator());

    @Rule
    public PerfMockery ctx = new PerfMockery(perfRule) {{
        setThreadingPolicy(new Synchroniser());
    }};

    private final FriendApi api = ctx.mock(FriendApi.class);

    private final List<Integer> ids = Collections.nCopies(20, 1);

    @Test
    @PerfTest(iterations = 1000, warmups = 0)
    public void simpleTest1() {
        FriendService service = new FriendService(api, new PerfThreadFactory(perfRule.getSimulation()));

        ctx.checking(new Expectations() {{
            oneOf(api).getFriends(); will(returnValue(ids)); taking(seconds(0));
            allowing(api).getProfilePic(with(any(Integer.class))); will(returnValue(new ProfilePic())); taking(seconds(new Normal(5.0, 0.001)));
        }});

        List<ProfilePic> res = service.getFriendsProfilePictures();
        assertEquals(res.size(), ids.size());
    }

}