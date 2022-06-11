import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

    @FXML
    private Label lblCurrentTime;

    @FXML
    private Label lblMaxTime;

    // private int video_id;
    private Stage stage;
    private MediaPlayer mediaPlayer;
    private boolean playing;
    private boolean seeking = false;

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
            switch (e.getCode()) {
                case SPACE:
                    playPause();
                    break;
                case LEFT:
                    backTen();
                    break;
                case RIGHT:
                    forwardTen();
                    break;
                default:
                    break;
            }
        });

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            //Once mediaPlayer has loaded media
            slider.setMax(media.getDuration().toMinutes());
            lblMaxTime.setText(minutesToHMS(slider.getMax()));
            mediaPlayer.play();
            playing = true;
        });
        mediaPlayer.setOnError(()->System.out.println("media error"+mediaPlayer.getError().toString()));
        mediaView.setMediaPlayer(mediaPlayer);
        mediaView.setFitWidth(800);

        //When mediaPlayer time changes, update slider value and current time label
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!seeking) {
                slider.setValue(newTime.toMinutes());
            }
            lblCurrentTime.setText(minutesToHMS(newTime.toMinutes()));
        });

        //If slider value changes to something different than current mediaPlayer time, update mediaPlayer time
        slider.setOnMouseReleased((e) -> {
            if (slider.getValue() != mediaPlayer.getCurrentTime().toMinutes()){
                mediaPlayer.seek(Duration.minutes(slider.getValue()));
            }
            seeking = false;
        });
        
        //While dragging the automatic update of slider with time stops (so it doesn't jerk around)
        slider.setOnDragDetected((e) -> {
            seeking = true;
        });
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

    // @FXML
    private void backTen() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
    }

    // @FXML
    private void forwardTen() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    private String minutesToHMS(double mins) {

        double tempMins = mins;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (mins >= 60) {
            hours = (int)(tempMins / 60);
            tempMins -= hours * 60;
        }

        minutes = (int)tempMins;
        seconds = (int)((tempMins - minutes) * 60);

        String HMS = String.format("%02d:%02d", minutes, seconds);
        if (hours != 0) {
            HMS = String.format("%02d:"+HMS, hours);
        }

        return HMS;
    }

}
