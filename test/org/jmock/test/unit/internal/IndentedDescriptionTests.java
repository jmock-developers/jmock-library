package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.internal.IndentedDescription;

public class IndentedDescriptionTests extends TestCase {
    StringDescription raw = new StringDescription();
    IndentedDescription indented = new IndentedDescription("  ", raw);
    
    public void testIndentsLinesInDescriptionText() {
        indented.appendText("first line\n");
        indented.appendText("second line");
        
        assertEquals("  first line\n  second line", raw.toString());
    }
    
    public void testDoesNotAddIndentationUnlessTextAppendedToNewLine() {
        indented.appendText("first line\n");
        
        assertEquals("  first line\n", raw.toString());
    }
    
    public void testIndentsIfNecessaryBeforePassingValuesThrough() {
        indented.appendValue(1);
        
        assertEquals("  <1>", raw.toString());
    }
    
    public void testIndentsIfNecessaryBeforePassingListsThrough() {
        indented.appendValueList("{", ", ", "}", 1, 2, 3, 4);
        assertEquals("  {<1>, <2>, <3>, <4>}", raw.toString());
    }
}
