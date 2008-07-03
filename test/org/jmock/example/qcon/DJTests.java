package org.jmock.example.qcon;

import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;

public class DJTests extends MockObjectTestCase {
    Playlist playlist = mock(Playlist.class);
    MediaControl mediaControl = mock(MediaControl.class);
    
    DJ dj = new DJ(playlist, mediaControl);
    
    private static final String LOCATION_A = "location-a";
    private static final String TRACK_A = "track-a";
    
    private static final String LOCATION_B = "location-b";
    private static final String TRACK_B = "track-b";
    
    @Override
    public void setUp() {
        checking(new Expectations() {{
            allowing (playlist).hasTrackFor(LOCATION_A); will(returnValue(true));
            allowing (playlist).trackFor(LOCATION_A); will(returnValue(TRACK_A));
            allowing (playlist).hasTrackFor(LOCATION_B); will(returnValue(true));
            allowing (playlist).trackFor(LOCATION_B); will(returnValue(TRACK_B));
            allowing (playlist).hasTrackFor(with(any(String.class))); will(returnValue(false));
        }});
    }
    
    public void testStartsPlayingTrackForCurrentLocationWhenLocationFirstDetected() {
        checking(new Expectations() {{
            oneOf (mediaControl).play(TRACK_A);
        }});
        
        dj.locationChangedTo(LOCATION_A);
    }
    
    public void testPlaysTrackForCurrentLocationWhenPreviousTrackFinishesIfLocationChangedWhileTrackWasPlaying() {
        startingIn(LOCATION_A);
        
        dj.locationChangedTo(LOCATION_B);
        
        checking(new Expectations() {{
            oneOf (mediaControl).play(TRACK_B);
        }});
        
        dj.mediaFinished();
    }
    
    public void testDoesNotPlayTrackAgainIfStillInTheSameLocation() {
        startingIn(LOCATION_A);
        
        checking(new Expectations() {{
            never (mediaControl).play(with(any(String.class)));
        }});
        
        dj.mediaFinished();
    }
    
    public void testPlaysNewTrackAsSoonAsLocationChangesIfPreviousTrackFinishedWhileInSameLocation() {
        startingIn(LOCATION_A);
        dj.mediaFinished();
        
        checking(new Expectations() {{
            oneOf (mediaControl).play(TRACK_B);
        }});
        
        dj.locationChangedTo(LOCATION_B);
    }
    
    private void startingIn(String initialLocation) {
        checking(new Expectations() {{
            oneOf (mediaControl).play(with(any(String.class)));
        }});
        
        dj.locationChangedTo(initialLocation);
    }
}
