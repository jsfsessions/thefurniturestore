package com.itr.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address implements java.io.Serializable {

    private static final long serialVersionUID = 42L;

    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
