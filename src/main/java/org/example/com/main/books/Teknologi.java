package org.example.com.main.books;

public class Teknologi extends Book{
    private String category = "Teknologi";
    public Teknologi(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
}