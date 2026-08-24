package it.unicam.cs.mpgc.rpg126598;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("fxml/first-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("DemoRPG");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
