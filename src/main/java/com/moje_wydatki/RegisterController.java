package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class RegisterController {

    @FXML
    private Button exit_button;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Label error_label;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;


    
    public void LoginButtonOnAction(ActionEvent event)
    {

        ApplicationController Main = new ApplicationController();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        String loginFromUser = usernameTextField.getText();
        String passwordFromUser = passwordField.getText();
        String verifyPassword = "INSERT INTO Login_db (`name`, `password`) VALUES ( ?, ?)";


        try {
            if(!loginFromUser.isEmpty () || !passwordFromUser.isEmpty()) {

                if(loginFromUser.isEmpty()){
                    loginMessageLabel.setText("Wpisz nazwę użytkownika");
                    return;
                }
                if(passwordFromUser.isEmpty()){
                    loginMessageLabel.setText("Wpisz hasło");
                    return;
                }

                PreparedStatement statement = connection.prepareStatement(verifyPassword);
                statement.setString(1, loginFromUser);
                statement.setString(2, passwordFromUser);
                statement.executeUpdate();

                loginMessageLabel.setText("Poprawnie utworzono twoje konto, zaraz zostaniesz przekierowany na stronę logowania ");

                PauseTransition pause = new PauseTransition(Duration.seconds(5));

                pause.setOnFinished(e -> {
                    try{
                        Main.switchToMainScene("login-view.fxml");
                    }
                    catch (IOException ex){
                        ex.printStackTrace();
                    }
                });
                pause.play();




                // Zamykamy połączenie z bazą danych
                statement.close();
                connection.close();


            }
            else {
                loginMessageLabel.setText("Wpisz login i hasło");
            }



        } catch (Exception e) {

            // Wyświetlenie komunikatu o błędzie
            loginMessageLabel.setText("Błąd: " + e.getMessage());
        }

    }


    @FXML
    public void SwitchToLoginOnAction(ActionEvent event) throws IOException {
        ApplicationController Main = new ApplicationController();

        Main.switchToMainScene("login-view.fxml");
    }

    public void exit_buttonOnAction(ActionEvent e) {
        Stage stage = (Stage) exit_button.getScene().getWindow();
        stage.close();
    }


}