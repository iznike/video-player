import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private VBox lolomo;

    @FXML
    private Button addButton;

    private LolomoRow videoList;

    @FXML
    public void initialize() {

        videoList = new LolomoRow();
        lolomo.getChildren().clear();

        ArrayList<MovieTile> tiles = DBController.selectVideosAsTiles();
        tiles.forEach((x) -> {
                x.setOnPlay((e) -> {
                    playVideo((MovieTile) e.getSource());
                });
                videoList.addChildren(x);
            });

        //Only show lolomo row if it isn't empty
        if (!videoList.isEmpty()) {
            lolomo.getChildren().add(videoList);
        }
    }

    @FXML
    private void addVideo() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("new_video_dialog.fxml"));
            Stage dialog = new Stage();
            dialog.setTitle("New Video");
            dialog.setScene(new Scene(loader.load()));
            dialog.setResizable(false);
            dialog.showAndWait();
            initialize();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void playVideo(MovieTile tile) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("player.fxml"));
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
