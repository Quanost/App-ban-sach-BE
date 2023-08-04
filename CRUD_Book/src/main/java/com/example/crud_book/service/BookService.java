package com.example.crud_book.service;

import com.example.crud_book.model.Book;

import java.util.List;

public interface BookService {
    //Them Sach
    public Book addBook(Book book);

    //Sua thong tin sach
    public Book updateBook(long id, Book book);

    //Xoa sach
    public boolean deleteBook(Long id);

    // Lay danh sach
    public List<Book> getAllBook();

    // Lấy 1 sach
    public Book getOneBook(long id);

    //Lấy số lượng sách
    int getQuantityByBookId(long bookId);


}
