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
    MediaControl mediaControl = context.mock(MediaControl.class);

    DJ dj = new DJ(mediaControl);
    
    private static final String HAMMERSMITH = "hammersmith";
    private static final String HAMMERSMITH_TRACK = "the-clash/white-man-in-the-hammersmith-palais.mp3";
    
    private static final String WATERLOO = "waterloo";
    private static final String WATERLOO_TRACK = "the-kinks/waterloo-sunset.mp3";
    
    @Before
    public void initialiseTracksForLocations() {
        dj.addTrackForLocation(WATERLOO, WATERLOO_TRACK);
        dj.addTrackForLocation(HAMMERSMITH, HAMMERSMITH_TRACK);
    }
    
    @Test public void
    startsPlayingTrackForCurrentLocationWhenLocationFirstDetected() {
        context.checking(new Expectations() {{
            one (mediaControl).play(WATERLOO_TRACK);
        }});
        
        dj.locationChangedTo(WATERLOO);
    }
    
    @Test public void
    playsTrackForCurrentLocationWhenPreviousTrackFinishes() {
        startingIn(WATERLOO);
        
        dj.locationChangedTo(HAMMERSMITH);
        
        context.checking(new Expectations() {{
            one (mediaControl).play(HAMMERSMITH_TRACK);
        }});
        
        dj.mediaFinished();
    }
    
    @Test public void
    doesNotPlayTrackAgainIfStillInTheSameLocation() {
        startingIn(HAMMERSMITH);
        
        context.checking(new Expectations() {{
            never (mediaControl).play(with(any(String.class)));
        }});
        
        dj.mediaFinished();
    }
    
    @Test public void
    playsNewTrackAsSoonAsLocationChangedIfPreviousTrackFinishedWhileInSameLocation() {
        startingIn(HAMMERSMITH);
        dj.mediaFinished();
        
        context.checking(new Expectations() {{
            one (mediaControl).play(WATERLOO_TRACK);
        }});
        
        dj.locationChangedTo(WATERLOO);
    }
    
    private void startingIn(String initialLocation) {
        context.checking(new Expectations() {{
            one (mediaControl).play(with(any(String.class)));
        }});
        
        dj.locationChangedTo(initialLocation);
    }
}
