package test.jmock.examples.timedcache;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.examples.timedcache.*;

public class TimedCacheTest extends MockObjectTestCase {
    
    final private Object KEY = newDummy("key");
    final private Object VALUE = newDummy("value");
    final private Object NEW_VALUE = newDummy("newValue");

    private Mock mockClock = new Mock(Clock.class);
    private Mock mockLoader = new Mock(ObjectLoader.class);
    private Mock mockReloadPolicy = new Mock(ReloadPolicy.class);
    private TimedCache cache = new TimedCache( (ObjectLoader)mockLoader.proxy(),
            						   			(Clock)mockClock.proxy(), 
            						   			(ReloadPolicy)mockReloadPolicy.proxy() );
    private Timestamp loadTime = (Timestamp) newDummy(Timestamp.class, "loadTime");
    private Timestamp fetchTime = (Timestamp) newDummy(Timestamp.class, "fetchTime");
    private Timestamp reloadTime = (Timestamp) newDummy(Timestamp.class, "reloadTime");

    public void testLoadsObjectThatIsNotCached() {
        mockLoader.expect(once()).method("load").with( eq(KEY) )
        	.will( returnValue(VALUE) );
        mockLoader.expect(once()).method("load").with( eq("key2") )
        	.will( returnValue("value2") );
        mockClock.expect(atLeastOnce()).method("getCurrentTime").withNoArguments()
        	.will( returnValue(loadTime) );

        assertSame( "first object", VALUE, cache.lookup(KEY) );
        assertSame( "second object", "value2", cache.lookup("key2") );
    }

    public void xtestCachedObjectsAreNotReloaded() {
        mockLoader.expect(once()).method("load").with( eq(KEY) )
    	  .will( returnValue(VALUE) );

        assertSame( "loaded object", VALUE, cache.lookup(KEY) );
        assertSame( "cached object", VALUE, cache.lookup(KEY) );
    }
    
    public void testReturnsCachedObjectWithinTimeout() {
        mockLoader.expect(once()).method("load").with( eq(KEY) )
        	.will(returnValue(VALUE));
        
        mockClock.expect(atLeastOnce()).method("getCurrentTime").withNoArguments()
        	.after(mockLoader, "load")
        	.will(returnValues(loadTime, fetchTime));
    
        mockReloadPolicy.expect(atLeastOnce()).method("shouldReload").with( eq(loadTime), eq(fetchTime) )
        	.will(returnValue(false));
        
        assertSame( "should be loaded object", VALUE, cache.lookup(KEY) );
        assertSame( "should be cached object", VALUE, cache.lookup(KEY) );
    }

    public void testReloadsCachedObjectAfterTimeout() {
        mockClock.expect(times(3)).method("getCurrentTime").withNoArguments()
        	.will( returnValues(loadTime, fetchTime, reloadTime) );
        
        mockLoader.expect(times(2))
        	.method("load").with( eq(KEY) )
    		.will(returnValues(VALUE, NEW_VALUE));
        
        mockReloadPolicy.expect(atLeastOnce())
        	.method("shouldReload").with( eq(loadTime), eq(fetchTime) )
        	.will(returnValue(true));
       

        assertSame( "should be loaded object", VALUE, cache.lookup(KEY) );
        assertSame( "should be reloaded object", NEW_VALUE, cache.lookup(KEY) );
    }

    private Stub returnValues(Object value1, Object value2) {
        return new StubsSequence(StubsSequence.asList(returnValue(value1), returnValue(value2)));
    }

    private Stub returnValues(Object value1, Object value2, Object value3) {
        return new StubsSequence(StubsSequence.asList(returnValue(value1), returnValue(value2), returnValue(value3)));
    }
    
    private InvocationMatcher times(int expectedTimes) {
        return new InvokeCountMatcher(expectedTimes);
    }
}
