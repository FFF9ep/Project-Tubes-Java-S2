package org.example.com.main.books;

public class Novel extends Book{
    private String category = "Novel";
    public Novel(String bookId, String title, String author, int stock){
        super(bookId,title,author,stock);
        super.setCategory(category);
    }

    @Override
    public String getCategory() {
        return category;
    }
    
}
