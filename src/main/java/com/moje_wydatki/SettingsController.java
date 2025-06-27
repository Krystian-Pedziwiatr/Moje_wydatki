package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;


public class SettingsController implements Initializable {



    @FXML
    private Label Amount_wallet;

    @FXML
    private TextField userLogin;

    @FXML
    private  TextField userPassword;

    @FXML
    private Label status_label;

    private ApplicationController Main;

    private MywalletController totalAmount;


    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();

    //Pobranie id_user z sesji
    SessionController session = SessionController.getInstance();
    int userId = session.getUserId();

    public void initialize(URL url, ResourceBundle rb) {



        Main = new ApplicationController();
        totalAmount = new MywalletController();

        //Pobranie id_user z sesji
        SessionController session = SessionController.getInstance();
        int userId = session.getUserId();


            double sumAmount = totalAmount.getTotalAmountForUser(userId);
            Amount_wallet.setText("Stan portfela: " + sumAmount + "zł");

    }



    @FXML
    public void saveTodb(MouseEvent event){

        Connection connection = connectionClass.getConnection();

        String login = userLogin.getText();
        String password = userPassword.getText();
        String query = "UPDATE Login_db SET name = ?, password = ? WHERE id_user = ?";

        try {
            if(!login.isEmpty () || !password.isEmpty()) {


                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, login);
                statement.setString(2, password);
                statement.setDouble(3, userId);
                statement.executeUpdate();


                status_label.setText("Poprawnie zminieniono nazwę użytkownika oraz hasło");


                statement.close();
                connection.close();
            }
            else {
                status_label.setText("Wpisz nowy login i hasło");
            }
        } catch (Exception e) {

            // Wyświetlenie komunikatu o błędzie
            status_label.setText("Błąd: " + e.getMessage());
        }

    }



    @FXML
    public void budgetOnClick(MouseEvent event) {

        try {
            Main.switchToMainScene("application-view-budget.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void MyWalletOnClick(MouseEvent event) {

        try {
            Main.switchToMainScene("application-view-mywallet.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ExpensesOnClick(MouseEvent event) {

        try {
            Main.switchToMainScene("application-view-expenses.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void SettingsOnClick(MouseEvent event) {

        try {
            Main.switchToMainScene("application-view-settings.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



















