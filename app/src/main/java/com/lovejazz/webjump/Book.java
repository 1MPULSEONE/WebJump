package com.lovejazz.webjump;

public class Book {
    private String name;
    private String author;
    private double rating;
    private String image;
    public Book(){

    }

    public Book(String bookName, String author, double rating, String image,String key) {
        this.name = bookName;
        this.author = author;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }


    public String getImage() {
        return image;
    }
}
