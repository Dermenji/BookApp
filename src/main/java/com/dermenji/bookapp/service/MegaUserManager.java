package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.Constants;
import com.dermenji.bookapp.model.MegaUser;
import com.dermenji.bookapp.model.UserGroup;
import com.dermenji.bookapp.service.exception.UserAlreadyExists;
import com.dermenji.bookapp.service.exception.UserNotFound;

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
public class MegaUserManager implements MegaUserManagerLocal {

    @PersistenceContext(unitName = "megaAppUnit")
    EntityManager em;

    @Override
    public MegaUser getMegaUser(String userID) throws UserNotFound {
        Query query = em.createQuery("select megaUser.id, megaUser.firstName"
                + ", megaUser.lastName from MegaUser megaUser where "
                + "megaUser.id = :id");
        query.setParameter("id", userID);
        Object[] megaUserInfo;
        try {
            megaUserInfo = (Object[]) query.getSingleResult();
        } catch (NoResultException exception) {
            throw new UserNotFound(exception.getMessage());
        }
        MegaUser megaUser = new MegaUser(
                (String) megaUserInfo[0],
                (String) megaUserInfo[1],
                (String) megaUserInfo[2],
                null);
        return megaUser;
    }

    @Override
    public MegaUser registerMegaUser(MegaUser user) throws UserAlreadyExists {
        Query query = em.createQuery("select megaUser from MegaUser megaUser where "
                + "megaUser.id = :userID");
        query.setParameter("userID", user.getId());
        try {
            query.getSingleResult();
            throw new UserAlreadyExists();
        } catch (NoResultException exception) {
            Logger.getLogger(BookManager.class.getName()).log(Level.FINER, "No user found");
        }
        List<UserGroup> userGroups = new ArrayList<UserGroup>();
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(user);
        userGroup.setGroupId(Constants.USER_GROUP);
        userGroups.add(userGroup);
        user.setUserGroupList(userGroups);
        em.persist(user);
        em.flush();
        return user;
    }

    @Override
    public void removeMegaUser(String userID) throws UserNotFound {
        MegaUser megaUser = em.find(MegaUser.class, userID);
        if (megaUser == null) {
            throw new UserNotFound();
        }
        em.remove(megaUser);
        em.flush();
    }

    @Override
    public List<MegaUser> retrieveMegaUsers() {
        Query query = em.createQuery("select megaUser from MegaUser megaUser", MegaUser.class);
        List<MegaUser> result = query.getResultList();
        if (result == null) {
            return new ArrayList<MegaUser>();
        }
        return result;
    }
}
