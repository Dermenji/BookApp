package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.Book;
import com.dermenji.bookapp.service.exception.BookAlreadyExists;
import com.dermenji.bookapp.service.exception.BookNotFound;

import javax.ejb.Local;
import java.util.List;

@Local
public interface BookManagerLocal {
    public Book getBookInformation(Integer bookID) throws BookNotFound;
    public Book registerBook(Book book) throws BookAlreadyExists;
    public Book updateBook(Book book) throws BookNotFound;
    public void removeBook(Integer bookID) throws BookNotFound;
    public byte[] getBookContent(Integer bookID) throws BookNotFound;
    public List<Book> getAllBooks(Book book);
}
