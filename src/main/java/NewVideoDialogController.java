import java.io.File;
import java.net.URL;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class NewVideoDialogController {
    
    @FXML
    Button btnChooseVideo;

    @FXML
    Button btnChooseImage;

    @FXML
    Button btnDelete;

    @FXML
    TextField url;

    @FXML
    TextField title;

    @FXML
    TextField poster;

    private int id = -1;
    private String initialUrl;
    private String initialTitle;
    private String initialPoster;

    //Todo list:
    //url for video
    //name of video
    //video group
    //url for poster

    @FXML
    private void initialize() {

        btnChooseVideo.setFont(Icon.fontAwesome18);
        btnChooseImage.setFont(Icon.fontAwesome18);

        btnChooseVideo.setText(Icon.FILE_VIDEO);
        btnChooseImage.setText(Icon.FILE_IMAGE);
    }

    public void populate(int id, String url, String title, String poster) {
        this.url.setText(url);
        this.title.setText(title);
        this.poster.setText(poster);

        this.id = id;
        initialUrl = url;
        initialTitle = title;
        initialPoster = poster;

        btnDelete.setVisible(true);
    }

    @FXML
    private void ChooseVideo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video file");
        File selectedFile = fileChooser.showOpenDialog(btnChooseVideo.getScene().getWindow());

        if (selectedFile != null) {
            url.setText(selectedFile.getPath());
        }
    }

    @FXML
    private void ChooseImage() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image file");
        File selectedFile = fileChooser.showOpenDialog(btnChooseImage.getScene().getWindow());

        if (selectedFile != null) {
            poster.setText(selectedFile.getPath());
        }
    }

    @FXML
    private void Save() {
        
        if (validateVideo() & validateImage() & validateTitle()) {
            if (id == -1) {
                //Insert
                id = DBController.insertVideo(title.getText(), url.getText(), poster.getText());
                btnDelete.setVisible(true);
            } else {
                //Update what's been changed
                if (!url.getText().equals(initialUrl)) {
                    DBController.updateVideoFile(id, url.getText());
                }
                if (!title.getText().equals(initialTitle)) {
                    DBController.updateVideoTitle(id, title.getText());
                }
                if (!poster.getText().equals(initialPoster)) {
                    DBController.updateVideoPoster(id, poster.getText());
                }
            }

            initialUrl = url.getText();
            initialTitle = title.getText();
            initialPoster = poster.getText();

            close();
        }
    }

    private void close() {
        Stage stage = (Stage)url.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Delete() {
        //Ask user if they are sure before deleting
        Alert deleteAlert = new Alert(AlertType.WARNING, "Are you sure you want to delete this video?", ButtonType.YES, ButtonType.CANCEL);
        Optional<ButtonType> option = deleteAlert.showAndWait();

        if (option.get() == ButtonType.YES) {
            DBController.deleteVideo(id);
        }

        close();
    }

    private boolean validateVideo() {
        try {
            new URL(url.getText()).toURI();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Video URL is not valid.").show();
            return false;
        }
        return true;
    }

    private boolean validateImage() {
        if (poster.getText().isBlank()) {
            new Alert(AlertType.ERROR, "Poster URL is not valid.").show();
            return false;
        }
        Image image = new Image(poster.getText());
        if (image.isError()) {
            new Alert(AlertType.ERROR, "Poster URL is not valid.").show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateTitle() {
        if (title.getText().isBlank()) {
            new Alert(AlertType.ERROR, "Title cannot be blank.").show();
            return false;
        } else {
            return true;
        }
    }
}
