package com.dermenji.bookapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "BOOK_REQUEST")
public class BookRequest implements Serializable {
    private static final long serialVersionUID = 132123123120090L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "REQUEST_TIME")
    private long requestTime;

    @Basic(optional = false)
    @Column(name = "RESPONSE_TIME")
    private long responseTime;

    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MegaUser userId;
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Book bookId;

    public BookRequest() {
    }

    public BookRequest(Integer id) {
        this.id = id;
    }

    public BookRequest(Integer id, long requestTime, long responseTime, int status) {
        this.id = id;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MegaUser getUserId() {
        return userId;
    }

    public void setUserId(MegaUser userId) {
        this.userId = userId;
    }

    public Book getBookId() {
        return bookId;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BookRequest)) {
            return false;
        }
        BookRequest other = (BookRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null &&
                !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jsfprohtml5.megaapp.model.BookRequest[ id=" + id + " ]";
    }

}
