package test.jmock.examples.timedcache;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.timedcache.Clock;
import org.jmock.examples.timedcache.ReloadPolicy;
import org.jmock.examples.timedcache.ObjectLoader;
import org.jmock.examples.timedcache.TimedCache;
import org.jmock.examples.timedcache.Timestamp;

public class TimedCacheTest extends MockObjectTestCase {
    
    private Object key = newDummy("key");
    private Object value = newDummy("value");
    private Mock mockClock = new Mock(Clock.class);
    private Mock mockLoader = new Mock(ObjectLoader.class);
    private Mock mockReloadPolicy = new Mock(ReloadPolicy.class);
    private TimedCache cache = new TimedCache( (ObjectLoader)mockLoader.proxy(),
            						   			(Clock)mockClock.proxy(), 
            						   			(ReloadPolicy)mockReloadPolicy.proxy() );
    private Timestamp loadTime = (Timestamp) newDummy(Timestamp.class, "loadTime");
    private Timestamp fetchTime = (Timestamp) newDummy(Timestamp.class, "fetchTime");

    public void testLoadsObjectThatIsNotCached() {
        mockLoader.expect(once()).method("load").with( eq(key) )
        	.will(returnValue(value));

        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
        	.will(returnValue(loadTime));
        
        assertSame( "should be loaded object", value, cache.lookup(key) );
    }

    public void testReturnsCachedObjectWithinTimeout() {
        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
        	.will(returnValue(loadTime)).id("loadTime");
        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
        	.after("loadTime")
        	.will(returnValue(fetchTime));
        
        mockLoader.expect(once()).method("load").with( eq(key) )
        	.will(returnValue(value));
        
        mockReloadPolicy.expect(atLeastOnce()).method("shouldReload").with( eq(loadTime), eq(fetchTime) )
        	.will(returnValue(false));
        
        assertSame( "should be loaded object", value, cache.lookup(key) );
        assertSame( "should be cached object", value, cache.lookup(key) );
    }

    public void testReloadsCachedObjectAfterTimeout() {
        Timestamp reloadTime = (Timestamp) newDummy(Timestamp.class, "reloadTime");
        Object newValue = newDummy("newValue");
        
        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
        	.will(returnValue(loadTime)).id("loadTime");
        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
        	.after("loadTime")
        	.will(returnValue(fetchTime))
        	.id("fetch time");
        
        mockLoader.expect(once()).method("load").with( eq(key) )
    		.will(returnValue(value)).id("firstload");
        
        mockReloadPolicy.expect(atLeastOnce()).method("shouldReload").with( eq(loadTime), eq(fetchTime) )
        	.will(returnValue(true));
        
        mockLoader.expect(once()).method("load").with( eq(key) )
        	.after("firstload")
    		.will(returnValue(newValue));

        mockClock.expect(once()).method("getCurrentTime").withNoArguments()
    		.after("fetch time")
    		.will(returnValue(reloadTime));
        
        assertSame( "should be loaded object", value, cache.lookup(key) );
        assertSame( "should be cached object", newValue, cache.lookup(key) );
    }

}
