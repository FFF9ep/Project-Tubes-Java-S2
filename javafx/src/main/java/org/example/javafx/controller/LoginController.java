package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.example.javafx.MainApp;
import org.example.javafx.Member;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField nimInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button loginButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
            String nim = nimInput.getText();
            String password = passwordInput.getText();
            loginMember(Integer.parseInt(nim), password);
        });

        backButton.setOnAction(event -> {
            try {
                MainApp.showWelcomeView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loginMember(long nim, String password) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        Member member = new Member(null, nim, password);
        String json = gson.toJson(member);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("Login gagal.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Login berhasil.");
                } else {
                    System.out.println("Login gagal. Response Code: " + response.code());
                }
            }
        });
    }
}
