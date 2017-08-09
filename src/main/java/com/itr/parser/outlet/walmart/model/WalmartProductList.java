package com.itr.parser.outlet.walmart.model;

import com.itr.entity.WalmartProduct;
import java.util.List;
import java.util.stream.Stream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Products")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalmartProductList {

    private List<WalmartProduct> products;

    public List<WalmartProduct> getProducts() {
        return products;
    }

    public WalmartProductList setProducts(List<WalmartProduct> products) {
        this.products = products;
        return this;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public Stream<WalmartProduct> stream() {
        return products.stream();
    }
}
