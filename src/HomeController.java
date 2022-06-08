import java.io.File;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class HomeController {

    @FXML
    private VBox videoList;

    @FXML
    private Button addButton;
    
    // private ArrayList<String> videos = new ArrayList<String>();

    @FXML
    private void initialize() {
        
        ArrayList<String> videos = DBController.selectFiles();
        videos.forEach((x) -> {
                videoList.getChildren().add(new Button(x));
            });
    }

    @FXML
    private void addVideo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video file");
        File selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());

        if (selectedFile != null) {
            DBController.insertVideo(selectedFile.getPath(), selectedFile.getPath());
            videoList.getChildren().add(new Button(selectedFile.getPath()));
        }
    }
}
