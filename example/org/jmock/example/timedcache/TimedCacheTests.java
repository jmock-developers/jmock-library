/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.example.timedcache;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit3.JUnit3ErrorTranslator;


public class TimedCacheTests extends TestCase {
    final private Object KEY = "key";
    final private Object VALUE = "value";
    final private Object NEW_VALUE = "newValue";

    private Mockery context = new Mockery() {{
        setExpectationErrorTranslator(JUnit3ErrorTranslator.INSTANCE);
    }};
    
    private Clock clock = context.mock(Clock.class);
    private ObjectLoader loader = context.mock(ObjectLoader.class, "loader");
    private ReloadPolicy reloadPolicy = context.mock(ReloadPolicy.class);

    private TimedCache cache = new TimedCache(loader, clock, reloadPolicy);

    private Date loadTime = time(1);
    private Date fetchTime = time(2);

    public void testLoadsObjectThatIsNotCached() {
        final Object VALUE1 = "value1";
        final Object VALUE2 = "value2";

        context.checking(new Expectations() {{
            allowing (clock).time(); will(returnValue(loadTime));

            oneOf (loader).load("key1"); will(returnValue(VALUE1));
            oneOf (loader).load("key2"); will(returnValue(VALUE2));
        }});

        Object actualValue1 = cache.lookup("key1");
        Object actualValue2 = cache.lookup("key2");
        
        context.assertIsSatisfied();
        assertEquals("lookup with key1", VALUE1, actualValue1);
        assertEquals("lookup with key2", VALUE2, actualValue2);
    }

    public void testReturnsCachedObjectWithinTimeout() {
        context.checking(new Expectations() {{
            oneOf (clock).time(); will(returnValue(loadTime));
            oneOf (clock).time(); will(returnValue(fetchTime));
            
            allowing (reloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(false));
            
            oneOf (loader).load(KEY); will(returnValue(VALUE));
        }});
        
        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);
        
        context.assertIsSatisfied();
        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be cached object", VALUE, actualValueFromSecondLookup);
    }

    public void testReloadsCachedObjectAfterTimeout() {
        context.checking(new Expectations() {{
            allowing (reloadPolicy).shouldReload(loadTime, fetchTime); will(returnValue(true));
            
            oneOf (clock).time(); will(returnValue(loadTime));
            oneOf (loader).load(KEY); will(returnValue(VALUE));
            
            oneOf (clock).time(); will(returnValue(fetchTime));
            oneOf (loader).load(KEY); will(returnValue(NEW_VALUE));
        }});
        
        Object actualValueFromFirstLookup = cache.lookup(KEY);
        Object actualValueFromSecondLookup = cache.lookup(KEY);

        context.assertIsSatisfied();
        
        assertSame("should be loaded object", VALUE, actualValueFromFirstLookup);
        assertSame("should be reloaded object", NEW_VALUE, actualValueFromSecondLookup);
    }

    private Date time(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_YEAR, i);
        return calendar.getTime();
    }
}
