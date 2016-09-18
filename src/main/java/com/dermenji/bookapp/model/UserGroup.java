package com.dermenji.bookapp.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_GROUP")
public class UserGroup implements Serializable {
    private static final long serialVersionUID = 198213812312319321L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GROUP_ID")
    private Integer groupId;

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private MegaUser userId;

    public UserGroup() {
    }

    public UserGroup(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
    public MegaUser getUserId() {
        return userId;
    }
    public void setUserId(MegaUser userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null &&
                !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.dermenji.bookapp.model.UserGroup[ id=" + id + " ]";
    }

}
