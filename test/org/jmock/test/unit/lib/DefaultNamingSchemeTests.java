package org.jmock.test.unit.lib;

import java.net.URL;

import org.jmock.api.MockObjectNamingScheme;
import org.jmock.lib.DefaultNamingScheme;
import org.jmock.test.unit.support.DummyInterface;

import junit.framework.TestCase;

public class DefaultNamingSchemeTests extends TestCase {
    MockObjectNamingScheme namingScheme = DefaultNamingScheme.INSTANCE;
    
    public void testNamesMocksByLowerCasingFirstCharacterOfTypeName() {
        assertEquals("runnable", namingScheme.defaultNameFor(Runnable.class));
        assertEquals("dummyInterface", namingScheme.defaultNameFor(DummyInterface.class));
    }
    
    public interface GPSReceiver {}
    public interface HTTPClient {};
    public interface UDPDatagram {};
    
    public void testReturnsGoodNamesForClassesThatStartWithAcronyms() {
        assertEquals("gpsReceiver", namingScheme.defaultNameFor(GPSReceiver.class));
        assertEquals("httpClient", namingScheme.defaultNameFor(HTTPClient.class));
        assertEquals("udpDatagram", namingScheme.defaultNameFor(UDPDatagram.class));
        
    }
    
    public void testReturnsGoodNamesForClassesThatAreEntirelyAcronyms() {
        assertEquals("url", namingScheme.defaultNameFor(URL.class));
    }
}
