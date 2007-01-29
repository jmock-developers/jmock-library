package org.jmock.internal;

import java.util.HashMap;

import org.jmock.api.Expectation;

public class ExpectationNamespace {
    private HashMap<String,Expectation> namespace = new HashMap<String,Expectation>();
    
    public void bind(String name, Expectation expectation) {
        namespace.put(name, expectation);
    }
    
    public Expectation resolve(String name) {
        return namespace.get(name);
    }
}
