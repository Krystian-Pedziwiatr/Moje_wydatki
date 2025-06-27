package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;


public class ExpensesController implements Initializable {



    private ApplicationController Main;

    private MywalletController totalAmount;

    @FXML
    private LineChart<String, Number> barChart;

    @FXML
    private Label Amount_wallet;


    @FXML
    private Label status_label;

    @FXML
    private TextField amount;

    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] category = {"Żywność", "Paliwo", "Ciuchy, kosmetyki","Rozrywka", "Inne wydatki"};


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

        choiceBox.getItems().addAll(category);
        choiceBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return (s == null) ? "Wybierz kategorię" : s;
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });



    }


    @FXML
    public String getDataFromCategory(){

        return choiceBox.getValue();
    }

    @FXML
    public void saveAmount(MouseEvent event){

        String Amount = amount.getText();
        String category = getDataFromCategory();
        double formattedamound;

        String query = "INSERT INTO expenses (id_user, amount, date, category) VALUES (?, ?, CURDATE(), ?)";



        //Tworzenie połączenia z bazą danych
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();


        // Sprawdzenie czy oba pola nie są puste
        if(category == null){
            status_label.setText("Wybierz kategorię");

            if(Amount.isEmpty()){
                status_label.setText("Wpisz kwotę oraz wybierz kategorię");
            }
            return;
        }

        //Sprawdzenie czy da się przekonwertować jeśli nie wprowadzono string
        try{
            formattedamound = Double.parseDouble(Amount);
        }
        catch (NumberFormatException e){
            status_label.setText("Wprowadź poprawną kwotę");
            return ;
        }


        try{ PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setDouble(2, formattedamound);
            statement.setString(3, category);
            statement.executeUpdate();
            status_label.setText("Poprawnie wprowadzono kwotę do bazy danych!");
            statement.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
            status_label.setText("Bląd zapisu!");
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



















