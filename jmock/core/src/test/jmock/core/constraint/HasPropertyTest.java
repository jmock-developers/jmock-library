/*  Copyright (c) 2000-2005 jMock.org
 */
package test.jmock.core.constraint;

import junit.framework.TestCase;

import org.jmock.core.constraint.HasProperty;

/**
 * @author Iain McGinniss
 * @author Nat Pryce
 * @author Steve Freeman
 * @since 1.1.0
 */
public class HasPropertyTest extends TestCase {

	private final HasPropertyWithValueTest.BeanWithoutInfo bean = new HasPropertyWithValueTest.BeanWithoutInfo("a bean");

    public void testReturnsTrueIfPropertyExists() {
		HasProperty hasProperty = new HasProperty("writeOnlyProperty");

        assertTrue(hasProperty.eval(bean));
	}
	
	public void testReturnsFalseIfPropertyDoesNotExist() {
		HasProperty hasProperty = new HasProperty("aNonExistentProp");

        assertFalse(hasProperty.eval(bean));
	}
	
	public void testDescribeTo() {
		HasProperty hasProperty = new HasProperty("property");
		StringBuffer buffer = new StringBuffer();
		hasProperty.describeTo(buffer);
	
        assertEquals("hasProperty(\"property\")", buffer.toString());
	}   
}
