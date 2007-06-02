package org.jmock.examples.website;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Stub;


public class CallbackExampleTest extends MockObjectTestCase
{
    private static final String NAME = "the-name";

    public interface Named {
        void collectName( List list );
    }

    public void testCallbackStub() {
        Mock mockNamed = mock(Named.class);

        mockNamed.expects(once()).method("collectName").with(NOT_NULL)
            .will(addListElement(NAME));
        
        List list = new ArrayList();
        ((Named)mockNamed.proxy()).collectName(list);

        assertTrue("list should contain name", list.contains(NAME) );
    }

    private Stub addListElement( Object newElement ) {
        return new AddToListStub(newElement);
    }
}
