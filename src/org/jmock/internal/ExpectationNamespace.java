package org.jmock.internal;

import java.util.HashMap;

import org.jmock.api.Expectation;

public class ExpectationNamespace {
    private HashMap<String,Expectation> namespace = new HashMap<String,Expectation>();
    
    public void bind(String name, Expectation expectation) {
        if (namespace.containsKey(name)) {
            throw new IllegalArgumentException("name '" + name + "' is already bound");
        }
        
        namespace.put(name, expectation);
    }
    
    public Expectation resolve(String name) {
        if (!namespace.containsKey(name)) {
            throw new IllegalArgumentException("name '" + name + "' is not bound");
        }
        
        return namespace.get(name);
    }
}
