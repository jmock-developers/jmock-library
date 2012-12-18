package testdata.jmock.acceptance.junit4;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class JUnit4TestThatCreatesTwoMockeries {
    Mockery contextA = new JUnit4Mockery();
    Mockery contextB = new JUnit4Mockery();
    
    @Test
    public void happy() {
        // a-ok!
    }

}
