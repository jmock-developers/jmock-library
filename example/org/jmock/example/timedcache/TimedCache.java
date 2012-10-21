package org.jmock.example.timedcache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimedCache {
    private ObjectLoader loader;
    private Clock clock;
    private ReloadPolicy reloadPolicy;
    private Map<Object,TimestampedValue> cache = new HashMap<Object,TimestampedValue>();
    
    public TimedCache(ObjectLoader loader, Clock clock, ReloadPolicy reloadPolicy) {
        this.loader = loader;
        this.clock = clock;
        this.reloadPolicy = reloadPolicy;
    }

    public Object lookup(Object key) {
        Date fetchTime = clock.time();
        TimestampedValue cached = cache.get(key);
        
        if (cached == null || reloadPolicy.shouldReload(cached.loadTime, fetchTime)) {
            Object value = loader.load(key);
            cached = new TimestampedValue(value, fetchTime);
            cache.put(key, cached);
        }
        
        return cached.value;
    }
    
    private class TimestampedValue {
        public final Object value;
        public final Date loadTime;
        
        public TimestampedValue(Object value, Date timestamp) {
            this.value = value;
            this.loadTime = timestamp;
        }
    }
}
