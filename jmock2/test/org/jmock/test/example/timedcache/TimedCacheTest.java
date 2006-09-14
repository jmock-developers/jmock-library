/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.example.timedcache;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3ErrorTranslator;


public class TimedCacheTest extends TestCase {
    final private Object KEY = "key";
    final private Object VALUE = "value";
    final private Object NEW_VALUE = "newValue";

    private Mockery context = new Mockery() {{
        setExpectationErrorTranslator(new JUnit3ErrorTranslator());
    }};
    
    private Clock mockClock = context.mock(Clock.class);
    private ObjectLoader mockLoader = context.mock(ObjectLoader.class);
    private ReloadPolicy mockReloadPolicy = context.mock(ReloadPolicy.class);

    private TimedCache cache = new TimedCache(mockLoader, mockClock, mockReloadPolicy);

    private Date loadTime = time(1);
    private Date fetchTime = time(2);

    private Date time(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_YEAR, i);
        return calendar.getTime();
    }
    
    public void testLoadsObjectThatIsNotCached() {
        final Object VALUE1 = "value1";
        final Object VALUE2 = "value2";

        context.expects(new InAnyOrder() {{
            allow (mockClock).getCurrentTime(); will(returnValue(loadTime));

            exactly(1).of (mockLoader).load("key1"); will(returnValue(VALUE1));
            exactly(1).of (mockLoader).load("key2"); will(returnValue(VALUE2));
        }});

        Object actualValue1 = cache.lookup("key1");
        Object actualValue2 = cache.lookup("key2");
        
        context.assertIsSatisfied();
        assertEquals("lookup with key1", VALUE1, actualValue1);
        assertEquals("lookup with key2", VALUE2, actualValue2);
    }

    public void testReturnsCachedObjectWithinTimeout() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mockClock).getCurrentTime(); will(returnValue(loadTime));
            exactly(1).of (mockClock).getCurrentTime(); will(returnValue(fetchTime));
            
            allow (mockReloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(false));

            exactly(1).of (mockLoader).load(KEY); will(returnValue(VALUE));
        }});

        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);
        
        context.assertIsSatisfied();
        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be cached object", VALUE, actualValueFromSecondLookup);
    }

    public void testReloadsCachedObjectAfterTimeout() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mockClock).getCurrentTime(); will(returnValue(loadTime));
            exactly(1).of (mockClock).getCurrentTime(); will(returnValue(fetchTime));
            
            allow (mockReloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(true));

            exactly(1).of (mockLoader).load(KEY); will(returnValue(VALUE));
            exactly(1).of (mockLoader).load(KEY); will(returnValue(NEW_VALUE));
        }});

        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);

        context.assertIsSatisfied();
        
        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be reloaded object", NEW_VALUE, actualValueFromSecondLookup);
    }
}
