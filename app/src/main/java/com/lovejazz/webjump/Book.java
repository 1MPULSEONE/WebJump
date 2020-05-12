package com.lovejazz.webjump;

public class Book {
    String bookName;
    String author;
    double rating;
    int image;
    public Book(){

    }

    public Book(String bookName, String author, double rating, int image) {
        this.bookName = bookName;
        this.author = author;
        this.image = image;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }


    public int getImage() {
        return image;
    }
}
