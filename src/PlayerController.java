import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerController {
    
    @FXML
    private StackPane stack;

    @FXML
    private MediaView mediaView;

    @FXML
    private BorderPane border;

    @FXML
    private Button btnBack;

    @FXML
    private Slider slider;

    @FXML
    private Button btnPause;

    @FXML
    private Button btnFullScreen;

    // private int video_id;
    private Stage stage;
    private MediaPlayer mediaPlayer;
    private boolean playing;

    @FXML
    private void initialize() {
 
        BooleanProperty mouseMoving = new SimpleBooleanProperty();
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            //Hide when mouse has stopped for set time
            if (!isNowMoving) {
                border.setVisible(false);
            }
        });

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> mouseMoving.set(false));

        stack.setOnMouseMoved((e) -> {
            mouseMoving.set(true);
            pause.playFromStart();

            //Show when mouse moves
            border.setVisible(true);
        });
    }

    public void loadVideo(String title, Media media) {

        // this.video_id = video_id;
        stage = (Stage) stack.getScene().getWindow();
        stage.setTitle(title);

        stage.getScene().setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                playPause();
            }
        });

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
