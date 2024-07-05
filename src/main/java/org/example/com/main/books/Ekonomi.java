package org.example.com.main.books;

public class Ekonomi extends Book{
    private String category = "Ekonomi";
    public Ekonomi(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
}
