import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private VBox lolomo;

    @FXML
    private Button addButton;

    private LolomoRow videoList;

    @FXML
    private void initialize() {

        videoList = new LolomoRow();
        videoList.setTitle("test row");
        lolomo.getChildren().add(videoList);

        ArrayList<MovieTile> tiles = DBController.selectVideosAsTiles();
        tiles.forEach((x) -> {
                x.setOnPlay((e) -> {
                    playVideo((MovieTile) e.getSource());
                });
                // videoList.getChildren().add(x);
                videoList.addChildren(x);
            });
    }

    @FXML
    private void addVideo() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select video file");
        File selectedFile = fileChooser.showOpenDialog(addButton.getScene().getWindow());

        if (selectedFile != null) {
            int id = DBController.insertVideo(selectedFile.getPath(), selectedFile.getPath());
            MovieTile tile = new MovieTile(id, selectedFile.getName(), selectedFile.getPath(), 0);
                tile.setOnPlay((e) -> {
                    playVideo((MovieTile) e.getSource());
                });
                // videoList.getChildren().add(tile);
                videoList.addChildren(tile);
        }
    }

    private void playVideo(MovieTile tile) {

        try {
            FXMLLoader loader = new FXMLLoader(new File("src/player.fxml").toURI().toURL());
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.setScene(scene);

            PlayerController controller = loader.getController();
            controller.loadVideo(tile.getVideoID(), tile.getTitle(), tile.getMedia(), tile.getResumeTime());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
