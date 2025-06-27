package com.moje_wydatki;

import connect_db.ConnectionClass;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;


public class ViewController {


        @FXML
        private Label welcome;

        private int userId;
        private Connection connection;
        private SessionController session;
        private ApplicationController Main;


        @FXML
        public void initialize() {
                session = SessionController.getInstance();
                userId = session.getUserId();

                ConnectionClass connectionClass = new ConnectionClass();
                connection = connectionClass.getConnection();

                Main = new ApplicationController();

                String query = "SELECT name FROM Login_db WHERE id_user = ?";
                try {
                        PreparedStatement statement = connection.prepareStatement(query);
                        statement.setInt(1, userId);
                        try(ResultSet name = statement.executeQuery()){
                                if(name.next()){
                                        String user = name.getString("name");
                                        welcome.setText("Witaj ponownie " + user + "!");
                                }
                        }


                }
                catch (Exception e){
                        e.printStackTrace();
                }


        }
        public void handleLabelClickMyWallet(MouseEvent event) {
                try {
                        Main.switchToMainScene("application-view-mywallet.fxml");
                } catch (IOException e) {
                        e.printStackTrace();

                }
        }

        public void handleLabelClickBudget(MouseEvent event){
                try {
                        Main.switchToMainScene("application-view-budget.fxml");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void handleLabelClickExpenses(MouseEvent event){
                try {
                        Main.switchToMainScene("application-view-expenses.fxml");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void handleLabelClickSettings(MouseEvent event){
                try {
                        Main.switchToMainScene("application-view-settings.fxml");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }







}
