package org.example.com.main.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.com.main.UI.PropertyBook;
import org.example.com.main.books.*;
import java.util.ArrayList;

public abstract class User {
    private static ArrayList<Book> bookList = new ArrayList<>();
    public abstract void updateBooks(Stage stage);
    public void addBook(Book book){
        bookList.add(book);
    }

    public void displayBook(Stage stage){

    }
    public abstract void logOut(Stage stage);

    public TableView<PropertyBook> createTableView(ArrayList<Book> arr){
        TableView<PropertyBook> table = new TableView<>();
        table.setEditable(false);
        table.getColumns().clear();

        TableColumn<PropertyBook,String> idCol = new TableColumn<>("Id");
        TableColumn<PropertyBook,String> titleCol = new TableColumn<>("Title");
        TableColumn<PropertyBook,String> authorCol = new TableColumn<>("Author");
        TableColumn<PropertyBook,Integer> stockCol = new TableColumn<>("Stock");
        TableColumn<PropertyBook, String> categoryCol = new TableColumn<>("Category");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        table.getColumns().addAll(idCol,titleCol,authorCol,stockCol,categoryCol);

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


    

    public static ArrayList<Book> getBookList(){
        return bookList;
    }

}