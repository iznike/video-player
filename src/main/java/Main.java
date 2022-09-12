import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class Main extends Application{   

    @Override
    public void start(Stage stage) throws IOException{
        DBController.createDB();
        Icon.loadFonts();

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("home.fxml"));
        Scene scene = new Scene(loader.load());
        scene.setUserData(loader.getController());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
