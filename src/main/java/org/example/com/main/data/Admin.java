package org.example.com.main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage; // ini adalah java API
import org.example.com.main.Main;
import org.example.com.main.UI.PropertyBook;
import org.example.com.main.UI.PropertyMahasiswa;
import org.example.com.main.UI.UIManager;
import org.example.com.main.books.*;
import org.example.com.main.util.IMenu;
import org.example.com.main.exception.illegalAdminAcces;

import java.io.IOException;
import java.util.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Admin extends User implements IMenu { // ini adalah class dan menerapkan konsep OOP
    private TableView tableMahasiswa = new TableView<>();
    private final String adminUserName = "admin";
    private final String adminPassword = "admin";
    private static final ArrayList<Mahasiswa> MAHASISWA_DATA = new ArrayList<>();// ini adalah arraylist
    private static final ArrayList<String> mahasiswaList = new ArrayList<>();
    private static ArrayList<Book> borrowedBooks = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        borrowedBooks.add(book);
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public static void logIn(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Log In Admin");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label userName = new Label("Username:");
        grid.add(userName, 1, 1); // Kolom 0, Baris 1

        TextField inputUserName = new TextField();
        inputUserName.setPromptText("Masukkan username");
        grid.add(inputUserName, 1, 2); // Kolom 1, Baris 1

        Label password = new Label("Password : ");
        grid.add(password, 1, 3);

        PasswordField inputPassword = new PasswordField();
        inputPassword.setPromptText("Masukkan password");
        grid.add(inputPassword, 1, 4);

        Button btnSignIn = new Button("Login");
        Button btnBack = new Button("Kembali");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSignIn);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSignIn.setOnAction(actionEvent -> {
            if (inputUserName.getText().isEmpty() || inputPassword.getText().isEmpty()) {
                UIManager.showError(actionTarget, "Username atau password tidak boleh kosong");
            } else {
                Admin admin = new Admin();
                try {
                    boolean isValid = admin.isAdmin(inputUserName.getText(), inputPassword.getText());
                    admin.menu(stage);
                } catch (illegalAdminAcces e) {
                    UIManager.showError(actionTarget, e.getMessage());
                }
            } // ini adalah decision statement
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, 820, 420);
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("LOGIN ADMIN");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void menu(Stage stage) { // ini adalah isi dari javafx
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Admin Menu");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Button btnaddMahasiswa = new Button("Tambah Data Mahasiswa");
        Button btnAddBook = new Button("Tambah Buku Baru");
        Button btnDisplayMahasiswa = new Button("Tampilkan Daftar Mahasiswa");
        Button btnDisplayBook = new Button("Tampilkan Daftar Buku");
        Button btnEditBook = new Button("Edit Buku");
        Button btnDenda = new Button("Denda Buku");
        Button btnLogOut = new Button("Keluar");

        double width = 50;
        double height = 50;
        VBox conAddMahasiswa = new VBox(10);
        Label labelAddMahasiswa = new Label("Gunakan menu ini untuk \nmenambahkan mahasiswa");
        labelAddMahasiswa.setId("paragraph");
        conAddMahasiswa.setAlignment(Pos.CENTER);
        ImageView imageViewStudent = new ImageView(new Image(Main.class.getResourceAsStream("img/student.png")));
        imageViewStudent.setFitWidth(width);
        imageViewStudent.setFitHeight(height);
        conAddMahasiswa.getChildren().addAll(imageViewStudent, labelAddMahasiswa, btnaddMahasiswa);
        grid.add(conAddMahasiswa, 1, 3);

        VBox conAddBook = new VBox(10);
        Label labelAddBook = new Label("Gunakan menu ini untuk \nmenambahkan Buku");
        labelAddBook.setId("paragraph");
        conAddBook.setAlignment(Pos.CENTER);
        ImageView imageViewAddBook = new ImageView(new Image(Main.class.getResourceAsStream("img/Buku.png")));
        imageViewAddBook.setFitWidth(width);
        imageViewAddBook.setFitHeight(height);
        btnAddBook.getStyleClass().add("btnMenu");
        conAddBook.getChildren().addAll(imageViewAddBook, labelAddBook, btnAddBook);
        grid.add(conAddBook, 2, 3);

        VBox conDisplayMahasiswa = new VBox(10);
        Label labelDisplayMahasiswa = new Label("Gunakan menu ini untuk \nmenampilkan mahasiswa");
        labelDisplayMahasiswa.setId("paragraph");
        conDisplayMahasiswa.setAlignment(Pos.CENTER);
        ImageView imageViewDisplayMahasiswa = new ImageView(new Image(Main.class.getResourceAsStream("img/User.png")));
        imageViewDisplayMahasiswa.setFitWidth(width);
        imageViewDisplayMahasiswa.setFitHeight(height);
        btnAddBook.getStyleClass().add("btnMenu");
        conDisplayMahasiswa.getChildren().addAll(imageViewDisplayMahasiswa, labelDisplayMahasiswa, btnDisplayMahasiswa);
        grid.add(conDisplayMahasiswa, 3, 3);

        VBox conDisplayBook = new VBox(10);
        Label labelDisplayBook = new Label("Gunakan menu ini untuk \ndisplay book");
        labelDisplayBook.setId("paragraph");
        conDisplayBook.setAlignment(Pos.CENTER);
        ImageView imageViewDisplayBook = new ImageView(new Image(Main.class.getResourceAsStream("img/ViewBuku.png")));
        imageViewDisplayBook.setFitWidth(width);
        imageViewDisplayBook.setFitHeight(height);
        btnAddBook.getStyleClass().add("btnMenu");
        conDisplayBook.getChildren().addAll(imageViewDisplayBook, labelDisplayBook, btnDisplayBook);
        grid.add(conDisplayBook, 4, 3);

        VBox conEditBook = new VBox(10);
        Label labelEditBook = new Label("Gunakan menu ini untuk \nmengedit buku");
        labelEditBook.setId("paragraph");
        conEditBook.setAlignment(Pos.CENTER);
        ImageView imageEditBook = new ImageView(new Image(Main.class.getResourceAsStream("img/Update.png")));
        imageEditBook.setFitWidth(width);
        imageEditBook.setFitHeight(height);
        btnAddBook.getStyleClass().add("btnMenu");
        conEditBook.getChildren().addAll(imageEditBook, labelEditBook, btnEditBook);
        grid.add(conEditBook, 1, 4);

        VBox conDenda = new VBox(10);
        Label labelDenda = new Label("Gunakan menu ini untuk \n   mengirim email denda");
        labelDenda.setId("paragraph");
        conDenda.setAlignment(Pos.CENTER);
        ImageView imgDenda = new ImageView(new Image(Main.class.getResourceAsStream("img/Denda.png")));
        imgDenda.setFitWidth(width);
        imgDenda.setFitHeight(height);
        conDenda.getChildren().addAll(imgDenda, labelDenda, btnDenda);
        grid.add(conDenda, 2, 4);

        VBox conLogout = new VBox(10);
        Label labelLogout = new Label("Gunakan menu ini untuk \nkeluar dari menu");
        labelLogout.setId("paragraph");
        conLogout.setAlignment(Pos.CENTER);
        ImageView imageLogout = new ImageView(new Image(Main.class.getResourceAsStream("img/Kembali.png")));
        imageLogout.setFitWidth(width);
        imageLogout.setFitHeight(height);
        btnAddBook.getStyleClass().add("btnMenu");
        conLogout.getChildren().addAll(imageLogout, labelLogout, btnLogOut);
        grid.add(conLogout, 3, 4);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnDenda.setOnAction(actionEvent -> {
            // this.pinjamBuku(stage);
            System.out.println("Denda JALAN");
        });
        btnaddMahasiswa.setOnAction(actionEvent -> {
            try {
                addMahasiswa(stage);
            } catch (Exception e) {
                UIManager.showError(actionTarget, e.getMessage());
            } // ini adalah exception handling

        });

        btnAddBook.setOnAction(actionEvent -> {
            try {
                inputBook(stage);
            } catch (Exception e) {
                UIManager.showError(actionTarget, e.getMessage());
            }
        });

        btnDisplayBook.setOnAction(actionEvent -> {
            try {
                displayBook(stage);
            } catch (Exception e) {
                UIManager.showError(actionTarget, e.getMessage());
            }
        });

        btnDisplayMahasiswa.setOnAction(actionEvent -> {
            try {
                displayMahasiswa(stage);
            } catch (Exception e) {
                UIManager.showError(actionTarget, e.getMessage());
            }
        });

        btnDenda.setOnAction(actionEvent -> {
            dendaMahasiswa(stage);
        });

        btnLogOut.setOnAction(actionEvent -> {
            logOut(stage);
        });

        btnEditBook.setOnAction(actionEvent -> {
            updateBooks(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("ADMIN MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void alertEmptyBook(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Anda tidak mempunyai buku yang terpinjam");

        ButtonType btnBack = new ButtonType("Back");
        alert.getButtonTypes().setAll(btnBack);
        ;

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnBack) {
            stage.close();
            menu(stage);
        }
    }

    public void addMahasiswa(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Tambah Mahasiswa");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label name = new Label("Nama");
        grid.add(name, 0, 1); // Kolom 0, Baris 1
        TextField inputName = new TextField();
        inputName.setPromptText("Masukkan nama mahasiswa");
        grid.add(inputName, 1, 1); // Kolom 1, Baris 1

        Label NIM = new Label("NIM  ");
        grid.add(NIM, 0, 2);
        TextField inputNIM = new TextField();
        inputNIM.setPromptText("Masukkan NIM mahasiswa");
        grid.add(inputNIM, 1, 2);

        Label faculty = new Label("Fakultas");
        grid.add(faculty, 0, 3);
        TextField inputFaculty = new TextField();
        inputFaculty.setPromptText("Masukkan fakultas mahasiswa");
        grid.add(inputFaculty, 1, 3);

        Label program = new Label("Program Studi");
        grid.add(program, 0, 4);
        TextField inputProgram = new TextField();
        inputProgram.setPromptText("Masukkan prodi mahasiswa");
        grid.add(inputProgram, 1, 4);

        Label email = new Label("Email");
        grid.add(email, 0, 5);
        TextField inputEmail = new TextField();
        inputEmail.setPromptText("Masukkan email mahasiswa");
        grid.add(inputEmail, 1, 5);

        Button btnSubmit = new Button("Simpan");
        Button btnBack = new Button("Kembali");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 6);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 7);

        btnSubmit.setOnAction(actionEvent -> {
            Mahasiswa mahasiswa = Main.checkNIM(inputName.getText(), inputNIM.getText(), inputFaculty.getText(),
                    inputProgram.getText(), inputEmail.getText());
            if (inputName.getText().isEmpty() || inputNIM.getText().isEmpty() || inputFaculty.getText().isEmpty()
                    || inputProgram.getText().isEmpty())
                UIManager.showError(actionTarget, "PLEASE FILL ALL BLANKS");
            else if (mahasiswa == null)
                UIManager.showError(actionTarget, "NIM SAME");
            else {
                Main.addTempMahasiswa(this, inputName.getText(), inputNIM.getText(), inputFaculty.getText(),
                        inputProgram.getText(), inputEmail.getText());
                UIManager.showSuccess(actionTarget, "Mahasiswa Berhasil Ditambahkan");
            } // ini adalah decition statement
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("Menu Tambah Mahasiswa");
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }

    public void displayMahasiswa(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        tableMahasiswa.setEditable(true);

        tableMahasiswa.getColumns().clear();
        TableColumn<Mahasiswa, String> nameCol = new TableColumn<>("Name");
        TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
        TableColumn<Mahasiswa, String> facultyCol = new TableColumn<>("Faculty");
        TableColumn<Mahasiswa, String> prodiCol = new TableColumn<>("Program Studi");
        TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        prodiCol.setCellValueFactory(new PropertyValueFactory<>("programStudi"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableMahasiswa.getColumns().addAll(nameCol, nimCol, facultyCol, prodiCol, emailCol);

        nameCol.setPrefWidth(148);
        nimCol.setPrefWidth(148);
        facultyCol.setPrefWidth(148);
        prodiCol.setPrefWidth(148);
        emailCol.setPrefWidth(148);

        Button backBtn = new Button("Back");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(tableMahasiswa, 0, 0, 5, 1);

        GridPane.setHalignment(backBtn, HPos.RIGHT);
        gridPane.add(backBtn, 0, 1);

        Scene scene = new Scene(gridPane, UIManager.getWidth(), UIManager.getHeight());
        ArrayList<PropertyMahasiswa> convertMahasiswa = PropertyMahasiswa.mahasiswaToProperty(Admin.getMahasiswaData());
        final ObservableList<PropertyMahasiswa> data = FXCollections.observableArrayList(convertMahasiswa);

        backBtn.setOnAction(e -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        tableMahasiswa.setItems(data);

        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Data mahasiswa yang terdaftar");
        stage.show();
    }

    public void inputBook(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Tambah Buku");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label categoryLabel = new Label("Category:");
        grid.add(categoryLabel, 0, 1);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Manajemen", "Novel", "Ekonomi", "Hukum", "Sejarah", "Agama", "Psikologi",
                "Teknologi");
        categoryComboBox.setPromptText("Select category");
        grid.add(categoryComboBox, 1, 1);

        Label title = new Label("Judul :");
        grid.add(title, 0, 2); // Kolom 0, Baris 1
        TextField fieldTitle = new TextField();
        fieldTitle.setPromptText("Masukkan judul buku");
        grid.add(fieldTitle, 1, 2); // Kolom 1, Baris 1

        Label author = new Label("Pengarang : ");
        grid.add(author, 0, 3);
        TextField fieldAuthor = new TextField();
        fieldAuthor.setPromptText("Masukkan pengarang buku");
        grid.add(fieldAuthor, 1, 3);

        Label stock = new Label("Stok");
        grid.add(stock, 0, 4);
        TextField fieldStock = new TextField();
        fieldStock.setPromptText("Masukkan jumlah stok buku");
        grid.add(fieldStock, 1, 4);

        Button btnSubmit = new Button("Simpan");
        Button btnBack = new Button("Kembali");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSubmit.setOnAction(actionEvent -> {
            String categoryValue = categoryComboBox.getValue();
            if (categoryValue == null || fieldAuthor.getText().isEmpty() || fieldTitle.getText().isEmpty()
                    || fieldStock.getText().isEmpty())
                UIManager.showError(actionTarget, "PLEASE FILL ALL BLANKS");
            else {
                try {
                    Book book;
                    if (categoryValue.equals("Manajemen"))
                        book = new Manajemen(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Novel"))
                        book = new Novel(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Ekonomi"))
                        book = new Ekonomi(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Hukum"))
                        book = new Hukum(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Sejarah"))
                        book = new Sejarah(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Psikologi"))
                        book = new Psikologi(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else if (categoryValue.equals("Teknologi"))
                        book = new Teknologi(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText()));
                    else
                        book = new Agama(this.generateId(), fieldTitle.getText(), fieldAuthor.getText(),
                                Integer.parseInt(fieldStock.getText())); // ini adalah tipe data primitve
                    super.addBook(book);
                    UIManager.showSuccess(actionTarget, "Buku Berhasil Ditambahkan");
                } catch (Exception e) {
                    UIManager.showError(actionTarget, e.getMessage());
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("Menu Tambah Buku");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void updateBooks(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Edit Buku");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(getBookList());
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        grid.add(table, 0, 1, 5, 1);

        Label note = new Label("Masukkan ID buku yang ingin diedit");
        TextField fieldId = new TextField();
        fieldId.setPromptText("Masukkan id buku");
        grid.add(note, 0, 2, 5, 1);
        grid.add(fieldId, 0, 3, 5, 1);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 0, 4, 4, 1);

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("Edit Buku");
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnReturn);
        grid.add(hBBtn, 0, 5);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookAll(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            } else {
                changeBook(stage, book);
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("Menu Edit Buku");
        stage.setScene(scene);
        stage.show();
    }

    public void displayBook(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Buku Yang Tersedia");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(getBookList());

        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        grid.add(table, 0, 1, 5, 1); // Menambahkan TableView ke GridPane

        HBox hBBtn = new HBox(10);
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack);
        grid.add(hBBtn, 1, 2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 3);

        btnBack.setOnAction(e -> {
            stage.close();
            menu(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("Tampilkan Buku Yang Tersedia");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void logOut(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LOGOUT");
        alert.setHeaderText(null);
        alert.setContentText("Anda telah logout dari admin");

        ButtonType yesButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(yesButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            stage.close();
            try {
                Main.menu(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void changeBook(Stage stage, Book book) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Add Book");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label categoryLabel = new Label("Category:");
        grid.add(categoryLabel, 0, 1);

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Sejarah", "Manajemen", "Novel", "Agama", "Ekonomi", "Teknologi",
                "Psikologi", "Hukum");
        categoryComboBox.setPromptText("Select category");
        grid.add(categoryComboBox, 1, 1);

        Label title = new Label("Title :");
        grid.add(title, 0, 2); // Kolom 0, Baris 1
        TextField fieldTitle = new TextField();

        fieldTitle.setText(book.getTitle());
        grid.add(fieldTitle, 1, 2); // Kolom 1, Baris 1

        Label author = new Label("Author : ");
        grid.add(author, 0, 3);
        TextField fieldAuthor = new TextField();
        fieldAuthor.setText(book.getAuthor());
        grid.add(fieldAuthor, 1, 3);

        Label stock = new Label("Stock");
        grid.add(stock, 0, 4);
        TextField fieldStock = new TextField();
        fieldStock.setText(String.valueOf(book.getStock()));
        // fieldStock.setPromptText("Enter book stock");
        grid.add(fieldStock, 1, 4);

        Button btnSubmit = new Button("SAVE");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSubmit);
        grid.add(hBBtn, 1, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 6);

        btnSubmit.setOnAction(actionEvent -> {
            String categoryValue = categoryComboBox.getValue();
            if (categoryValue == null || fieldAuthor.getText().isEmpty() || fieldTitle.getText().isEmpty()
                    || fieldStock.getText().isEmpty())
                UIManager.showError(actionTarget, "PLEASE FILL ALL BLANKS");
            else {
                try {
                    book.setTitle(fieldTitle.getText());
                    book.setAuthor(fieldAuthor.getText());
                    book.setStock(Integer.parseInt(fieldStock.getText()));
                    book.setCategory(categoryValue);
                    UIManager.showSuccess(actionTarget, "BOOK SUCESSFLY ADDED");
                    stage.close();
                    menu(stage);
                } catch (Exception e) {
                    UIManager.showError(actionTarget, e.getMessage());
                }
            }
        });

        btnBack.setOnAction(actionEvent -> {
            stage.setScene(UIManager.getPreviousLayout());
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("CHANGE BOOK INFORMATION MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void addMahasiswa(String name, String NIM, String faculy, String program, String email) {
        Mahasiswa mahasiswa = new Mahasiswa(name, NIM, faculy, program, email);
        MAHASISWA_DATA.add(mahasiswa);
        mahasiswaList.add(NIM);
    }

    public boolean isAdmin(String username, String pass) throws illegalAdminAcces {
        if (username.equals(getAdminUserName()) && pass.equals(getAdminPassword()))
            return true;
        else
            throw new illegalAdminAcces("Invalid Credentials");
    }

    public String generateId() {
        Random random = new Random();

        StringBuilder idTengah = new StringBuilder();
        StringBuilder idAkhir = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            idTengah.append(random.nextInt(10));
            idAkhir.append(random.nextInt(10));

        } // ini adalah loop statement
        return ("UMM-" + idTengah + "-" + idAkhir);

    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public static ArrayList<Mahasiswa> getMahasiswaData() {// ini adalah arraylist
        return MAHASISWA_DATA;
    }

    public static Book searchBookAll(String id) {
        for (Book book : Admin.getBookList())
            if (book.getBookId().equals(id))
                return book;
        return null;
    }

    public static Mahasiswa searchMahasiswaAll(String id) {
        for (Mahasiswa data : getMahasiswaData())
            if (data.getEmail().equals(id))
                return data;
        return null;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public static void sendMail(String pesan, String email) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        String mainEmail = "bimo.putra742@gmail.com";
        String password = "gxty vwjb otvm pjke";

        // Create a Session with the specified properties
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(mainEmail, password);
            }
        });
        String toEmail = email;
        String subject = "UMM Library - Information";
        String body = pesan;
        try {
            MimeMessage msg = new MimeMessage(session);
            // Set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(mainEmail, "UMM Library"));
            msg.setReplyTo(InternetAddress.parse(mainEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            // System.out.println("Message is ready");
            Transport.send(msg);
            System.out.println("Email Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dendaMahasiswa(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene()); // SAVE PREVIOUS SCENE
        tableMahasiswa.setEditable(true);

        tableMahasiswa.getColumns().clear();

        TableColumn<Mahasiswa, String> nameCol = new TableColumn<>("Name");
        TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
        TableColumn<Mahasiswa, String> facultyCol = new TableColumn<>("Faculty");
        TableColumn<Mahasiswa, String> prodiCol = new TableColumn<>("Program Studi");
        TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        facultyCol.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        prodiCol.setCellValueFactory(new PropertyValueFactory<>("programStudi"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableMahasiswa.getColumns().addAll(nameCol, nimCol, facultyCol, prodiCol, emailCol);

        nameCol.setPrefWidth(148);
        nimCol.setPrefWidth(148);
        facultyCol.setPrefWidth(148);
        prodiCol.setPrefWidth(148);
        emailCol.setPrefWidth(148);

        Button backBtn = new Button("Back");
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Label note = new Label("Masukkan Email mahasiswa yang tersedia pada tabel diatas");
        TextField fieldEmail = new TextField();
        fieldEmail.setPromptText("Enter Email");
        gridPane.add(note, 0, 2, 5, 1);
        gridPane.add(fieldEmail, 0, 3, 5, 1);
        gridPane.add(tableMahasiswa, 0, 0, 5, 1);
        GridPane.setHalignment(backBtn, HPos.RIGHT);
        gridPane.add(backBtn, 0, 5);

        Scene scene = new Scene(gridPane, UIManager.getWidth(), UIManager.getHeight());

        ArrayList<PropertyMahasiswa> convertMahasiswa = PropertyMahasiswa.mahasiswaToProperty(Admin.getMahasiswaData());
        final ObservableList<PropertyMahasiswa> dataMahasiswa = FXCollections.observableArrayList(convertMahasiswa); // ini
                                                                                                                     // adalah
                                                                                                                     // arraylist
        tableMahasiswa.setItems(dataMahasiswa);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        gridPane.add(actionTarget, 0, 4, 4, 1);

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("Kirimkan laporan");
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnReturn);
        gridPane.add(hBBtn, 0, 5);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldEmail.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Mahasiswa mahasiswa = this.searchMahasiswaAll(fieldEmail.getText());
            if (mahasiswa == null) {
                UIManager.showError(actionTarget, "Email " + fieldEmail.getText() + " is not found");
            } else {
                System.out.println();
                UIManager.showSuccess(actionTarget, "Email berhasil terkirim");

                StringBuilder pesanBuilder = new StringBuilder();
                pesanBuilder.append("Peringatan kepada ").append(mahasiswa.getName())
                        .append(", untuk segera mengembalikan buku yang dipinjam di Perpustakaan UMM \n")
                        .append("===============================\n")
                        .append("Mohon untuk segera mengembalikan buku yang telah dipinjam ke perpustakaan UMM terdekat dan membayar denda yang dikenakan.\nTerimakasih\n");
                pesanBuilder.append("===============================\n")
                        .append("===UMM Library===");

                String pesan = pesanBuilder.toString();
                sendMail(pesan, mahasiswa.getEmail());

            }

        });

        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.setTitle("TABLE REGISTERED Mahasiswa");
        stage.show();
    }

}