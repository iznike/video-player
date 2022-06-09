import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class PlayerController {
    
    @FXML
    private StackPane stack;

    @FXML
    private MediaView mediaView;

    // private int video_id;
    private Stage stage;
    private MediaPlayer mediaPlayer;
    private boolean playing;

    @FXML
    private void initialize() {
        
    }

    public void loadVideo(String title, Media media) {

        // this.video_id = video_id;
        stage = (Stage) stack.getScene().getWindow();
        stage.setTitle(title);

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnError(()->System.out.println("media error"+mediaPlayer.getError().toString()));
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
