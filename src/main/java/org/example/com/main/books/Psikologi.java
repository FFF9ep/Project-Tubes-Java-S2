package org.example.com.main.books;

public class Psikologi extends Book{
    private String category = "Psikologi";
    public Psikologi(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
}