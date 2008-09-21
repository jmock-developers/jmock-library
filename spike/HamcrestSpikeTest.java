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
  Listener listener = context.mock(Listener.class);
  HamcrestSpike spike = new HamcrestSpike(listener);
  
  @Test public void displaysDifference() {
      context.checking(new Expectations() {{
        one(listener).receives(with(anEvent()), with(equal("right")));
      }});
    
      spike.goForIt(1, "one", "right");
  }
  
  
  protected Matcher<Event> anEvent() {
    return new TypeSafeDiagnosingMatcher<Event>() {
      @Override
      public boolean matchesSafely(Event item, Description description) {
        description.appendText("Event: " + item.one + ", \"" + item.two + "\"");
        return item.one == 3 && "four".equals(item.two);
      }

      public void describeTo(Description description) {
          description.appendText("Event with 3 and \"four\"");
      }
    };
  }


  public static class Event {
    public final int one;
    public final String two;

    public Event(int one, String two) {
      this.one = one;
      this.two = two;
      
    }
  }

  public interface Listener {
    void receives(Event event, String otherwise);
  }
  
  public static class HamcrestSpike  {
    private final Listener listener;

    public HamcrestSpike(Listener listener) {
      this.listener = listener;
    }

    public void goForIt(int i, String string, String otherwise) {
      listener.receives(new Event(i, string), otherwise);
    }
    
  }
  
}
