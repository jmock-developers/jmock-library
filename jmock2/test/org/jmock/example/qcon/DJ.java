package org.jmock.example.qcon;


public class DJ implements LocationAware, MediaTracker {
    private final MediaControl mediaControl;
    private final Playlist playlist;
    
    private String currentLocationName = null;
    private boolean trackFinished = true;
    private boolean locationChanged = false;

    
    public DJ(Playlist playlist, MediaControl mediaControl) {
        this.playlist = playlist;
        this.mediaControl = mediaControl;
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
        mediaControl.play(playlist.trackFor(currentLocationName));
    }
}
