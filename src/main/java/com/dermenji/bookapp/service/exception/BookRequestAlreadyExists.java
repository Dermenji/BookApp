package com.dermenji.bookapp.service.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class BookRequestAlreadyExists extends Exception {
    public BookRequestAlreadyExists () {
        this.message = "Book request already exists";
    }

    public BookRequestAlreadyExists(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    private String message;
}