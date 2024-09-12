package com.example.demo.service;
import java.util.List;
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
    
}
