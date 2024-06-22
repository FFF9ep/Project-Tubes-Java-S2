package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.javafx.MainApp;

public class WelcomeController {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
            try {
                MainApp.showLoginView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        registerButton.setOnAction(event -> {
            try {
                MainApp.showRegisterView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
