package com.business.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class Employee implements Serializable {

    private static final long serialVersionUID = 3636381708060681199L;

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "firstname", length = 20, nullable = false)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "lastname", length = 30, nullable = false)
    private String lastname;

    @NotNull
    @Size(min = 6, max = 30)
    @Column(name = "username", length = 30, unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "email", length = 50, unique = true, nullable = false)
    private String email;

    @NotNull
    @Size(min = 6, max = 50)
    @Column(name = "address", length = 50, nullable = false)
    private String address;

    @NotNull
    @Size(max = 20)
    @Column(name = "phoneNumber", length = 20, nullable = false)
    private String phoneNumber;

    public Employee() {
    }

    public Employee(String firstname, String lastname, String username, String email, String address, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.email);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employee other = (Employee) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", email=" + email + '}';
    }
}
