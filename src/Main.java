import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application{
    
    

    @Override
    public void start(Stage stage) throws IOException{
        DBController.createDB();

        Parent root = FXMLLoader.load(new File("src/home.fxml").toURI().toURL());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
