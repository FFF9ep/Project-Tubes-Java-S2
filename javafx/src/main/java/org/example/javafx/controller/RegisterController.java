package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import okhttp3.*;
import org.example.javafx.MainApp;
import org.example.javafx.Member;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField nameInput;
    @FXML
    private TextField nimInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        registerButton.setOnAction(event -> {
            String namaLengkap = nameInput.getText();
            long nim = Long.parseLong(nimInput.getText());
            String password = passwordInput.getText();
            registerMember(namaLengkap, nim, password);
        });

        backButton.setOnAction(event -> {
            try {
                MainApp.showWelcomeView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void registerMember(String namaLengkap, long nim, String password) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        Member member = new Member(namaLengkap, nim, password);
        String json = gson.toJson(member);

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url("http://localhost:5000/api/reg-member")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("Registrasi gagal.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("Registrasi berhasil.");
                } else {
                    System.out.println("Registrasi gagal. Response Code: " + response.code());
                }
            }
        });
    }
}
