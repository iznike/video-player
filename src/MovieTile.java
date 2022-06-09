import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.util.Duration;

public class MovieTile extends HBox {
    
    private int video_id;
    private String title;
    private File file;
    private double resume_time;

    private Label lblTitle;
    private Button btnPlay;

    private EventHandler<ActionEvent> onPlay;

    public MovieTile(int video_id, String title, String path, double resume_time) {

        this.video_id = video_id;
        this.title = title;
        file = new File(path);
        this.resume_time = resume_time;

        lblTitle = new Label(title);
        btnPlay = new Button();
        btnPlay.setPrefSize(20, 20);
        btnPlay.setOnAction((e) -> {
            onPlay.handle(new ActionEvent(this, e.getTarget()));
        });

        HBox hbox = new HBox(lblTitle);
        hbox.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(hbox, btnPlay);
        setAlignment(Pos.CENTER);
        setHgrow(hbox, Priority.ALWAYS);
        
    }

    public int getVideoID() {
        return video_id;
    }

    public String getTitle() {
        return title;
    }

    public Media getMedia() {
        return new Media(file.toURI().toString());
    }

    public Duration getResumeTime() {
        return Duration.minutes(resume_time);
    }

    public Label getLabel() {
        return lblTitle;
    }

    public Button getButton() {
        return btnPlay;
    }

    public void setOnPlay(EventHandler<ActionEvent> value) {
        onPlay = value;
    }

}
