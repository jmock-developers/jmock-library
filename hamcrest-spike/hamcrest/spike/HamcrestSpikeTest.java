package hamcrest.spike;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
/*
Unexpected invocation: foo.blah(10, 20) 
Expected: 
    foo.blah(less than 10, 30) 
        mismatch: parameter 0 was greater than or equal to 10 
        mismatch: parameter 1 was not equal to 30 
    bar.somethingElse("hello") 
        mismatch: target object was foo 
 */
@RunWith(JMock.class)
public class HamcrestSpikeTest {
  Mockery context = new Mockery();
  HamcrestSpikeListener listener = context.mock(HamcrestSpikeListener.class);
  HamcrestSpike spike = new HamcrestSpike(listener);
  
  @Test public void displaysDifference() {
      context.checking(new Expectations() {{
        one(listener).alsoReceives(with(anEvent(1, "one")));
        one(listener).receives(with(anEvent(1, "one")), with(equal("right")));
      }});
    
      spike.goForIt(1, "one", "right");
  }
  
  
  protected Matcher<Event> anEvent(final int one, final String two) {
    return new TypeSafeDiagnosingMatcher<Event>() {
      @Override
      public boolean matchesSafely(Event item, Description description) {
        description.appendText("Event: " + item.one + ", \"" + item.two + "\"");
        return item.one == one && two.equals(item.two);
      }

      public void describeTo(Description description) {
          description.appendText("Event with " + one + " and \"" + two + "\"");
      }
    };
  }
  
}
