package org.example.com.main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.Stage;
import org.example.com.main.Main;
import org.example.com.main.UI.PropertyBook;
import org.example.com.main.UI.UIManager;
import org.example.com.main.books.*;
import org.example.com.main.util.IMenu;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import java.awt.Desktop;

public class Mahasiswa extends User implements IMenu {
    private String name, faculty, programStudi, NIM, email;
    private ArrayList<Book> borrowedBooks = new ArrayList<>();
    private static ArrayList<Book> mahasiswaBook = new ArrayList<>();
    private static String[][] tempBook = new String[10][10];
    private static int numberBorroewd = 0;

    public Mahasiswa(String name, String NIM, String faculty, String programStudi, String email) {
        this.name = name;
        this.NIM = NIM;
        this.faculty = faculty;
        this.programStudi = programStudi;
        this.email = email;
    }

    public Mahasiswa() { // ini adalah overloading method

    }

    public static void logIn(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Log In Mahasiswa");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Label userName = new Label("NIM:");
        grid.add(userName, 1, 1); // Kolom 0, Baris 1

        TextField fieldNIM = new TextField();
        fieldNIM.setPromptText("Enter your NIM");
        grid.add(fieldNIM, 1, 2); // Kolom 1, Baris 1

        Button btnSignIn = new Button("SIGN IN");
        Button btnBack = new Button("BACK");
        HBox hBBtn = new HBox(10);
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSignIn);
        grid.add(hBBtn, 1, 3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnSignIn.setOnAction(actionEvent -> {
            if (fieldNIM.getText().isEmpty())
                UIManager.showError(actionTarget, "Field is empty");
            else {
                Mahasiswa mahasiswa = null;
                mahasiswa = searchMahasiswa(fieldNIM.getText());
                if (mahasiswa != null) {
                    Mahasiswa.setmahasiswaBook();
                    mahasiswa.menu(stage);
                } else
                    UIManager.showError(actionTarget, "NIM not found");
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
        stage.setTitle("LOGIN MAHASISWA");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void menu(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Menu Mahasiswa");

        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 32));
        grid.add(sceneTitle, 2, 0, 3, 1);

        Button btnBukuT = new Button("Buku Terpinjam");
        Button btnPinjamB = new Button("Pinjam Buku");
        Button btnKembalikanB = new Button("Kembalikan Buku");
        Button btnOut = new Button("Pinjam Buku atau Logout");
        Button btnUpBook = new Button("Update Buku");
        Button btnLapor = new Button("Lapor Buku");

        double width = 50;
        double height = 50;

        VBox conBukuT = new VBox(10);
        Label labelBukuTerpinjam = new Label(" Gunakan menu ini untuk melihat\n         buku yang terpinjam");
        labelBukuTerpinjam.setId("paragraph");
        conBukuT.setAlignment(Pos.CENTER);
        ImageView imgBukuTerpinjam = new ImageView(new Image(Main.class.getResourceAsStream("img/Buku.png")));
        imgBukuTerpinjam.setFitWidth(width);
        imgBukuTerpinjam.setFitHeight(height);
        // conBukuT.setStyle("-fx-background-color: #D3D3D3;");
        conBukuT.getChildren().addAll(imgBukuTerpinjam, labelBukuTerpinjam, btnBukuT);
        grid.add(conBukuT, 1, 3);

        VBox conPinjamBuku = new VBox(10);
        Label labelPinjamB = new Label("Gunakan menu ini untuk \n    melakukan peminjaman");
        labelPinjamB.setId("paragraph");
        conPinjamBuku.setAlignment(Pos.CENTER);
        ImageView imgPinjamB = new ImageView(new Image(Main.class.getResourceAsStream("img/Pinjam.png")));
        imgPinjamB.setFitWidth(width);
        imgPinjamB.setFitHeight(height);
        conPinjamBuku.getChildren().addAll(imgPinjamB, labelPinjamB, btnPinjamB);
        grid.add(conPinjamBuku, 2, 3);

        VBox conKembalikanB = new VBox(10);
        Label labelKembalikanB = new Label("Gunakan menu ini untuk \n  mengembalikan buku");
        labelKembalikanB.setId("paragraph");
        conKembalikanB.setAlignment(Pos.CENTER);
        ImageView imgKembalikanB = new ImageView(new Image(Main.class.getResourceAsStream("img/Kembali.png")));
        imgKembalikanB.setFitWidth(width);
        imgKembalikanB.setFitHeight(height);

        conKembalikanB.getChildren().addAll(imgKembalikanB, labelKembalikanB, btnKembalikanB);
        grid.add(conKembalikanB, 3, 3);

        VBox conUpdateBuku = new VBox(10);
        Label labelUpdateBuku = new Label("Gunakan menu ini untuk \n    meng-udapte buku");
        labelUpdateBuku.setId("paragraph");
        conUpdateBuku.setAlignment(Pos.CENTER);
        ImageView imgUpdateBuku = new ImageView(new Image(Main.class.getResourceAsStream("img/Update.png")));
        imgUpdateBuku.setFitWidth(width);
        imgUpdateBuku.setFitHeight(height);
        conUpdateBuku.getChildren().addAll(imgUpdateBuku, labelUpdateBuku, btnUpBook);
        grid.add(conUpdateBuku, 1, 4);

        VBox conLapor = new VBox(10);
        Label labelLapor = new Label("Gunakan menu ini untuk \n   melaporkan keluhan");
        labelLapor.setId("paragraph");
        conLapor.setAlignment(Pos.CENTER);
        ImageView imgLapor = new ImageView(new Image(Main.class.getResourceAsStream("img/Denda.png")));
        imgLapor.setFitWidth(width);
        imgLapor.setFitHeight(height);
        // conLapor.setStyle("-fx-background-color: #D3D3D3;");
        conLapor.getChildren().addAll(imgLapor, labelLapor, btnLapor);
        grid.add(conLapor, 2, 4);

        VBox conOut = new VBox(10);
        Label labelKeluar = new Label("Gunakan menu ini untuk \n   Keluar dari menu");
        labelKeluar.setId("paragraph");
        conOut.setAlignment(Pos.CENTER);
        ImageView imgKeluar = new ImageView(new Image(Main.class.getResourceAsStream("img/Keluar.png")));
        imgKeluar.setFitWidth(width);
        imgKeluar.setFitHeight(height);
        // conOut.setStyle("-fx-background-color: #D3D3D3;");
        conOut.getChildren().addAll(imgKeluar, labelKeluar, btnOut);
        grid.add(conOut, 3, 4);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        btnBukuT.setOnAction(actionEvent -> {
            this.displayBook(stage);
            // System.out.println("jalan");
        });

        btnPinjamB.setOnAction(actionEvent -> {
            this.pinjamBuku(stage);
        });

        btnKembalikanB.setOnAction(actionEvent -> {
            if (this.borrowedBooks.isEmpty())
                alertEmptyBook(stage);
            else
                returnBooks(stage);
        });

        btnUpBook.setOnAction(actionEvent -> {
            if (this.borrowedBooks.isEmpty())
                alertEmptyBook(stage);
            else
                updateBooks(stage);
        });

        btnOut.setOnAction(actionEvent -> {
            logOut(stage);
        });

        btnLapor.setOnAction(actionEvent -> {
            displayLapor(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getWidth());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("MENU MAHASISWA");
        stage.setScene(scene);
        stage.show();
    }

    public void alertEmptyBook(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("INFORMASI");
        alert.setHeaderText(null);
        alert.setContentText("Anda tidak memiliki buku yang terpinjam");

        ButtonType btnBack = new ButtonType("Kembali");
        alert.getButtonTypes().setAll(btnBack);
        ;

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == btnBack) {
            stage.close();
            menu(stage);
        }
    }

    @Override
    public void logOut(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("INFORMASI");
        alert.setHeaderText(null);
        alert.setContentText("Anda berhasil logout dari mahasiswa");

        ButtonType yesButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(yesButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            stage.close();
            try {
                clearArray();
                Main.menu(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void displayBook(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("DAFTAR BUKU TERPINJAM");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
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
        stage.setTitle("Daftar Buku");
        stage.setScene(scene);
        stage.show();

    }

    public void pinjamBuku(Stage stage) {

        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("PEMINJAMAN BUKU");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = super.createTableView(getMahasiswaBook());
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        Label id = new Label("ID");
        TextField fieldId = new TextField();
        fieldId.setPromptText("Masukkan id buku");
        grid.add(id, 0, 2);
        grid.add(fieldId, 1, 2);

        Label duration = new Label("Lama Peminjaman");
        TextField fieldDuration = new TextField();
        fieldDuration.setPromptText("Masukkan lama peminjaman");
        grid.add(duration, 0, 3);
        grid.add(fieldDuration, 1, 3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 4);

        HBox hBBtn = new HBox(10);
        Button btnAdd = new Button("Pinjam Buku");
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnAdd);
        grid.add(hBBtn, 1, 5);

        btnAdd.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty() || fieldDuration.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }

            Book book = this.searchBookAll(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
                return;
            }

            if (isBookAvailable(this, fieldId.getText())) {
                UIManager.showError(actionTarget, "BOOK HAS BEEN BORROWED");
                return;
            }

            try {
                String durasi = fieldDuration.getText();

                Book bookInfo = this.searchBookAll(fieldId.getText());
                StringBuilder pesanBuilder = new StringBuilder();
                pesanBuilder.append("Terimakasih ").append(getName())
                        .append(", telah meminjam buku di Perpustakaan UMM \n")
                        .append("===============================\n")
                        .append("Berikut informasi terkait buku yang anda pinjam\n");

                if (bookInfo != null) {
                    pesanBuilder.append("Kode Buku : ").append(bookInfo.getBookId()).append("\n")
                            .append("Judul : ").append(bookInfo.getTitle()).append("\n")
                            .append("Pengarang : ").append(bookInfo.getAuthor()).append("\n");
                } else {
                    pesanBuilder.append("Judul : Tidak ditemukan\n")
                            .append("Pengarang : Tidak ditemukan\n");
                }

                pesanBuilder.append("Lama Peminjaman : ").append(durasi).append(" Hari\n")
                        .append("===============================\n")
                        .append("PERHATIAN!!\nPastikan anda mengembalikan buku tersebut dalam jangka waktu yang sudah anda tentukan\nTerimakasih\n===UMM Library===");

                String pesan = pesanBuilder.toString();

                if (Integer.parseInt(fieldDuration.getText()) >= 7) {
                    UIManager.showError(actionTarget, "Durasi harus dibawah 7 hari");
                    return;
                }
                tempBook[numberBorroewd][0] = book.getBookId();
                tempBook[numberBorroewd][1] = fieldDuration.getText();
                numberBorroewd++;
                book.setStock(book.getStock() - 1);
                UIManager.showSuccess(actionTarget, "BOOK ADDED SUCCESSFULLY");
                keepBooks(stage);
                Admin.sendMail(pesan, getEmail());
            } catch (NumberFormatException e) {
                UIManager.showError(actionTarget, "INPUT VALID NUMBER DURATION");
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
        stage.setTitle("Peminjaman Buku");
        stage.setScene(scene);
        stage.show();
    }

    public void keepBooks(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("APAKAH KAMU INGIN MEMINJAM BUKU INI");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        ArrayList<Book> selectionArr = new ArrayList<>();
        for (int i = 0; i < numberBorroewd; i++) {
            for (Book book : getBookList()) {
                if (book.getBookId().equals(tempBook[i][0])) {
                    Book newBook = new Book(book.getBookId(), book.getTitle(), book.getAuthor(), book.getStock());
                    newBook.setCategory(book.getCategory());
                    newBook.setDuration(Integer.parseInt(tempBook[i][1]));
                    selectionArr.add(newBook);
                    break;
                }
            }
        }
        TableView<PropertyBook> table = createTableView(selectionArr);
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        HBox hBBtn = new HBox(10);
        Button btnNo = new Button("Tidak");
        Button btnSave = new Button("Pinjam");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnNo, btnSave);
        grid.add(hBBtn, 1, 2);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 1, 3);

        btnSave.setOnAction(actionEvent -> {
            Main.addTempBook(this, numberBorroewd, tempBook);
            UIManager.showSuccess(actionTarget, "Buku berhasil dipinjam");
            // logOut(stage);
            menu(stage);
        });

        btnNo.setOnAction(e -> {
            UIManager.showError(actionTarget, "Buku tidak terpinjam");
            // logOut(stage);
            menu(stage);
        });

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        try {
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: CSS file not found. Please ensure style.css is in the correct directory.");
            e.printStackTrace();
        }
        stage.setTitle("Detail Peminjaman");
        stage.setScene(scene);
        stage.show();
    }

    public void returnBooks(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Pengembalian Buku");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        Label note = new Label("Masukkan ID buku yang akan dikembalikan");
        TextField fieldId = new TextField();
        fieldId.setPromptText("Masukkan id buku");
        grid.add(note, 0, 2, 5, 1);
        grid.add(fieldId, 0, 3, 4, 1);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 0, 4, 4, 1);

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("Kembalikan Buku");
        Button btnBack = new Button("Batal");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnReturn);
        grid.add(hBBtn, 0, 4, 1, 1);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookBorrowed(fieldId.getText());
            if (book == null) {
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            } else {
                Book bookInfo = this.searchBookAll(fieldId.getText());
                StringBuilder pesanBuilder = new StringBuilder();
                pesanBuilder.append("Terimakasih ").append(getName())
                        .append(", telah mengembalikan buku di Perpustakaan UMM \n")
                        .append("===============================\n")
                        .append("Berikut informasi terkait buku yang telah anda kembalikan\n");

                if (bookInfo != null) {
                    pesanBuilder.append("Kode Buku : ").append(bookInfo.getBookId()).append("\n")
                            .append("Judul : ").append(bookInfo.getTitle()).append("\n")
                            .append("Pengarang : ").append(bookInfo.getAuthor()).append("\n");
                } else {
                    pesanBuilder.append("Judul : Tidak ditemukan\n")
                            .append("Pengarang : Tidak ditemukan\n");
                }

                pesanBuilder.append("===============================\n")
                        .append("Terimakasih telah mengembalikan buku tersebut ke Perpustakaan UMM\n===UMM Library===");

                String pesan = pesanBuilder.toString();

                this.changeAmountBook(book, fieldId.getText());
                UIManager.showSuccess(actionTarget, "Buku berhasil dikembalikan");
                stage.close();
                Admin.sendMail(pesan, getEmail());
                pinjamBuku(stage);
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
        stage.setTitle("Pengembalian Buku");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public TableView<PropertyBook> createTableView(ArrayList<Book> arr) {
        TableView<PropertyBook> table = new TableView<>();
        table.setEditable(false);
        table.getColumns().clear();

        TableColumn<PropertyBook, String> idCol = new TableColumn<>("Id");
        TableColumn<PropertyBook, String> titleCol = new TableColumn<>("Title");
        TableColumn<PropertyBook, String> authorCol = new TableColumn<>("Author");
        TableColumn<PropertyBook, Integer> durationCol = new TableColumn<>("Duration");
        TableColumn<PropertyBook, String> categoryCol = new TableColumn<>("Category");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getColumns().addAll(idCol, titleCol, authorCol, durationCol, categoryCol);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        final VBox vbox = new VBox();
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(20, 10, 10, 10));
        vbox.getChildren().addAll(table);
        gridPane.add(vbox, 0, 0);

        ArrayList<PropertyBook> convertBook = PropertyBook.bookToProperty(arr);
        final ObservableList<PropertyBook> data = FXCollections.observableArrayList(convertBook);
        table.setItems(data);
        return table;
    }

    @Override
    public void updateBooks(Stage stage) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Jarak horizontal antar kolom
        grid.setVgap(10); // Jarak vertikal antar baris
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Perpanjangan Peminjaman Buku");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1); // Menambahkan TableView ke GridPane

        Label note = new Label("Masukkan ID buku yang ingin diperpanjang");
        TextField fieldId = new TextField();
        fieldId.setPromptText("Masukkan id buku");
        grid.add(note, 0, 2, 5, 1);
        grid.add(fieldId, 0, 3, 4, 1);

        HBox hBBtn = new HBox(10);
        Button btnReturn = new Button("Simpan Perubahan");
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnReturn);
        grid.add(hBBtn, 0, 5);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200); // Set a fixed width to prevent layout changes
        grid.add(actionTarget, 0, 4, 4, 1);

        btnReturn.setOnAction(actionEvent -> {
            if (fieldId.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            Book book = this.searchBookBorrowed(fieldId.getText());
            if (book == null)
                UIManager.showError(actionTarget, "Book with id " + fieldId.getText() + " is not found");
            else
                changeDuration(stage, book);

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
        stage.setTitle("RETURN BOOK MENU");
        stage.setScene(scene);
        stage.show();
    }

    public void changeDuration(Stage stage, Book book) {
        UIManager.setPreviousLayout(stage.getScene());// SAVE PRVIOUS SCENE
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Perpanjangan Durasi Peminjaman");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        TableView<PropertyBook> table = createTableView(this.getBorrowedBooks());
        table.getColumns().forEach(column -> {
            column.setPrefWidth(148);
        });
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        grid.add(vbox, 0, 1, 2, 1);

        Label duration = new Label("Durasi");
        TextField fieldDuration = new TextField();
        fieldDuration.setPromptText("Masukkan lama peminjaman yang baru");
        grid.add(duration, 0, 2);
        grid.add(fieldDuration, 1, 2);

        HBox hBBtn = new HBox(10);
        Button btnSave = new Button("Simpan");
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, btnSave);
        grid.add(hBBtn, 1, 3);

        final Text actionTarget = new Text();
        actionTarget.setWrappingWidth(200);
        grid.add(actionTarget, 1, 4);

        Scene scene = new Scene(grid, UIManager.getWidth(), UIManager.getHeight());
        stage.setTitle("PERPANJANGAN");
        stage.setScene(scene);
        stage.show();

        btnSave.setOnAction(actionEvent -> {
            if (fieldDuration.getText().isEmpty()) {
                UIManager.showError(actionTarget, "FIELD CANNOT BE EMPTY");
                return;
            }
            try {
                int newDuration = Integer.parseInt(fieldDuration.getText());
                if (newDuration > 14)
                    UIManager.showError(actionTarget, "DURATION MUST BELOW 15");
                else {
                    book.setDuration(newDuration);
                    UIManager.showSuccess(actionTarget, "BOOK EDIT SUCCESFULLY");
                }

            } catch (Exception e) {
                UIManager.showError(actionTarget, "PLEASE INPUT VALID NUMBER");
            }
        });
        btnBack.setOnAction(actionEvent -> {
            stage.close();
            menu(stage);
        });

    }

    public void displayLapor(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text sceneTitle = new Text("Laporkan masalah");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1); // Kolom 0, Baris 0, Colspan 2, Rowspan 1

        Button googleButton = new Button("Laporkan kendala");
        googleButton.setOnAction(e -> {
            // try {
            // Desktop.getDesktop().browse(new URI("https://forms.gle/imu8x7tjDs2Loqhh9"));
            // } catch (Exception ex) {
            // ex.printStackTrace();
            // }
        });

        HBox hBBtn = new HBox(10);
        Button btnBack = new Button("Kembali");
        hBBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hBBtn.getChildren().addAll(btnBack, googleButton);
        grid.add(hBBtn, 0, 5);

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
        stage.setScene(scene);
        stage.show();
    }

    public static void clearArray() {
        for (int i = 0; i < numberBorroewd; i++) {
            tempBook[i][0] = null;
            tempBook[i][1] = null;
        }
        numberBorroewd = 0;
    }

    public boolean isBookAvailable(Mahasiswa mahasiswa, String id) {
        boolean isFound = false;
        for (int i = 0; i < numberBorroewd; i++)
            if (tempBook[i][0].equals(id)) {
                isFound = true;
                break;
            }
        Book book = mahasiswa.searchBookBorrowed(id);
        if (book == null && !isFound)
            return false;
        return true;
    }

    @Override
    public void addBook(Book book) {
        borrowedBooks.add(book);
    }

    public void choiceBook(String bookId, int duration) {
        Book book = Mahasiswa.searchBookAll(bookId);
        Book borrowedBookCopy = new Book(book.getBookId(), book.getTitle(), book.getAuthor(), 1);
        borrowedBookCopy.setDuration(duration);
        borrowedBookCopy.setCategory(book.getCategory());
        this.addBook(borrowedBookCopy);
        Book bookAdmin = Admin.searchBookAll(bookId);
        bookAdmin.setStock(bookAdmin.getStock() - 1);
    }

    public static void setmahasiswaBook() {
        mahasiswaBook.clear();
        for (Book book : getBookList()) {
            Book copiedBook = new Book(book.getBookId(), book.getTitle(), book.getAuthor(), book.getStock());
            copiedBook.setCategory(book.getCategory());
            mahasiswaBook.add(copiedBook);
        }
    }

    public void changeAmountBook(Book bookBorrowed, String inputId) {
        Book bookAdmin = Admin.searchBookAll(inputId);
        bookAdmin.setStock(bookAdmin.getStock() + 1);
        Book allBook = Mahasiswa.searchBookAll(inputId);
        allBook.setStock(allBook.getStock() + 1);
        this.getBorrowedBooks().remove(bookBorrowed);
    }

    public static Book searchBookAll(String id) {
        for (Book book : mahasiswaBook)
            if (book.getBookId().equals(id))
                return book;
        return null;
    }

    public Book searchBookBorrowed(String id) {
        for (Book book : this.getBorrowedBooks())
            if (book.getBookId().equals(id))
                return book;
        return null;
    }

    public static Mahasiswa searchMahasiswa(String inputNIM) {
        for (Mahasiswa mahasiswa : Admin.getMahasiswaData())
            if (mahasiswa.getNIM().equals(inputNIM))
                return mahasiswa;
        return null;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getProgramStudi() {
        return programStudi;
    }

    public static ArrayList<Book> getMahasiswaBook() {
        return mahasiswaBook;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public String getNIM() {
        return this.NIM;
    }

    public String getEmail() {
        return email;
    }

}