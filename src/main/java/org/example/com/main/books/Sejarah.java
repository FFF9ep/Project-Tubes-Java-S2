package org.example.com.main.books;

public class Sejarah extends Book{
    private String category = "Sejarah";
    public Sejarah(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
}