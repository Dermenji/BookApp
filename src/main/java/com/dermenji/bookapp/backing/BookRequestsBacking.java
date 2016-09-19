package com.dermenji.bookapp.backing;

import com.dermenji.bookapp.model.Book;
import com.dermenji.bookapp.model.BookRequest;
import com.dermenji.bookapp.model.Constants;
import com.dermenji.bookapp.service.BookManagerLocal;
import com.dermenji.bookapp.service.BookRequestManagerLocal;
import com.dermenji.bookapp.service.exception.BookNotFound;
import com.dermenji.bookapp.service.exception.BookRequestNotFound;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class BookRequestsBacking extends BaseBacking implements Serializable {
    @EJB
    private BookManagerLocal bookManager;

    @EJB
    private BookRequestManagerLocal bookRequestManager;

    private List<BookRequest> bookRequestList;
    private BookRequest selectedBookRequest;

    private int status = 1;
    private String infoMessage;

    public List<BookRequest> getBookRequestList() {
        return bookRequestList;
    }

    public void setBookRequestList(List<BookRequest> bookRequestList) {
        this.bookRequestList = bookRequestList;
    }

    public BookRequest getSelectedBookRequest() {
        return selectedBookRequest;
    }

    public void setSelectedBookRequest(BookRequest selectedBookRequest) {
        this.selectedBookRequest = selectedBookRequest;
    }

    public int getStatus() {
        return status;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void retrieveBookRequests(ComponentSystemEvent event) {
        bookRequestList = bookRequestManager.viewRequests(getRequest().getUserPrincipal().getName(),
                status);

        if (bookRequestList.isEmpty()) {
            infoMessage = "No Requests found";
        } else {
            infoMessage = bookRequestList.size() + " request(s) found";
        }

    }

    public String downloadBook() {
        Book currentSelectedBook = getSelectedBookRequest().getBookId();

        Book book;
        byte[] content;

        try {
            book = bookManager.getBookInformation(currentSelectedBook.getId());
            content = bookManager.getBookContent(currentSelectedBook.getId());

        } catch (BookNotFound ex) {
            Logger.getLogger(BookRequestsBacking.class.getName()).log(Level.SEVERE, "No books found !!!", ex);
            return null;
        }

        ExternalContext externalContext = getContext().getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType(Constants.APP_PDF_TYPE);
        externalContext.setResponseContentLength(content.length);
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + book.getTitle() + ".pdf\"");
        OutputStream output = null;

        try {
            output = externalContext.getResponseOutputStream();

            output.write(content);

            output.flush();
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(BookRequestsBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while downloading book"));
        } finally {
            getContext().responseComplete();
        }

        return null;
    }

    public String approveRequest() {
        BookRequest currentBookRequest = getSelectedBookRequest();

        try {
            bookRequestManager.approveBookRequest(currentBookRequest.getId());
            infoMessage = "Book Request Approved";
        } catch (BookRequestNotFound ex) {
            Logger.getLogger(BookRequestsBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while approving book request"));
        }

        return null;
    }

    public String rejectRequest() {
        BookRequest currentBookRequest = getSelectedBookRequest();

        try {
            bookRequestManager.rejectBookRequest(currentBookRequest.getId());
            infoMessage = "Book Request Rejected";
        } catch (BookRequestNotFound ex) {
            Logger.getLogger(BookRequestsBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while rejecting book request"));
        }

        return null;
    }
}