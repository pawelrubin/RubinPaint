package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    public static Stage primaryStage;
    
    @Override
    public void start(final Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("MacynPaint2137");
        primaryStage.setScene(new Scene(root, 1600, 400));
        //primaryStage.setResizable();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
