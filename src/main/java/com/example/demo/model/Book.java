package com.example.demo.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String bId;

    @Column (nullable =  false)
    private String authorName;

    @Column(nullable = false)
    private double price;


}
