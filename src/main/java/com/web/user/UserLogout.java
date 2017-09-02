package com.web.user;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class UserLogout {

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        ec.redirect(ec.getRequestContextPath());
        System.out.println("ec.getRequestContextPath() " + ec.getRequestContextPath());
    }
}
