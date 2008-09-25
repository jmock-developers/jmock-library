/**
 * 
 */
package hamcrest.spike;


public interface HamcrestSpikeListener {
    void receives(Event event, String otherwise);
    void withTwoArgs(Event event, String other);
    void alsoReceives(Event event);
    void withThreeArgs(Event with, String with2, String with3);
  }