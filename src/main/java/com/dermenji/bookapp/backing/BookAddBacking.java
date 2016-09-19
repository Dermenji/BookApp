package com.dermenji.bookapp.backing;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.Part;

import com.dermenji.bookapp.model.Book;
import com.dermenji.bookapp.service.BookManagerLocal;
import com.dermenji.bookapp.service.exception.BookAlreadyExists;
import org.apache.commons.io.IOUtils;

@Named
@ViewScoped
public class BookAddBacking extends BaseBacking implements Serializable {
    @EJB
    private BookManagerLocal bookManager;

    @Named
    @Produces
    @RequestScoped
    private Book newBook = new Book();

    private String infoMessage;
    private Part filePart;

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public Part getFilePart() {
        return filePart;
    }

    public void setFilePart(Part filePart) {
        this.filePart = filePart;
    }

    public String saveBook() {
        try {
            byte[] bytes = IOUtils.toByteArray(filePart.getInputStream());

            newBook.setContent(bytes);

            bookManager.registerBook(newBook);
            infoMessage = "Book saved successfully";

            newBook = new Book();
        } catch (BookAlreadyExists ex) {
            Logger.getLogger(BookAddBacking.class.getName()).log(Level.SEVERE, null, ex);
            infoMessage = "A book with the same ISBN already exists";
        } catch (Exception ex) {
            Logger.getLogger(BookAddBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while saving book"));
        }

        return null;
    }

    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<FacesMessage>();

        Part file = (Part) value;

        if (file.getSize() > 1048576) {
            msgs.add(new FacesMessage("file size must not exceed 1 MB"));
        }

        if (! "application/pdf".equals(file.getContentType())) {
            msgs.add(new FacesMessage("Book format must be PDF"));
        }

        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }
}
