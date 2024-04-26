package game.utils;

import javafx.scene.media.MediaPlayer;
import utils.Common;

public class DistanceAwareAudio {
    private final MediaPlayer mediaPlayer;
    private final Vector position = new Vector();
    private final Vector awarenessPosition = new Vector();
    private float awarenessDistance = 300;
    
    public DistanceAwareAudio(String audioUrl, Vector position) {
        this.mediaPlayer = new MediaPlayer(Common.loadMedia(audioUrl));
        this.position.set(position);
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public void setAwarenessPosition(Vector awarenessPosition) {
        this.awarenessPosition.set(awarenessPosition);
    }
    
    public void setAwarenessDistance(float awarenessDistance) {
        this.awarenessDistance = awarenessDistance;
    }
    
    public void update() {
        float distance = this.position.getDistanceFrom(awarenessPosition);
        float computedVolume = game.utils.Common.map(
            distance,
            0,
            awarenessDistance,
            1,
            0
        );
        
        this.mediaPlayer.setVolume(computedVolume);
    }
    
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
