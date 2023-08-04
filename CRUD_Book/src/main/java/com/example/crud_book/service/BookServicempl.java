package com.example.crud_book.service;

import com.example.crud_book.model.Book;
import com.example.crud_book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServicempl implements BookService{

   @Autowired
   private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        if(book != null){
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public Book updateBook(long id, Book book) {
        if(book!=null){
            Book book1=bookRepository.getById(id);
            if(book1!=null){
                book1.setName(book.getName());
                book1.setAuthor(book.getAuthor());
                book1.setQuantity(book.getQuantity());

                return bookRepository.save(book1);
            }
        }
        return null;
    }

    @Override
    public boolean deleteBook(Long id) {
        if(id>=1){
            Book book=bookRepository.getById(id);
            if(book!=null){
                bookRepository.delete(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public Book getOneBook(long id) {
        return bookRepository.getById(id);
    }

    //Lấy số lượng sách
    @Override
    public int getQuantityByBookId(long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            return book.getQuantity();
        } else {
            return -1; // Hoặc một giá trị thể hiện sách không tồn tại
        }
    }
}
