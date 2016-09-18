package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.BookRequest;
import com.dermenji.bookapp.service.exception.BookRequestAlreadyExists;
import com.dermenji.bookapp.service.exception.BookRequestNotFound;

import javax.ejb.Local;
import java.util.List;

@Local
public interface BookRequestManagerLocal {
    public BookRequest sendBookRequest(BookRequest bookRequest) throws BookRequestAlreadyExists;
    public void approveBookRequest(Integer bookRequestNumber) throws BookRequestNotFound;
    public void rejectBookRequest(Integer bookRequestNumber) throws BookRequestNotFound;
    public List<BookRequest> viewRequests(String userName, int status);
}
