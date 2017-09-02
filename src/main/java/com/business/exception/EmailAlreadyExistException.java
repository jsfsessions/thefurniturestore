package com.business.exception;

import javax.ejb.EJBException;

public class EmailAlreadyExistException extends EJBException {

    private static final long serialVersionUID = -1092996466169517962L;

    public EmailAlreadyExistException() {
        super();
    }

    public EmailAlreadyExistException(final String message) {
        super(message);
    }
}
