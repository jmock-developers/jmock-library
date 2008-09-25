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
  
  @Test public void unexpectedInvocation() {
      context.checking(new Expectations() {{
        one(listener).alsoReceives(with(anEvent(1, "one")));
        one(listener).receives(with(anEvent(1, "one")), with(equal("right")));
        one(listener).receives(with(anEvent(2, "two")), with(equal("wrong")));
        allowing(listener).withThreeArgs(with(anEvent(1, "one")), with("wrong"), with("third"));
      }});
    
      spike.goForIt(1, "one", "right");
  }

  @Test public void multipleMethodsWithSameArguments() {
      context.checking(new Expectations() {{
        allowing(listener).alsoReceives(with(anEvent(1, "one")));
        
        one(listener).receives(with(anEvent(2, "three")), with(equal("wrong")));
        one(listener).receives(with(anEvent(2, "two")), with(equal("wrong")));
        one(listener).withThreeArgs(with(anEvent(1, "one")), with(equal("right")), with(equal("third")));
      }});
    
      spike.goForIt(1, "one", "right");
  }

  @Test public void missingInvocation() {
      context.checking(new Expectations() {{
        one(listener).alsoReceives(with(anEvent(1, "one")));
        one(listener).receives(with(anEvent(1, "one")), with(equal("right")));
      }});

      spike.single(1, "one", "right");
  }
  
  
  protected Matcher<Event> anEvent(final int a, final String b) {
    return new TypeSafeDiagnosingMatcher<Event>() {
      @Override
      public boolean matchesSafely(Event item, Description description) {
          if (a != item.a) {
              description.appendText("'a' was not " + a);
              return false;
          }
          if (! b.equals(item.b)) {
              description.appendText("'b' was not " + b);
              return false;
          }
          return true;
      }

      public void describeTo(Description description) {
          description.appendText("Event with " + a + " and \"" + b + "\"");
      }
    };
  }
  
}
