import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application{
    
    

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(new File("src/player.fxml").toURI().toURL());
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
