module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;


    opens org.example.javafx to javafx.fxml, com.google.gson;
    exports org.example.javafx;
    exports org.example.javafx.controller;
    opens org.example.javafx.controller to com.google.gson, javafx.fxml;
}