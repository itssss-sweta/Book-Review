package com.example.demo.service;
import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public Book postBook(Book book){
        try{
            return bookRepository.save(book);
        }
        catch( Exception e){
            throw new RuntimeException("Failed to post book: " + e.getMessage());
        }
    }

    public List<Book> getBook(){
        try{
            return bookRepository.findAll();
        }
        catch( Exception e){
            throw new RuntimeException("Failed to fetch book: " + e.getMessage());
        }
    }

    public boolean deleteBook(long id){
        try{
            if(bookRepository.existsById(id)){
                bookRepository.deleteById(id);
                return true;
            }
            else{
                return false;
            }
        }
        catch( Exception e){
            throw new RuntimeException("Failed to delete book: " + e.getMessage());
        }
    }
    

    public Book updateBook(long id,Book newBook){
        try{
        Optional<Book> bookWithId = bookRepository.findById(id);
        if(bookWithId.isPresent()){
            Book book = bookWithId.get();
            book.setAuthorName(newBook.getAuthorName());
            book.setBId(newBook.getBId());
            book.setBookTitle(newBook.getBookTitle());
            book.setPrice(newBook.getPrice());
            return bookRepository.save(book);
        }
        else{
            throw new RuntimeException("Book not found with id " + id);
        }
    }
    catch(Exception e){
            throw new RuntimeException("Book not found with id " + id);
        }
    }
}
