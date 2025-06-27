package com.moje_wydatki;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class ApplicationController extends javafx.application.Application {

    public static Stage stg;

    @Override
    public void start(Stage primaryStage) throws IOException {

        stg = primaryStage;


        // Wczytaj scneę logowania z pliku fxml
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));

        primaryStage.setTitle("Logowanie");
        primaryStage.setScene(new Scene(root, 1200,600));
        primaryStage.show();

    }

    public void switchToMainScene(String fxml) throws IOException{

//        // Zmiana głównej sceny aplikacji

        Parent pane = FXMLLoader.load((getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
        stg.setTitle("Moje wydatki");



    }


    public static void main(String[] args) {
        ApplicationController.launch(args);
    }
}