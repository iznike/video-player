import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MovieTile extends StackPane {
    
    private int video_id;
    private String title;
    private File file;
    private double resume_time;

    // private Label lblTitle;
    // private Button btnPlay;
    // private HBox buttons;
    private VBox overlay;

    private EventHandler<ActionEvent> onPlay;

    public MovieTile(int video_id, String title, String path, double resume_time) {

        this.video_id = video_id;
        this.title = title;
        file = new File(path);
        this.resume_time = resume_time;

        setHeight(300);

        //Create ImageView
        String urlString = "";
        try {
            URI uri = new URI("https://www.themoviedb.org/t/p/original/cFcZYgPRFZdBkA7EsxHz5Cb8x5.jpg");
            urlString = uri.toURL().toString();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        ImageView imageView = new ImageView(urlString);
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(heightProperty().divide(1.5));
        imageView.setCursor(Cursor.HAND);

        //Set up buttons
        Button btnPlay = new Button(Icon.PLAY);
        btnPlay.setFont(Icon.fontAwesome12);
        btnPlay.setTextFill(Paint.valueOf("white"));
        btnPlay.setStyle("-fx-background-color: transparent");
        btnPlay.setCursor(Cursor.HAND);
        btnPlay.setOnAction((e) -> {
            onPlay.handle(new ActionEvent(this, e.getTarget()));
        });

        Button btnEdit = new Button(Icon.EDIT);
        btnEdit.setFont(Icon.fontAwesome12);
        btnEdit.setTextFill(Paint.valueOf("white"));
        btnEdit.setStyle("-fx-background-color: transparent");
        btnEdit.setCursor(Cursor.HAND);
        
        HBox buttons = new HBox(10, btnPlay, btnEdit);

        //Set up title label
        Label lblTitle = new Label(title);
        lblTitle.setTextFill(Paint.valueOf("white"));
        lblTitle.setWrapText(true);
        lblTitle.maxWidthProperty().bind(imageView.fitWidthProperty().add(-20));

        //Set up overlay
        VBox overlayContent = new VBox(10, buttons, lblTitle);
        overlayContent.setPadding(new Insets(10));
        overlayContent.setStyle("-fx-background-color: #181818");
        overlay = new VBox(overlayContent);
        overlay.setAlignment(Pos.BOTTOM_CENTER);
        overlay.setVisible(false);
        
        getChildren().addAll(imageView, overlay);

        //Event handling
        setOnMouseClicked((e) -> {
            onPlay.handle(new ActionEvent(this, e.getTarget()));
        });

        setOnMouseEntered((e) -> {
            popOut();
        });

        setOnMouseExited((e) -> {
            //Go back into place
            // setHeight(getHeight() - 15);
            // setWidth(getHeight()/1.5);

            // getChildren().remove(1);

            // getLolomoRow().unfreeze();

            // buttons.setVisible(false);

            overlay.setVisible(false);
        });
        
    }

    private void popOut() {

        // getLolomoRow().freeze();
        
        // setHeight(getHeight() + 15);
        // setWidth(getHeight());

        // Button btnEdit = new Button();
        // VBox buttons = new VBox(btnEdit);

        // getChildren().add(buttons);

        // buttons.setPrefHeight(getHeight());
        // buttons.setPrefWidth(getWidth()/2);
        // buttons.setVisible(true);

        overlay.setVisible(true);
        
    }

    private LolomoRow getLolomoRow() {
        return (LolomoRow) getParent().getParent().getParent().getParent().getParent().getParent();
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

    // public Label getLabel() {
    //     return lblTitle;
    // }

    // public Button getButton() {
    //     return btnPlay;
    // }

    public void setOnPlay(EventHandler<ActionEvent> value) {
        onPlay = value;
    }

}
