package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SeniorQuery.fxml"));
        primaryStage.setTitle("Earthquake Query");
        primaryStage.setScene(new Scene(root, root.getLayoutX(), root.getLayoutY()));
        primaryStage.setResizable(false);


        primaryStage.setWidth(1200);
        primaryStage.setHeight(1100);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
