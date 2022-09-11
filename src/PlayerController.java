import java.io.File;
import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
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

    @FXML
    private Button btnBackTen;

    @FXML
    private Button btnForwardTen;

    @FXML
    private Button btnVolume;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Group volumeGroup;

    private int video_id;
    private Stage stage;
    private MediaPlayer mediaPlayer;
    private boolean playing;
    private boolean seeking = false;

    @FXML
    private void initialize() {
 
        //Set up icons
        Labeled[] iconsNeeded12 = {btnBackTen, btnForwardTen, btnVolume};
        for (int i=0;i<iconsNeeded12.length;i++) {
            iconsNeeded12[i].setFont(Icon.fontAwesome12);
        }
        
        Labeled[] iconsNeeded18 = {btnBack, btnPause, btnFullScreen};
        for (int i=0;i<iconsNeeded18.length;i++) {
            iconsNeeded18[i].setFont(Icon.fontAwesome18);
        }

        btnBack.setText(Icon.ARROW_LEFT);
        btnPause.setText(Icon.PAUSE);
        btnBackTen.setText(Icon.BACKWARD);
        btnForwardTen.setText(Icon.FORWARD);
        btnFullScreen.setText(Icon.EXPAND);
        btnVolume.setText(Icon.VOLUME_DOWN);
        
        //Mouse movement detection
        BooleanProperty mouseMoving = new SimpleBooleanProperty();
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            //Hide when mouse has stopped for set time, unless stopped over a control button
            if (!isNowMoving && !isHoverControls()) {
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

    public void loadVideo(int video_id, String title, Media media, Duration resumeTime) {

        this.video_id = video_id;
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
            mediaPlayer.seek(resumeTime);
            mediaPlayer.play();
            playing = true;
        });
        mediaPlayer.setOnError(()->System.out.println("media error"+mediaPlayer.getError().toString()));
        mediaView.setMediaPlayer(mediaPlayer);

        //Bind mediaView width to width of window
        mediaView.fitWidthProperty().bind(stage.getScene().widthProperty());

        //Bind mediaPlayer volume to volumeSlider
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());

        //Change volume button icon as volume is changed
        mediaPlayer.volumeProperty().addListener((obs, oldVolume, newVolume) -> {
            setVolumeIcon();
        });

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
            btnPause.setText(Icon.PLAY);
        } else {
            mediaPlayer.play();
            playing = true;
            btnPause.setText(Icon.PAUSE);
        }
    }

    @FXML
    private void backTen() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
    }

    @FXML
    private void forwardTen() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    @FXML
    private void fullscreen() {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            btnFullScreen.setText(Icon.EXPAND);
        } else {
            stage.setFullScreen(true);
            btnFullScreen.setText(Icon.COMPRESS);
        }
    }

    @FXML
    private void exit() {

        saveResumeTime();
                
        //Load home page
        try {
            Parent root = FXMLLoader.load(new File("src/home.fxml").toURI().toURL());
            Scene scene = new Scene(root);
            stage.setTitle("");
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void onMouseEnterVolume() {
        volumeSlider.setVisible(true);
    }

    @FXML
    private void onMouseExitVolume() {
        volumeSlider.setVisible(false);
    }

    @FXML
    private void onVolumeClicked() {
        //Unmute or mute
        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false);
            setVolumeIcon();
        }
        else {
            mediaPlayer.setMute(true);
            btnVolume.setText(Icon.VOLUME_MUTE);
        }
    }

    private void setVolumeIcon() {
        double volumeValue = volumeSlider.getValue();

        //Set volume button icon based on what the volume is
        if (volumeValue == 0) {
            btnVolume.setText(Icon.VOLUME_MUTE);
        }
        else if (volumeValue > 0 && volumeValue <= 0.5) {
            btnVolume.setText(Icon.VOLUME_DOWN);
        }
        else {
            btnVolume.setText(Icon.VOLUME_UP);
        }
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

    private void saveResumeTime() {

        Duration currentTime = mediaPlayer.getCurrentTime();

        //If video at end, reset resume time to 0, if not, save where video is up to and stop it
        if (currentTime.equals(mediaPlayer.getStopTime())) {
            DBController.updateVideoTime(video_id, 0);
        } else {
            DBController.updateVideoTime(video_id, currentTime.toMinutes());
        }

        mediaPlayer.stop();

    }

    private boolean isHoverControls() {
        return volumeGroup.isHover() || btnBackTen.isHover()
            || btnPause.isHover() || btnForwardTen.isHover()
            || btnFullScreen.isHover();
    }

}
