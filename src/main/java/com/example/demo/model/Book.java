package com.example.demo.model;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private long bId;

    @Column (nullable =  false)
    private String authorName;

    @Column(nullable = false)
    private double price;

    public long getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public long getBId() {
        return bId;
    }

    public void setBId(long serialNumber) {
        this.bId = serialNumber;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
