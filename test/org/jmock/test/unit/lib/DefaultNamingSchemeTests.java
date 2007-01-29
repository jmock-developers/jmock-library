package org.jmock.test.unit.lib;

import org.jmock.api.MockObjectNamingScheme;
import org.jmock.lib.DefaultNamingScheme;
import org.jmock.test.unit.support.DummyInterface;

import junit.framework.TestCase;

public class DefaultNamingSchemeTests extends TestCase {
    public void testNamesMocksByLowerCasingFirstCharacterOfTypeName() {
        MockObjectNamingScheme namingScheme = DefaultNamingScheme.INSTANCE;
        
        assertEquals("runnable", namingScheme.defaultNameFor(Runnable.class));
        assertEquals("dummyInterface", namingScheme.defaultNameFor(DummyInterface.class));
    }
}
