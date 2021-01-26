package uk.jamesdal.perfmock.test.unit.lib;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.api.MockObjectNamingScheme;
import uk.jamesdal.perfmock.lib.RetroNamingScheme;
import uk.jamesdal.perfmock.test.unit.support.DummyInterface;

public class RetroNamingSchemeTests extends TestCase {
    public void testNamesMocksByLowerCasingFirstCharacterOfTypeName() {
        MockObjectNamingScheme namingScheme = RetroNamingScheme.INSTANCE;
        
        assertEquals("mockRunnable", namingScheme.defaultNameFor(Runnable.class));
        assertEquals("mockDummyInterface", namingScheme.defaultNameFor(DummyInterface.class));
    }
}
