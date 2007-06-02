/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.website;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.stub.DefaultResultStub;


public class BeanPropertyGetterMatcherTest extends MockObjectTestCase
{
    private DefaultResultStub returnDefaultValue = new DefaultResultStub();
    private BeanPropertyGetterMatcher beanPropertyGetters = new BeanPropertyGetterMatcher();

    public void testCanMatchPropertyGetters() {
        Mock mockPerson = mock(Person.class);
        Person person = (Person)mockPerson.proxy();

        mockPerson.stubs().match(beanPropertyGetters).will(returnDefaultValue);

        assertEquals("age", 0, person.age());
        assertEquals("name", "", person.name());
        assertEquals("children", 0, person.children().length);
        assertNotNull("spouse", person.spouse());
    }
}
