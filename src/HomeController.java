import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
                Button b = new Button(x);
                b.setOnAction((e) -> {
                    playVideo((Button) e.getSource());
                });
                videoList.getChildren().add(b);
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

    private void playVideo(Button b) {

        try {
            FXMLLoader loader = new FXMLLoader(new File("src/player.fxml").toURI().toURL());
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(scene);

            File file = new File(b.getText());
            Media media = new Media(file.toURI().toString());
            PlayerController controller = loader.getController();
            controller.loadVideo(b.getText(), media);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        
    }
}
