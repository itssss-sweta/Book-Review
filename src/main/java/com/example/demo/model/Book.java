package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false, unique = true, name = "isbn_number")
    private String isbn;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private double price;

    private String imageUrl;

    @ManyToMany(mappedBy = "favouriteBooks")
    @JsonIgnore
    private Set<UserModel> favoritedByUsers = new HashSet<>();

    public Set<UserModel> getFavoritedByUsers() {
        return favoritedByUsers;
    }

    public void setFavoritedByUsers(Set<UserModel> favoritedByUsers) {
        this.favoritedByUsers = favoritedByUsers;
    }

}
