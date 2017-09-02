package com.business.exception;

import javax.ejb.EJBException;

public class EmployeeAlreadyHasRoleException extends EJBException {

    private static final long serialVersionUID = -6631663487928999005L;

    public EmployeeAlreadyHasRoleException() {
        super();
    }

    public EmployeeAlreadyHasRoleException(final String message) {
        super(message);
    }
}
