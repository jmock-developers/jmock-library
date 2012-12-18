package testdata.jmock.acceptance.junit4;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatDoesNotCreateAMockery {
    Mockery context = null;
    
    @Test
    public void happy() {
        // a-ok!
    }
}
