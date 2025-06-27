package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;



public class MywalletController implements Initializable {


        private ApplicationController Main;



        @FXML
        private LineChart<String, Number> barChart;

        @FXML
        private Label Amount_wallet, status_label;

        @FXML
        private PieChart pieChart;



        //Tworzenie połączenia z bazą danych
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        //Pobranie id_user z sesji
        SessionController session = SessionController.getInstance();
        int userId = session.getUserId();

        public void initialize(URL url, ResourceBundle rb) {

            Main = new ApplicationController();

            loadPieChartData();
            Amount_wallet.setText("Stan portfela: " + getTotalAmountForUser(userId) + "zł");
            status_label.setText("Miesięczny bilnas twojego porfela to: " + monthlyBalance() + "zł");
            //Tworzenie połączenia z bazą danych
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();
            //Tworzenie zapytania SQL
            String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total_amount  FROM income_money  WHERE id_user = ?  GROUP BY month  ORDER BY month ";


            // Tworzenie serii danych
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Przychody");

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();




                while (resultSet.next()) {

                    String month = resultSet.getString("month");
                    double totalAmount = resultSet.getDouble("total_amount");

                    //Wyciąganie miesiąca z daty w bazie danych

                    series.getData().add(new XYChart.Data<>(month, totalAmount));

                }

                barChart.getData().add(series);


                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }





        }

    // Metoda, która oblicza łączną kwotę dla danego użytkownika
    public double getTotalAmountForUser(int userId) {


        //Tworzenie zapytania SQL
        String query = "SELECT " +
                "(SELECT (SUM(amount)) FROM income_money WHERE id_user = ?) AS total_income, " +
                "(SELECT (SUM(amount)) FROM expenses WHERE id_user = ?) AS total_expenses";


        double totalBalance = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {

                double totalIncome = resultSet.getDouble("total_income");
                double totalExpenses = resultSet.getDouble("total_expenses");
                totalBalance = totalIncome - totalExpenses;

            }


            resultSet.close();
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBalance;
    }


    @FXML
    public void loadPieChartData(){

            String query = "SELECT category, SUM(amount) AS total_amount FROM expenses WHERE id_user = ? GROUP BY category;";

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()){
                    String category = resultSet.getString("category");
                    Double totalAmount = resultSet.getDouble("total_amount");

                    PieChart.Data slice = new PieChart.Data(category, totalAmount);
                    pieChart.getData().add(slice);
                    pieChart.setTitle("Wydatki");
                }

                resultSet.close();
                statement.close();
            }
            catch (Exception e){
                e.printStackTrace();

            }
    }

    private double monthlyBalance(){

        double totalIncome = 0;
        double totalExpenses = 0;
        double totalMonthly = 0;

        // Tworzenie zapytania SQL, aby obliczyć sumy dla bieżącego miesiąca
        String query = "SELECT " +
                "(SELECT SUM(amount) FROM income_money WHERE id_user = ? AND MONTH(DATE) = MONTH(CURRENT_DATE()) AND YEAR(DATE) = YEAR(CURRENT_DATE())) AS total_income, " +
                "(SELECT SUM(amount) FROM expenses WHERE id_user = ? AND MONTH(DATE) = MONTH(CURRENT_DATE()) AND YEAR(DATE) = YEAR(CURRENT_DATE())) AS total_expenses";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("total_income");
                totalExpenses = resultSet.getDouble("total_expenses");
                totalMonthly = totalIncome - totalExpenses;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalMonthly;
    }

    private double mostSpent(){

        double totalIncome = 0;
        double totalExpenses = 0;
        double totalMonthly = 0;

        // Tworzenie zapytania SQL, aby obliczyć sumy dla bieżącego miesiąca
        String query = "SELECT " +
                "(SELECT SUM(amount) FROM income_money WHERE id_user = ? AND MONTH(DATE) = MONTH(CURRENT_DATE()) AND YEAR(DATE) = YEAR(CURRENT_DATE())) AS total_income, " +
                "(SELECT SUM(amount) FROM expenses WHERE id_user = ? AND MONTH(DATE) = MONTH(CURRENT_DATE()) AND YEAR(DATE) = YEAR(CURRENT_DATE())) AS total_expenses";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalIncome = resultSet.getDouble("total_income");
                totalExpenses = resultSet.getDouble("total_expenses");
                totalMonthly = totalIncome - totalExpenses;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalMonthly;
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



















