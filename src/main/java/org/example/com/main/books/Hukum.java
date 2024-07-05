package org.example.com.main.books;

public class Hukum extends Book{
    private String category = "Hukum";
    public Hukum(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
}