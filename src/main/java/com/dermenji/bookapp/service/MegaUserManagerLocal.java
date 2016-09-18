package com.dermenji.bookapp.service;

import com.dermenji.bookapp.model.MegaUser;
import com.dermenji.bookapp.service.exception.UserAlreadyExists;
import com.dermenji.bookapp.service.exception.UserNotFound;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MegaUserManagerLocal {
    public MegaUser getMegaUser(String userID) throws UserNotFound;
    public List<MegaUser> retrieveMegaUsers();
    public MegaUser registerMegaUser(MegaUser user) throws UserAlreadyExists;
    public void removeMegaUser(String userID) throws UserNotFound;
}
