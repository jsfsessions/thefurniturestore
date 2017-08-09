package com.itr.parser.outlet.walmart.service;

import java.io.File;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.itr.parser.outlet.walmart.model.WalmartProductList;

public class XmlDeserializingService {

    private File source;

    public XmlDeserializingService(File source) {
        this.source = source;
    }

    public XmlDeserializingService setSource(File source) {
        this.source = source;
        return this;
    }

    public WalmartProductList deserializeBatchDocument() {
        Objects.requireNonNull(source, "File is not set");
        WalmartProductList products = new WalmartProductList();

        try {
            JAXBContext context = JAXBContext.newInstance(WalmartProductList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            products = (WalmartProductList) unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
        }

        return products;
    }
}
