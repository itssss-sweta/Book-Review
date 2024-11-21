package com.example.demo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Book title cannot be empty.")
    private String bookTitle;

    @Column(nullable = false, unique = true, name = "isbn_number")
    @Size(min = 10, max = 10, message = "ISBN number should be exactly 10 digits.")
    private String isbn;

    @Column(nullable = false)
    @NotNull(message = "Author name cannot be empty.")
    private String authorName;

    @Column(nullable = false)
    @Positive(message = "Price must be greater than zero.")
    private double price;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Rating should be between 0 and 5.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating should be between 0 and 5.")
    private double rating;

    @Column(nullable = false)
    @NotNull(message = "Publisher cannot be empty.")
    private String publisher;

    @Column(nullable = false)
    @Min(value = 1500, message = "Invalid year.")
    private int publicationYear;

    @Lob // This annotation ensures the field is treated as a large object (LOB) in MySQL
    @Column(columnDefinition = "LONGTEXT")
    @NotNull(message = "Description cannot be empty.")
    @Size(max = 1000, message = "Description cannot be longer than 1000 characters.")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Edition cannot be empty.")
    private String edition;

    @Column(nullable = false)
    @NotNull(message = "Genres cannot be empty.")
    @ManyToMany
    @JoinTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonManagedReference
    private List<Genre> genres = new ArrayList<>();

    @Column(nullable = false)
    @Min(value = 1, message = "Page Count must be positive.")
    private int pageCount;

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
