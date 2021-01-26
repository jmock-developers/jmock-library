package uk.jamesdal.perfmock.test.unit;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.Mockery;
import uk.jamesdal.perfmock.test.unit.support.DummyInterface;

public class MockeryTests extends TestCase {
    public interface AnotherInterface {}
    
    public void testNamesMockObjectAfterMockedTypeIfNoNameSpecified() {
        Mockery mockery = new Mockery();
        
        assertEquals("dummyInterface", 
                     mockery.mock(DummyInterface.class).toString());
        assertEquals("anotherInterface", 
                     mockery.mock(AnotherInterface.class).toString());
    }

    public void testNamesMockObjectAfterExplicitNameIfNameIsSpecified() {
        Mockery mockery = new Mockery();
        
        assertEquals("firstMock", 
                     mockery.mock(DummyInterface.class, "firstMock").toString());
        assertEquals("secondMock", 
                     mockery.mock(AnotherInterface.class, "secondMock").toString());
    }
}
