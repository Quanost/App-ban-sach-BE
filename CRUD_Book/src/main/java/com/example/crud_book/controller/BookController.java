package com.example.crud_book.controller;

import com.example.crud_book.model.Book;
import com.example.crud_book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String test(){
        return "test";
    }

    //API add book
    @PostMapping("/add")
    public Book addBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

    //API update book
    @PutMapping("/update")
    public Book updateBook(@RequestParam("id") long id, @RequestBody Book book){
        return bookService.updateBook(id, book);
    }

    // API delete book
    @DeleteMapping("/delete/{id}")
    public boolean deleteBook(@PathVariable("id") long id){
        return bookService.deleteBook(id);
    }

    // API lay danh sach
    @GetMapping("/list")
    public List<Book> getAllBook(){
        return bookService.getAllBook();
    }

//    API lay so luong sach
    @GetMapping("/quantity/{id}")
    public int getBookQuantityById(@PathVariable("id") long id) {
        int quantity = bookService.getQuantityByBookId(id);
        return quantity;
    }
}
