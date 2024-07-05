package org.example.com.main.books;

public class Agama extends Book{
    private String category = "Agama";
    public Agama(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }
    
    @Override
    public String getCategory() {
        return category;
    }
}