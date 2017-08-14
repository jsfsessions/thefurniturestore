package com.users;

import com.exceptions.EmailAlreadyExistException;
import com.exceptions.EmployeeAlreadyHasRoleException;
import com.exceptions.UsernameAlreadyExistException;
import com.itr.outlet.walmart.boundary.EmployeeService;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.omnifaces.util.Messages;

@Named
@RequestScoped
public class UserSignUp {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String INVALID_EMAIL_MSG = "Please enter a valid email address";

    @EJB
    private EmployeeService employeeService;

    @NotNull
    @Size(min = 2, max = 20)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastname;

    @NotNull
    @Size(min = 6, max = 30)
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    @NotNull
    @Size(min = 6, max = 50)
    @Pattern(regexp = EMAIL_REGEX, message = INVALID_EMAIL_MSG)
    private String email;

    @NotNull
    @Size(min = 6, max = 50)
    private String address;

    @NotNull
    @Size(max = 15)
    private String phoneNumber;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String signUpAction() {
        try {
            // Hash password from string
            String hashedPassword = employeeService.hashPassword(password);

            // Save to database
            employeeService.registerEmployee(firstname, lastname, username, hashedPassword, email, address, phoneNumber);
            return "login?faces-redirect=true";

        } catch (UsernameAlreadyExistException e) {
            Messages.addGlobal(FacesMessage.SEVERITY_INFO, e.getMessage() + " Please insert a different one.");
        } catch (EmailAlreadyExistException e) {
            Messages.addGlobal(FacesMessage.SEVERITY_INFO, e.getMessage() + " Please insert a different one or try to Sign in.");
        } catch (EmployeeAlreadyHasRoleException e) {
            Messages.addGlobal(FacesMessage.SEVERITY_INFO, e.getMessage() + " Please insert a different one or contact administrator.");
        } catch (NoSuchAlgorithmException e) {
            Messages.addGlobal(FacesMessage.SEVERITY_ERROR, "A server error occurred. Please try again or contact administrator.");
        }
        return "login";
    }
}
