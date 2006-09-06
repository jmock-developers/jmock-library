package org.jmock.internal;

import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

public class IndentedDescription implements Description {
    private final String indent;
    private final Description raw;
    private boolean mustIndent = true;
    
    public IndentedDescription(Description raw) {
        this("  ", raw);
    }
    
    public IndentedDescription(String indent, Description raw) {
        this.indent = indent;
        this.raw = raw;
    }
    
    public Description appendText(String text) {
        StringBuffer indented = new StringBuffer();
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            if (mustIndent) {
                indented.append(indent);
            }
            indented.append(ch);
            mustIndent = (ch == '\n');
        }
        
        raw.appendText(indented.toString());
        
        return this;
    }
    
    public Description appendValue(Object value) {
        indentIfNecessary();
        raw.appendValue(value);
        return this;
    }
    
    public <T> Description appendValueList(String start, String separator, String end, T... values) {
        indentIfNecessary();
        raw.appendValueList(start, separator, end, values);
        return this;
    }

    public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
        indentIfNecessary();
        raw.appendValueList(start, separator, end, values);
        return null;
    }

    private void indentIfNecessary() {
        if (mustIndent) {
            raw.appendText(indent);
            mustIndent = false;
        }
    }

    public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
        return appendList(start, separator, end, values.iterator());
    }
    
    private Description appendList(String start, String separator, String end, Iterator<? extends SelfDescribing> i) {
        boolean separate = false;
        appendText(start);
        while (i.hasNext()) {
            if (separate) appendText(separator);
            i.next().describeTo(this);
            separate = true;
        }
        appendText(end);
        
        return this;
    }
}
