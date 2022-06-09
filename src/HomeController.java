import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private VBox videoList;

    @FXML
    private Button addButton;

    @FXML
    private void initialize() {

        ArrayList<String> videos = DBController.selectFiles();
        videos.forEach((x) -> {
                MovieTile tile = new MovieTile(0, x, x, 0);
                tile.setOnPlay((e) -> {
                    playVideo((MovieTile) e.getSource());
                });
                videoList.getChildren().add(tile);
            });
    }

    @FXML
    private void addVideo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video file");
        File selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());

        if (selectedFile != null) {
            DBController.insertVideo(selectedFile.getPath(), selectedFile.getPath());
            MovieTile tile = new MovieTile(0, selectedFile.getName(), selectedFile.getPath(), 0);
                tile.setOnPlay((e) -> {
                    playVideo((MovieTile) e.getSource());
                });
                videoList.getChildren().add(tile);
        }
    }

    private void playVideo(MovieTile tile) {

        try {
            FXMLLoader loader = new FXMLLoader(new File("src/player.fxml").toURI().toURL());
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(scene);

            PlayerController controller = loader.getController();
            controller.loadVideo(tile.getTitle(), tile.getMedia());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
