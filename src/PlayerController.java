import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class PlayerController {
    
    @FXML
    private StackPane stack;

    @FXML
    private MediaView mediaView;

    private MediaPlayer mediaPlayer;
    private boolean playing;

    @FXML
    private void initialize() {
        File file = new File("C:\\Users\\bel\\Videos\\SampleVideo_1280x720_30mb.mp4");
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        playing = true;
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setFitWidth(800);
    }

    @FXML
    private void playPause() {
        if (playing) {
            mediaPlayer.pause();
            playing = false;
        } else {
            mediaPlayer.play();
            playing = true;
        }
    }
}
