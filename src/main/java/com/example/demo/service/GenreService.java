package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.GenreDto;
import com.example.demo.model.Genre;
import com.example.demo.model.ResponseModel;
import com.example.demo.repository.GenreRepository;
import com.example.demo.utils.ResponseUtil;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // Create a new genre
    public ResponseEntity<ResponseModel<Genre>> createGenre(GenreDto genreDetail) {
        try {
            Genre genre = new Genre();
            genre.setName(genreDetail.getName());
            Genre insertedGenre = genreRepository.save(genre);
            return ResponseUtil.createdResponse(insertedGenre, "Genre Added!");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while creating the genre: " + e.getMessage());

        }
    }

    // Get all genres
    public ResponseEntity<ResponseModel<List<Genre>>> getAllGenres() {
        try {
            List<Genre> genres = genreRepository.findAll();
            return ResponseUtil.successResponse(genres, "Genres Fetched Successfully!");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while fetching genres: " + e.getMessage());

        }

    }

    // Get a genre by ID
    public ResponseEntity<ResponseModel<Genre>> getGenreById(Long id) {
        try {
            Genre genre = genreRepository.findById(id)
                    .orElse(null);
            if (genre == null) {
                return ResponseUtil.notFoundResponse("Genre not found with ID:" + id);
            }
            return ResponseUtil.successResponse(genre, "Genre Found.");
        } catch (Exception e) {
            return ResponseUtil
                    .serverErrorResponse("An error occurred while fetching genre with id: " + id + e.getMessage());

        }
    }

    // Delete a genre by ID
    public ResponseEntity<ResponseModel<Genre>> deleteGenreById(Long id) {
        try {
            Genre genre = genreRepository.findById(id)
                    .orElse(null);
            if (genre == null) {
                return ResponseUtil.notFoundResponse("Genre not found with ID:" + id);
            }
            genreRepository.deleteById(id);
            return ResponseUtil.successResponse(genre, "Genre with id " + id + " deleted successfully.");
        } catch (Exception e) {
            return ResponseUtil
                    .serverErrorResponse("An error occurred while deleting genre with id: " + id + e.getMessage());

        }
    }
}
