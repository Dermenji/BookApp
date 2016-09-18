package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.*;
import com.dermenji.bookapp.service.exception.BookRequestAlreadyExists;
import com.dermenji.bookapp.service.exception.BookRequestNotFound;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class BookRequestManager implements BookRequestManagerLocal {

    @PersistenceContext(unitName = "megaAppUnit")
    EntityManager em;

    @Override
    public BookRequest sendBookRequest(BookRequest bookRequest) throws BookRequestAlreadyExists {
        Query query = em.createQuery("select bookRequest from BookRequest bookRequest where "
                + "bookRequest.bookId.id = :bookId and bookRequest.userId.id = :userId");
        query.setParameter("bookId", bookRequest.getBookId().getId());
        query.setParameter("userId", bookRequest.getUserId().getId());
        try {
            query.getSingleResult();
            throw new BookRequestAlreadyExists();
        } catch (NoResultException exception) {
            Logger.getLogger(BookManager.class.getName()).log(Level.FINER, "No book request found");
        }
        bookRequest.setRequestTime(System.currentTimeMillis());
        bookRequest.setStatus(Constants.PENDING_REQUEST); //pending status...
        em.persist(bookRequest);
        em.flush();
        return bookRequest;
    }

    @Override
    public void approveBookRequest(Integer bookRequestNumber) throws BookRequestNotFound {
        BookRequest updatableBookRequest = em.find(BookRequest.class, bookRequestNumber);
        if (updatableBookRequest == null) {
            throw new BookRequestNotFound();
        }
        updatableBookRequest.setStatus(Constants.APPROVED_REQUEST); //approved status
        updatableBookRequest.setResponseTime(System.currentTimeMillis());
        em.merge(updatableBookRequest);
        em.flush();
    }

    @Override
    public void rejectBookRequest(Integer bookRequestNumber) throws BookRequestNotFound {
        BookRequest updatableBookRequest = em.find(BookRequest.class, bookRequestNumber);
        if (updatableBookRequest == null) {
            throw new BookRequestNotFound();
        }
        updatableBookRequest.setStatus(Constants.REJECTED_REQUEST); //rejected status
        updatableBookRequest.setResponseTime(System.currentTimeMillis());
        em.merge(updatableBookRequest);
        em.flush();
    }

    @Override
    public List<BookRequest> viewRequests(String userID, int status) {
        String requestQuery = "select bookRequest.id, book.id, book.title, bookRequest.requestTime, bookRequest.responseTime, bookRequest.userId.id "
                + "from BookRequest bookRequest JOIN bookRequest.bookId book JOIN bookRequest.userId user "
                + "where bookRequest.status = :statusID";
        Query query = null;
        UserGroup group = getUserGroup(userID);
        if (group.getGroupId() == Constants.USER_GROUP) {
            requestQuery += " and bookRequest.userId.id = :userId";
            query = em.createQuery(requestQuery);
            query.setParameter("statusID", status);
            query.setParameter("userId", userID);
        } else {
            query = em.createQuery(requestQuery);
            query.setParameter("statusID", status);
        }
        List<BookRequest> bookRequests = new ArrayList<BookRequest>();
        List<Object[]> results = (List<Object[]>) query.getResultList();
        if (results == null) {
            return bookRequests;
        }
        for (Object[] result : results) {
            BookRequest bookRequest = new BookRequest((Integer) result[0]);
            Book book = new Book();
            book.setId((Integer) result[1]);
            book.setTitle((String) result[2]);
            bookRequest.setBookId(book);
            bookRequest.setRequestTime((Long) result[3]);
            bookRequest.setResponseTime((Long) result[4]);
            bookRequest.setUserId(new MegaUser((String) result[5]));
            bookRequests.add(bookRequest);
        }
        return bookRequests;
    }

    private UserGroup getUserGroup(String userID) {
        Query query = em.createQuery("Select userGroup from UserGroup userGroup where userGroup. " +
                "userId.id=:userID", UserGroup.class);
        query.setParameter("userID", userID);
        UserGroup group;
        try {
            group = (UserGroup) query.getSingleResult();
        } catch (NoResultException exception) {
            throw new IllegalStateException(userID + " state is invalid as user does not belong to any group!!!");
        }
        return group;
    }
}
