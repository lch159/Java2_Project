package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;

/**
 * This application is used to query earthquake data through three ways: csv file, database and web scraping. <br>
 * Users can sort those searched earthquake data with date, longitude and latitude, depth, intensity and plate.<br>
 * The searched earthquake data will be shown in the form of a table and a map.
 *
 * @author lichenhao
 * @author linsen
 * @author zhengxiaodian
 * @version 1.00
 * @see #start(Stage)
 * @see Earthquake
 * @see Controller_SeniorQuery
 */
public class Main extends Application {

    /**
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SeniorQuery.fxml"));
        Scene scene = new Scene(root, root.getLayoutX(), root.getLayoutY());
        primaryStage.setTitle("Earthquake Query");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.setWidth(1200);
        primaryStage.setHeight(1000);
        primaryStage.show();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
