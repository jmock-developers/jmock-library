package org.jmock.test.unit.lib;

import java.net.URL;

import junit.framework.TestCase;

import org.jmock.api.MockObjectNamingScheme;
import org.jmock.lib.CamelCaseNamingScheme;
import org.jmock.test.unit.support.DummyInterface;

public class CamelCaseNamingSchemeTests extends TestCase {
    MockObjectNamingScheme namingScheme = CamelCaseNamingScheme.INSTANCE;
    
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
