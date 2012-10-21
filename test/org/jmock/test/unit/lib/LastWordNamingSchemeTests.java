package org.jmock.test.unit.lib;

import java.lang.reflect.InvocationHandler;
import java.net.URL;
import java.util.Collection;
import java.util.ListIterator;

import junit.framework.TestCase;

import org.jmock.api.MockObjectNamingScheme;
import org.jmock.lib.LastWordNamingScheme;

public class LastWordNamingSchemeTests extends TestCase {
    MockObjectNamingScheme namingScheme = LastWordNamingScheme.INSTANCE;
    
    
    public void testNamesMocksByLowercasingTheLastWordOfTheTypeName() {
        assertEquals("iterator", namingScheme.defaultNameFor(ListIterator.class));
        assertEquals("handler", namingScheme.defaultNameFor(InvocationHandler.class));
    }
    
    
    public void testLowercasesEntireNameIfItContainsOnlyOneWord() {
        assertEquals("runnable", namingScheme.defaultNameFor(Runnable.class));
        assertEquals("collection", namingScheme.defaultNameFor(Collection.class));
    }

    public interface X {}
    public void testLowercasesNameContainingSingleUpperCaseLetter() {
        assertEquals("x", namingScheme.defaultNameFor(X.class));
    }
    
    public interface y {}
    public void testLowercasesNameContainingSingleLowerCaseLetter() {
        assertEquals("y", namingScheme.defaultNameFor(y.class));
    }
    
    public void testLowercasesEntireNameIfItContainsOnlyCapitals() {
        assertEquals("url", namingScheme.defaultNameFor(URL.class));
    }
    
    public interface Party1999 {}
    public interface NMEA0183 {} // standard for sending GPS data over serial lines
    
    public void testAllowsNamesToEndInNumbers() {
        assertEquals("party1999", namingScheme.defaultNameFor(Party1999.class));
        assertEquals("nmea0183", namingScheme.defaultNameFor(NMEA0183.class));
    }
    
    public interface TelnetURL {}
    
    public void testReturnsTrailingAcronymInLowerCase() {
        assertEquals("url", namingScheme.defaultNameFor(TelnetURL.class));
    }
}
