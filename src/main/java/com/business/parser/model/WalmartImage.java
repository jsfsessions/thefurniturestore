package com.business.parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class wrapper to write images as array in xml.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class WalmartImage {

    private String link;

    public WalmartImage() {
    }

    public WalmartImage(String link) {
        this.link = link;
    }

    public WalmartImage setLink(String link) {
        this.link = link;
        return this;
    }

    public String getLink() {
        return link;
    }
}
