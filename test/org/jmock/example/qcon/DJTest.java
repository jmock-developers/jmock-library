package org.jmock.example.qcon;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DJTest {
    Mockery context = new JUnit4Mockery();
    Playlist playlist = context.mock(Playlist.class);
    MediaControl mediaControl = context.mock(MediaControl.class);
    
    DJ dj = new DJ(playlist, mediaControl);
    
    private static final String LOCATION_A = "location-a";
    private static final String TRACK_A = "track-a";
    
    private static final String LOCATION_B = "location-b";
    private static final String TRACK_B = "track-b";
    
    @Before
    public void initialiseTracksForLocations() {
        context.checking(new Expectations() {{
            allowing (playlist).hasTrackFor(LOCATION_A); will(returnValue(true));
            allowing (playlist).trackFor(LOCATION_A); will(returnValue(TRACK_A));
            allowing (playlist).hasTrackFor(LOCATION_B); will(returnValue(true));
            allowing (playlist).trackFor(LOCATION_B); will(returnValue(TRACK_B));
            allowing (playlist).hasTrackFor(with(any(String.class))); will(returnValue(false));
        }});
    }
    
    @Test public void
    startsPlayingTrackForCurrentLocationWhenLocationFirstDetected() {
        context.checking(new Expectations() {{
            one (mediaControl).play(TRACK_A);
        }});
        
        dj.locationChangedTo(LOCATION_A);
    }
    
    @Test public void
    playsTrackForCurrentLocationWhenPreviousTrackFinishesIfLocationChangedWhileTrackWasPlaying() {
        startingIn(LOCATION_A);
        
        dj.locationChangedTo(LOCATION_B);
        
        context.checking(new Expectations() {{
            one (mediaControl).play(TRACK_B);
        }});
        
        dj.mediaFinished();
    }
    
    @Test public void
    doesNotPlayTrackAgainIfStillInTheSameLocation() {
        startingIn(LOCATION_A);
        
        context.checking(new Expectations() {{
            never (mediaControl).play(with(any(String.class)));
        }});
        
        dj.mediaFinished();
    }
    
    @Test public void
    playsNewTrackAsSoonAsLocationChangesIfPreviousTrackFinishedWhileInSameLocation() {
        startingIn(LOCATION_A);
        dj.mediaFinished();
        
        context.checking(new Expectations() {{
            one (mediaControl).play(TRACK_B);
        }});
        
        dj.locationChangedTo(LOCATION_B);
    }
    
    private void startingIn(String initialLocation) {
        context.checking(new Expectations() {{
            one (mediaControl).play(with(any(String.class)));
        }});
        
        dj.locationChangedTo(initialLocation);
    }
}
