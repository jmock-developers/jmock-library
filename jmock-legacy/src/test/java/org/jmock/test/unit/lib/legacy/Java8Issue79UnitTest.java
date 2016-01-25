package org.jmock.test.unit.lib.legacy;

import java.io.File;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;

/**
 * This used to fail in Java 8 as cglib-nodep used asm:5.0.3 which required jdk<=7
 * Now we use cglib (not cglib-nodep) and override asm to 5.0.4
 * @see https://github.com/jmock-developers/jmock-library/issues/79
 * @see https://github.com/cglib/cglib/issues/20
 */
public class Java8Issue79UnitTest {
    @Rule
    public final JUnitRuleMockery _context = new JUnitRuleMockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testMock() {
        _context.mock(File.class);
    }
}
