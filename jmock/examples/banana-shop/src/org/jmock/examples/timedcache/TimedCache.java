package org.jmock.examples.timedcache;

import java.util.HashMap;

public class TimedCache 
{
    private ObjectLoader loader;
    private Clock clock;
    private ReloadPolicy reloadPolicy;
    private HashMap cache = new HashMap();
    
    private class TimestampedValue {
        public final Object value;
        public final Timestamp loadTime;
        
        public TimestampedValue(final Object value, final Timestamp timestamp) {
            this.value = value;
            this.loadTime = timestamp;
        }
    }
    
    
    public TimedCache(ObjectLoader loader, Clock clock, ReloadPolicy reloadPolicy) {
        this.loader = loader;
        this.clock = clock;
        this.reloadPolicy = reloadPolicy;
    }
    
    public Object lookup(Object theKey) {
        TimestampedValue found = (TimestampedValue) cache.get(theKey);
        
        if( found == null || reloadPolicy.shouldReload(found.loadTime, clock.getCurrentTime())) {
            found = loadObject(theKey);
        }
        
        return found.value;
    }
    
    private TimestampedValue loadObject(Object key) {
        Object value = loader.load(key);
        Timestamp loadTime = clock.getCurrentTime();
        TimestampedValue timestampedValue = new TimestampedValue(value,loadTime);
        
        cache.put( key, timestampedValue);
        
        return timestampedValue;
    }
    
    public void putValue(Object key, Object value, Timestamp loadTime) {
        cache.put( key, new TimestampedValue(value,loadTime) );
        
    }
}