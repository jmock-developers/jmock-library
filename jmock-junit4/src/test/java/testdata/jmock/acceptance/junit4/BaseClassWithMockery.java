package testdata.jmock.acceptance.junit4;

import uk.jamesdal.perfmock.Mockery;
import uk.jamesdal.perfmock.integration.junit4.JMock;
import uk.jamesdal.perfmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class BaseClassWithMockery {
    protected Mockery context = new JUnit4Mockery();
}
