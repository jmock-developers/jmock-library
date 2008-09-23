/**
 * 
 */
package hamcrest.spike;

public class HamcrestSpike  {
    private final HamcrestSpikeListener listener;

    public HamcrestSpike(HamcrestSpikeListener listener) {
      this.listener = listener;
    }

    public void single(int i, String string, String otherwise) {
        listener.alsoReceives(new Event(i, string));
    }
    
    public void goForIt(int i, String string, String otherwise) {
        listener.alsoReceives(new Event(i, string));
        listener.receives(new Event(i, string), otherwise);
        listener.receives(new Event(i + 1, string + "1"), otherwise);
    }
    
  }