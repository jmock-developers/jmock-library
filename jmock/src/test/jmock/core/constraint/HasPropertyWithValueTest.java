/*  Copyright (c) 2000-2005 jMock.org
 */
package test.jmock.core.constraint;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import junit.framework.TestCase;

import org.jmock.core.constraint.HasPropertyWithValue;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsSame;

/**
 * @author Iain McGinniss
 * @author Nat Pryce
 * @author Steve Freeman
 * @since 1.1.0
 */
public class HasPropertyWithValueTest extends TestCase {
	final private BeanWithoutInfo shouldMatch = new BeanWithoutInfo("is expected");
	final private BeanWithoutInfo shouldNotMatch = new BeanWithoutInfo("not expected");
	final private BeanWithInfo beanWithInfo = new BeanWithInfo("with info");
	
	public void testMatchesInfolessBeanWithMatchedNamedProperty() {
		HasPropertyWithValue hasProperty = new HasPropertyWithValue("property", new IsSame("is expected"));
        
		assertTrue(hasProperty.eval(shouldMatch));
		assertFalse(hasProperty.eval(shouldNotMatch));
	}
	
	public void testMatchesBeanWithInfoWithMatchedNamedProperty() {
		HasPropertyWithValue hasProperty = new HasPropertyWithValue("property", new IsSame("with info"));
		assertTrue(hasProperty.eval(beanWithInfo));
	}
	
	public void testDoesNotMatchInfolessBeanWithoutMatchedNamedProperty() {
		HasPropertyWithValue hasProperty = new HasPropertyWithValue("nonExistentProperty", new IsAnything());
		assertFalse(hasProperty.eval(shouldNotMatch));
	}
	
	public void testDoesNotMatchWriteOnlyProperty() {
		HasPropertyWithValue hasProperty = new HasPropertyWithValue("writeOnlyProperty", new IsAnything());
		assertFalse(hasProperty.eval(shouldNotMatch));
	}
	
	public void testDescribeTo() {
		IsEqual isEqual = new IsEqual(new Boolean(true));
		String isEqualDescription = isEqual.describeTo(new StringBuffer()).toString();
		HasPropertyWithValue hasProperty = new HasPropertyWithValue("property", new IsEqual(new Boolean(true)));

        assertEquals("hasProperty(\"property\", " + isEqualDescription + ")", hasProperty.describeTo(new StringBuffer()).toString());
	}
    
    public static class BeanWithoutInfo {
        private String property;
        
        public BeanWithoutInfo(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
        
        public void setProperty(String property) {
            this.property = property;
        }

        public void setWriteOnlyProperty(float property) {
        }
        
        public String toString() {
            return "[Person: " + property + "]";
        }
    }

    public static class BeanWithInfo {
        private String propertyValue;
        
        public BeanWithInfo(String propertyValue) {
            this.propertyValue = propertyValue;
        }
        public String property() { return propertyValue; }
    }

    public static class BeanWithInfoBeanInfo extends SimpleBeanInfo {
        public PropertyDescriptor[] getPropertyDescriptors() {
            try {
                return new PropertyDescriptor[] {
                        new PropertyDescriptor("property", BeanWithInfo.class, "property", null)
                };
            } catch (IntrospectionException e) {
                throw new RuntimeException("Introspection exception: " + e.getMessage());
            }
        }
    }
}
