package org.example.com.main.UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.com.main.books.Book;

import java.util.ArrayList;

public class PropertyBook {
    private SimpleStringProperty id; // ini semua ada enkapsulasi dengan variable private
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty category;
    private SimpleIntegerProperty duration;

    PropertyBook(String id, String tile, String author, int stock, String category, int duration) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(tile);
        this.author = new SimpleStringProperty(author);
        this.stock = new SimpleIntegerProperty(stock);
        this.category = new SimpleStringProperty(category);
        this.duration = new SimpleIntegerProperty(duration);
    }// ini adalah construcktor

    public static ArrayList<PropertyBook> bookToProperty(ArrayList<Book> arr) {
        ArrayList<PropertyBook> temp = new ArrayList<>();
        for (Book book : arr) {
            PropertyBook obj = new PropertyBook(book.getBookId(), book.getTitle(), book.getAuthor(), book.getStock(),
                    book.getCategory(), book.getDuration());
            temp.add(obj);
        }
        return temp;
    }

    // semua dibawah ini adalah setter dan getter
    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public int getStock() {
        return stock.get();
    }

    public SimpleIntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }
}