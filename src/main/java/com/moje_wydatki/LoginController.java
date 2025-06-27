package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class LoginController {


    @FXML
    private Label loginMessageLabel;

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
        String verifyPassword = "SELECT `id_user`,`password` FROM `Login_db` WHERE `name` = ?";
        String password = null;


        try {
            if(!loginFromUser.isEmpty () || !passwordFromUser.isEmpty()) {


                PreparedStatement statement = connection.prepareStatement(verifyPassword);
                statement.setString(1, loginFromUser);
                ResultSet queryOutput = statement.executeQuery();

                if (queryOutput.next()) {

                    //Pobranie z bazy danych pola hasło
                    password = queryOutput.getString("password");
                    //Pobranie z bazy danych pola id_user
                    int userId = queryOutput.getInt("id_user");

                    if (password.equals(passwordFromUser)) {


                        //Ustawianie sesji użykownika o danym id
                        SessionController.getInstance().setUserId(userId);

                        //Przechodzenie do głównej sceny aplikacji
                        Main.switchToMainScene("application-view.fxml");



                        loginMessageLabel.setText("Yes");
                    } else {
                        loginMessageLabel.setText("Niepoprawne hasło");
                    }

                } else {
                    loginMessageLabel.setText("Nie znaleziono użytkownika");
                }


                // Zamykamy połączenie z bazą danych
                queryOutput.close();
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
    public void RegisterButtonOnAction(ActionEvent event) throws IOException {

        ApplicationController Main = new ApplicationController();
        Main.switchToMainScene("register-view.fxml");
    }


}