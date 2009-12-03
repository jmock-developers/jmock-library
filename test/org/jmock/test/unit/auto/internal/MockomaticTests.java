package org.jmock.test.unit.auto.internal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.auto.Auto;
import org.jmock.auto.Mock;
import org.jmock.auto.internal.Mockomatic;
import org.jmock.test.acceptance.MockedType;

public class MockomaticTests extends TestCase {
    Mockery mockery = new Mockery();
    Mockomatic mockomatic = new Mockomatic(mockery);
    
    public static class ObjectWithPublicAndPrivateFields {
        public @Mock MockedType publicMock;
        private @Mock MockedType privateMock;
        
        public MockedType privateMock() { return privateMock; }  
    }
    
    public void testCreatesMockObjectsNamedAfterTheField() {        
        ObjectWithPublicAndPrivateFields example = new ObjectWithPublicAndPrivateFields();
        
        mockomatic.fillIn(example);
        
        assertThat("created public mock", 
                   example.publicMock, notNullValue());
        assertThat("named public mock after field", 
                   example.publicMock.toString(), equalTo("publicMock"));

        assertThat("created private mock", 
                   example.privateMock(), notNullValue());
        assertThat("named private mock after field", 
                   example.privateMock().toString(), equalTo("privateMock"));
    }
    
    public static class BaseClass {
        public @Mock MockedType mockInBaseClass;
    }
    public static class DerivedClass extends BaseClass {
        public @Mock MockedType mockInDerivedClass;
    }
    
    public void testCreatesMockObjectsInAllClassesInInheritanceHierarchy() {
        DerivedClass example = new DerivedClass();
        mockomatic.fillIn(example);
        
        assertThat("created mock in base class", example.mockInBaseClass, notNullValue());
        assertThat("created mock in derived class", example.mockInDerivedClass, notNullValue());
    }
    
    public static class WantsStates {
        public @Auto States stateMachine;
    }
    
    public void testCreatesStateMachinesNamedAfterTheField() {
        WantsStates example = new WantsStates();
        mockomatic.fillIn(example);
        
        assertThat("created state machine", 
                   example.stateMachine, notNullValue());
        assertThat("named state machine after field", 
                   example.stateMachine.toString(), startsWith("stateMachine "));
    }
    
    public static class WantsSequence {
        public @Auto Sequence aSequence;
    }
    
    public void testCreatesSequencesNamedAfterTheField() {
        WantsSequence example = new WantsSequence();
        mockomatic.fillIn(example);
        
        assertThat("created sequence", 
                   example.aSequence, notNullValue());
        assertThat("named sequence after field", 
                   example.aSequence.toString(), equalTo("aSequence"));
    }
}
