/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.example.timedcache;

import junit.framework.TestCase;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.jmock.InAnyOrder;
import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.jmock.test.example.timedcache.Clock;
import org.jmock.test.example.timedcache.ObjectLoader;
import org.jmock.test.example.timedcache.ReloadPolicy;
import org.jmock.test.example.timedcache.TimedCache;

import java.util.Date;


public class TimedCacheTest extends TestCase
{

    final private Object KEY = "key";
    final private Object VALUE = "value";
    final private Object NEW_VALUE = "newValue";

    private Mockery context = new Mockery();

    private Clock mockClock = context.mock(Clock.class);
    private ObjectLoader mockLoader = context.mock(ObjectLoader.class);
    private ReloadPolicy mockReloadPolicy = context.mock(ReloadPolicy.class);

    private TimedCache cache = new TimedCache(mockLoader, mockClock, mockReloadPolicy);

    private Date loadTime = new Date(1);
    private Date fetchTime = new Date(2);
    private Date reloadTime = new Date(3);

    public void testLoadsObjectThatIsNotCached() {
        final Object VALUE1 = "value1";
        final Object VALUE2 = "value2";

        context.expects(new InAnyOrder() {{
            allow (mockClock).getCurrentTime(); will(returnValue(loadTime));

            exactly(1).of (mockLoader).load("key1"); will(returnValue(VALUE1));
            exactly(1).of (mockLoader).load("key2"); will(returnValue(VALUE2));
        }});

        MatcherAssert.assertThat(cache.lookup("key1"), IsEqual.eq(VALUE1));
        MatcherAssert.assertThat(cache.lookup("key2"), IsEqual.eq(VALUE2));

        context.assertIsSatisfied();
    }

    public void testReturnsCachedObjectWithinTimeout() {
        context.expects(new InAnyOrder() {{
            allow (mockClock).getCurrentTime(); will(returnValue(fetchTime));
            allow (mockReloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(false));

            exactly(1).of (mockLoader).load(KEY); will(returnValue(VALUE));
        }});

        context.assertIsSatisfied();
        assertSame("should be loaded object", VALUE, cache.lookup(KEY));
        assertSame("should be cached object", VALUE, cache.lookup(KEY));
    }

    public void testReloadsCachedObjectAfterTimeout() {
        context.expects(new InAnyOrder() {{
            allow (mockClock).getCurrentTime(); will(returnValue(fetchTime));
            allow (mockReloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(true));

            context.expects(new InThisOrder() {{
                exactly(1).of (mockLoader).load(KEY); will(returnValue(VALUE));
                exactly(1).of (mockLoader).load(KEY); will(returnValue(NEW_VALUE));
            }});
        }});

        context.assertIsSatisfied();
        assertSame("should be loaded object", VALUE, cache.lookup(KEY));
        assertSame("should be reloaded object", NEW_VALUE, cache.lookup(KEY));
    }
}
