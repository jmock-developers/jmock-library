package test.jmock.examples.timedcache;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Stub;
import org.jmock.examples.timedcache.*;

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
        mockClock.expect(atLeastOnce()).method("getCurrentTime").withNoArguments()
        	.will(returnValues(loadTime, fetchTime));
        
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
        
        mockClock.expect(atLeastOnce()).method("getCurrentTime").withNoArguments()
        	.will(returnValues(loadTime, fetchTime, reloadTime));
        
        mockLoader.expect(times(2)).method("load").with( eq(key) )
    		.will(returnValues(value, newValue));
        
        mockReloadPolicy.expect(atLeastOnce()).method("shouldReload").with( eq(loadTime), eq(fetchTime) )
        	.will(returnValue(true));
       

        assertSame( "should be loaded object", value, cache.lookup(key) );
        assertSame( "should be cached object", newValue, cache.lookup(key) );
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
