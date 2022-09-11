import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class MovieTile extends StackPane {
    
    private int video_id;
    private String title;
    private File file;
    private double resume_time;
    private String poster_path;

    private VBox overlay;

    private EventHandler<ActionEvent> onPlay;

    public MovieTile(int video_id, String title, String path, String poster_path, double resume_time) {

        this.video_id = video_id;
        this.title = title;
        file = new File(path);
        this.resume_time = resume_time;
        this.poster_path = poster_path;

        setHeight(300);

        //Create ImageView
        Image poster = new Image(poster_path);
        ImageView imageView = new ImageView(poster);
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(heightProperty().divide(1.5));
        imageView.setCursor(Cursor.HAND);

        //Set up buttons
        Button btnPlay = new Button(Icon.PLAY);
        btnPlay.setFont(Icon.fontAwesome12);
        btnPlay.setOnAction((e) -> {
            onPlay.handle(new ActionEvent(this, e.getTarget()));
        });

        Button btnEdit = new Button(Icon.EDIT);
        btnEdit.setFont(Icon.fontAwesome12);
        btnEdit.setOnAction((e) -> {
            try {
                FXMLLoader loader = new FXMLLoader(new File("src/new_video_dialog.fxml").toURI().toURL());
                Stage dialog = new Stage();
                dialog.setTitle("Edit Video");
                dialog.setScene(new Scene(loader.load()));
                dialog.setResizable(false);

                NewVideoDialogController controller = loader.getController();
                controller.populate(video_id, file.getPath(), title, poster_path);

                dialog.showAndWait();
                HomeController homeController = (HomeController)getScene().getUserData();
                homeController.initialize();
    
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
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
            overlay.setVisible(false);
        });
        
    }

    private void popOut() {
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

    public void setOnPlay(EventHandler<ActionEvent> value) {
        onPlay = value;
    }

}
