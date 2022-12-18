import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerController {
    
    @FXML
    private StackPane stack;

    @FXML
    private ImageView videoImageView;

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
    private EmbeddedMediaPlayer mediaPlayer;
    private boolean playing;
    private AtomicBoolean seeking = new AtomicBoolean(false);

    @FXML
    private void initialize() {
 
        //Set up media player
        MediaPlayerFactory factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        mediaPlayer.videoSurface().set(new ImageViewVideoSurface(videoImageView));
        
        //Set up icons
        // Labeled[] iconsNeeded12 = {btnBackTen, btnForwardTen, btnVolume};
        // for (int i=0;i<iconsNeeded12.length;i++) {
        //     iconsNeeded12[i].setFont(Icon.fontAwesome12);
        // }
        
        // Labeled[] iconsNeeded18 = {btnBack, btnPause, btnFullScreen};
        // for (int i=0;i<iconsNeeded18.length;i++) {
        //     iconsNeeded18[i].setFont(Icon.fontAwesome18);
        // }

        btnBack.setText(Icon.ARROW_LEFT);
        btnPause.setText(Icon.PAUSE);
        btnBackTen.setText(Icon.BACKWARD);
        btnForwardTen.setText(Icon.FORWARD);
        btnFullScreen.setText(Icon.EXPAND);
        btnVolume.setText(Icon.VOLUME_DOWN);

        // btnPause.setFont(Font.font(Icon.fontAwesome18.getFamily(), 38));
        // System.out.println(Icon.fontAwesome12.getFamily());
        // btnPause.setStyle("-fx-font: 40 'Font Awesome 5 Free Solid'");
        
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

    public void loadVideo(int video_id, String title, String path, long resumeTime) {

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

        String startTimeOption = ":start-time=" + (resumeTime/1000);    //milliseconds to seconds
        mediaPlayer.media().play(path, startTimeOption);
        playing = true;

        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void lengthChanged(MediaPlayer mediaPlayer, long newLength) {
                Platform.runLater(() -> updateDuration(newLength));
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("media error");
            }

            //Change volume button icon as volume is changed
            @Override
            public void volumeChanged(MediaPlayer mediaPlayer, float volume) {
                Platform.runLater(() -> setVolumeIcon());
            }

            //When mediaPlayer time changes, update slider value and current time label
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                Platform.runLater(() -> updateSliderPosition(newTime));
            }
        });

        //Bind mediaView width to width of window
        videoImageView.fitWidthProperty().bind(stage.getScene().widthProperty());

        //Bind mediaPlayer volume to volumeSlider
        // mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        volumeSlider.valueProperty().addListener((obs, oldVolume, newVolume) -> {
            mediaPlayer.audio().setVolume(newVolume.intValue());
        });

        //If slider value changes to something different than current mediaPlayer time, update mediaPlayer time
        slider.setOnMouseReleased((e) -> {
            if (slider.getValue() != (double)mediaPlayer.status().time()){
                mediaPlayer.controls().setTime((long)slider.getValue());
            }
            seeking.set(false);
        });
        
        //While dragging the automatic update of slider with time stops (so it doesn't jerk around)
        slider.setOnDragDetected((e) -> {
            seeking.set(true);
        });
    }

    @FXML
    private void playPause() {
        if (playing) {
            mediaPlayer.controls().pause();
            playing = false;
            btnPause.setText(Icon.PLAY);
        } else {
            mediaPlayer.controls().play();
            playing = true;
            btnPause.setText(Icon.PAUSE);
        }
    }

    @FXML
    private void backTen() {
        mediaPlayer.controls().skipTime(-10000);
    }

    @FXML
    private void forwardTen() {
        mediaPlayer.controls().skipTime(10000);
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

    private synchronized void updateSliderPosition(float newTime) {
        if (!seeking.get()) {
            slider.setValue(newTime);
        }
        lblCurrentTime.setText(msToHMS(newTime));
    }

    private synchronized void updateDuration(long newLength) {
        slider.setMax(newLength);
        lblMaxTime.setText(msToHMS(newLength));
    }

    @FXML
    private void exit() {

        saveResumeTime();
        mediaPlayer.release();
                
        //Load home page
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("home.fxml"));
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
        if (mediaPlayer.audio().isMute()) {
            mediaPlayer.audio().setMute(false);
            setVolumeIcon();
        }
        else {
            mediaPlayer.audio().setMute(true);
            btnVolume.setText(Icon.VOLUME_MUTE);
        }
    }

    private synchronized void setVolumeIcon() {
        double volumeValue = volumeSlider.getValue();

        //Set volume button icon based on what the volume is (max is 200)
        if (volumeValue == 0) {
            btnVolume.setText(Icon.VOLUME_MUTE);
        }
        else if (volumeValue > 0 && volumeValue <= 100) {
            btnVolume.setText(Icon.VOLUME_DOWN);
        }
        else {
            btnVolume.setText(Icon.VOLUME_UP);
        }   
    }

    private String msToHMS(double milliseconds) {

        double mins = milliseconds/60000;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        if (mins >= 60) {
            hours = (int)(mins / 60);
            mins -= hours * 60;
        }

        minutes = (int)mins;
        seconds = (int)((mins - minutes) * 60);

        String HMS = String.format("%02d:%02d", minutes, seconds);
        if (hours != 0) {
            HMS = String.format("%02d:"+HMS, hours);
        }

        return HMS;
    }

    private void saveResumeTime() {

        long currentTime = mediaPlayer.status().time();

        //If video at end, reset resume time to 0, if not, save where video is up to and stop it
        if (mediaPlayer.status().state() == State.ENDED) {
            DBController.updateVideoTime(video_id, 0);
        } else {
            DBController.updateVideoTime(video_id, currentTime);
        }

        mediaPlayer.controls().stop();

    }

    private boolean isHoverControls() {
        return volumeGroup.isHover() || btnBackTen.isHover()
            || btnPause.isHover() || btnForwardTen.isHover()
            || btnFullScreen.isHover();
    }

}
