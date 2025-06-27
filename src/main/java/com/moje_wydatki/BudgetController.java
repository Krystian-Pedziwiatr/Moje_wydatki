package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class BudgetController implements Initializable {



    @FXML
    private Label Amount_wallet;


    @FXML
    private TextField Amount1;

    @FXML
    private Label status_label;

    @FXML
    private TextField monthly_amount;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button saveRaport;

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

        //Ukrycie paska posępu pobierania raportu
        progressBar.setVisible(false);


        //Pobranie id_user z sesji
        SessionController session = SessionController.getInstance();
        int userId = session.getUserId();

        //Dodanie satnu portfela
        double sumAmount = totalAmount.getTotalAmountForUser(userId);
        Amount_wallet.setText("Stan portfela: " + sumAmount + "zł");


    }

    @FXML
    public void SaveAmountOnClick(ActionEvent event) {

        String amount = Amount1.getText();

        //Tworzenie połączenia z bazą danych
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        if(amount.isEmpty()){
            status_label.setText("Wpisz kwotę");
            return;
        }
        double UserAmount;

        try{
            UserAmount = Double.parseDouble(amount);
        }
        catch (NumberFormatException e){
            status_label.setText("Wprowadź poprawną kwotę");
            return ;
        }

        String query = "INSERT INTO income_money (id_user, amount, date, solary) VALUES (?, ?, CURDATE(), 0)";

        try{ PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setDouble(2, UserAmount);
            statement.executeUpdate();
            status_label.setText("Poprawnie wprowadzono kwotę do bazy danych!");

        }
        catch (Exception e){
            e.printStackTrace();
            status_label.setText("Bląd zapisu!");
        }

    }


    public void SaveMonthlyAmount(ActionEvent Event){

        String amount = monthly_amount.getText();

        String query = "INSERT INTO income_money (id_user, amount, date, solary) VALUES (?, ?, CURDATE(), 1)";



        //Tworzenie połączenia z bazą danych
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        if(amount.isEmpty()){
            status_label.setText("Wpisz kwotę");
            return;
        }
        double MonthlyAmount;

        try{
            MonthlyAmount = Double.parseDouble(amount);
        }
        catch (NumberFormatException e){
            status_label.setText("Wprowadź poprawną kwotę");
            return ;
        }



        try{ PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setDouble(2, MonthlyAmount);
            statement.executeUpdate();
            status_label.setText("Poprawnie wprowadzono kwotę do bazy danych!");

        }
        catch (Exception e){
            e.printStackTrace();
            status_label.setText("Bląd zapisu!");
        }

    }

    @FXML
    public void SaveBudgetRaport(){

        String query = "SELECT * FROM income_money WHERE id_user = ?";

        //Tworzenie połączenia z bazą danych
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            //Połączenie z bazą danych i wykonanie zapytania
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            // Tworzenie akrkusza za pomocą biblioteki Maven
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Raport budżet");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Kwota");
            headerRow.createCell(2).setCellValue("Data");

            int rowIndex = 1;

            // Dodawanie danych w pętli do arkusza
            while(resultSet.next()){

                progressBar.setVisible(true);
                handleStart();
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(resultSet.getInt("id"));
                row.createCell(1).setCellValue(resultSet.getDouble("amount"));
                row.createCell(2).setCellValue(resultSet.getDate("date").toString());
            }
            // Okno dialogowe do wyboru lokalizacji zapisu pliku
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Raport", "Raport.xlsx"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    status_label.setText("Plik Excel został zapisany pomyślnie.");
                } catch (IOException e) {
                    status_label.setText("Błąd podczas zapisywania pliku.");
                    e.printStackTrace();
                }
            }


            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            status_label.setText("Błąd podczas generowania raportu.");
        }
    }


    public void handleStart() {
        // Wyłączenie przycisku, aby uniknąć wielokrotnego uruchomienia
        saveRaport.setDisable(true);

        // Utworzenie zadania
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // Symulowanie zadania
                for (int i = 0; i <= 30; i++) {
                    Thread.sleep(30); // symulacja czasu przetwarzania
                    updateProgress(i, 15);
                }
                return null;

            }
        };

        // Ustawienie paska postępu do aktualizacji na podstawie zadania
        progressBar.progressProperty().bind(task.progressProperty());

        // Dodanie reakcji na zakończenie zadania
        task.setOnSucceeded(e -> saveRaport.setDisable(false));
        task.setOnFailed(e -> saveRaport.setDisable(false));

        // Uruchomienie zadania w nowym wątku
        new Thread(task).start();
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



















