package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.Book;
import com.dermenji.bookapp.service.exception.BookAlreadyExists;
import com.dermenji.bookapp.service.exception.BookNotFound;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class BookManager implements BookManagerLocal {

    @PersistenceContext(unitName = "megaAppUnit")
    EntityManager em;

    @Override
    public Book registerBook(Book book) throws BookAlreadyExists {
        Query query = em.createQuery("select book from Book book where "
                + "book.isbn = :isbn");
        query.setParameter("isbn", book.getIsbn());

        try {
            query.getSingleResult();
            throw new BookAlreadyExists();
        } catch (NoResultException exception) {
            Logger.getLogger(BookManager.class.getName()).log(Level.FINER, "No similar books found");
        }
        em.persist(book);
        em.flush();
        return book;
    }

    @Override
    public Book updateBook(Book book) throws BookNotFound {
        Book updatableBook = em.find(Book.class, book.getId());
        if (updatableBook == null) {
            throw new BookNotFound();
        }
        mergeBookAttrs(book, updatableBook);
        em.merge(updatableBook);
        em.flush();
        return book;
    }

    private void mergeBookAttrs(Book book, Book updatableBook) {
        if (book.getAuthor() != null) {
            updatableBook.setAuthor(book.getAuthor());
        }
        if (book.getContent() != null) {
            updatableBook.setContent(book.getContent());
        }
        if (book.getIsbn() != null) {
            updatableBook.setIsbn(book.getIsbn());
        }
        if (book.getLang() != null) {
            updatableBook.setLang(book.getLang());
        }
        if (book.getPublisher() != null) {
            updatableBook.setPublisher(book.getPublisher());
        }
        if (book.getTitle() != null) {
            updatableBook.setTitle(book.getTitle());
        }
    }

    @Override
    public Book getBookInformation(Integer bookID) throws BookNotFound {
        Query query = em.createQuery("select book.id, book.isbn, book.title, "
                + "book.author, book.publisher, book.lang from Book book where "
                + "book.id = :id");
        query.setParameter("id", bookID);
        Object[] bookInfo = null;
        try {
            bookInfo = (Object[]) query.getSingleResult();
        } catch (NoResultException exception) {
            throw new BookNotFound(exception.getMessage());
        }
        Book book = new Book(
                (Integer) bookInfo[0],
                (String) bookInfo[1],
                (String) bookInfo[2],
                (String) bookInfo[3],
                (String) bookInfo[4],
                (String) bookInfo[5],
                null);
        return book;
    }

    @Override
    public void removeBook(Integer bookID) throws BookNotFound {
        Book book = em.find(Book.class, bookID);
        if (book == null) {
            throw new BookNotFound();
        }
        em.remove(book);
        em.flush();
    }

    @Override
    public byte[] getBookContent(Integer bookID) throws BookNotFound {
        byte[] content = null;
        try {
            content = (byte[]) em.createQuery("Select book.content from Book book where book.id=:id")
                    .setParameter("id", bookID)
                    .getSingleResult();
        } catch (NoResultException exception) {
            throw new BookNotFound(exception.getMessage());
        }
        return content;
    }

    @Override
    public List<Book> getAllBooks(Book searchableBook) {
        List<Book> books = new ArrayList<Book>();
        String searchableTitle = searchableBook.getTitle();
        Query query = em.createQuery("select book.id, book.isbn, book.title, "
                + "book.author, book.publisher, book.lang from Book book where "
                + "book.title like :title");
        query.setParameter("title", "%" + searchableTitle + "%");
        List<Object[]> bookList = (List<Object[]>) query.getResultList();
        if (bookList == null) {
            return books;
        }
        for (Object[] bookInfo : bookList) {
            Book book = new Book(
                    (Integer) bookInfo[0],
                    (String) bookInfo[1],
                    (String) bookInfo[2],
                    (String) bookInfo[3],
                    (String) bookInfo[4],
                    (String) bookInfo[5],
                    null);
            books.add(book);
        }
        return books;
    }
}