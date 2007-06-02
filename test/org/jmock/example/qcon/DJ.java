package org.jmock.example.qcon;

import java.util.HashMap;
import java.util.Map;

public class DJ implements LocationAware, MediaTracker {
    private final MediaControl mediaControl;
    private final Map<String,String> tracksByLocation = new HashMap<String,String>();
    
    private String currentLocationName = null;
    private boolean trackFinished = true;
    private boolean locationChanged = false;
    
    public DJ(MediaControl mediaControl) {
        this.mediaControl = mediaControl;
    }
    
    public void addTrackForLocation(String locationName, String trackFileName) {
        tracksByLocation.put(locationName, trackFileName);
    }
    
    public void locationChangedTo(String newLocationName) {
        currentLocationName = newLocationName;
        
        if (trackFinished) {
            startPlaying();
            trackFinished = false;
        }
        else {
            locationChanged = true;
        }
    }
    
    public void mediaFinished() {
        if (locationChanged) {
            startPlaying();
            locationChanged = false;
        }
        else {
            trackFinished = true;
        }
    }
    
    private void startPlaying() {
        mediaControl.play(tracksByLocation.get(currentLocationName));
    }
}
