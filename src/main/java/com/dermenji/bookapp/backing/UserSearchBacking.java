package com.dermenji.bookapp.backing;

import com.dermenji.bookapp.model.MegaUser;
import com.dermenji.bookapp.service.MegaUserManagerLocal;
import com.dermenji.bookapp.service.exception.UserNotFound;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named
@ViewScoped
public class UserSearchBacking extends BaseBacking implements Serializable {

    @EJB
    private MegaUserManagerLocal userManager;

    private List<MegaUser> userList;
    private String infoMessage;
    private MegaUser selectedUser;


    public List<MegaUser> getUserList() {
        return userList;
    }

    public void setUserList(List<MegaUser> userList) {
        this.userList = userList;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public MegaUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(MegaUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String retrieveUserList() {
        userList = userManager.retrieveMegaUsers();

        if (userList.isEmpty()) {
            infoMessage = "No Users found!";
        } else {
            infoMessage = userList.size() + " user(s) found";
        }

        return null;
    }

    public String deleteUser() {
        MegaUser currentSelectedUser = getSelectedUser();
        try {
            userManager.removeMegaUser(currentSelectedUser.getId());
            userList.remove(currentSelectedUser);
            infoMessage = "User deleted successfully";
        } catch (UserNotFound ex) {
            Logger.getLogger(UserSearchBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while deleting user"));
        }

        return null;
    }
}
