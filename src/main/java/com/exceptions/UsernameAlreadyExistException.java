package com.exceptions;

import javax.ejb.EJBException;

public class UsernameAlreadyExistException extends EJBException {

    private static final long serialVersionUID = -3157190740484987311L;

    public UsernameAlreadyExistException() {
        super();
    }

    public UsernameAlreadyExistException(final String message) {
        super(message);
    }
}
