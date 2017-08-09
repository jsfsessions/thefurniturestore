package com.itr.helpers;

import com.itr.outlet.walmart.boundary.WalmartProductService;
//import com.itr.entity.Product;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Startup
@Singleton
public class PopulateProductService implements Serializable {

    private static final long serialVersionUID = 42L;
    private final static String[] colors = new String[10];
    List<String> imgUrl;
    String url = "https://i5.walmartimages.com/asr/2c1f4706-5196-4d7e-8ae4-6bed03955bf1_1.35f06d5caf3caf8336eb52f895f5bff4.jpeg?odnHeight=450&odnWidth=450&odnBg=FFFFFF";

    @EJB
    WalmartProductService productService;

    @PostConstruct
    public void init() {
        
        colors[0] = "Black";
        colors[1] = "White";
        colors[2] = "Green";
        colors[3] = "Red";
        colors[4] = "Blue";
        colors[5] = "Orange";
        colors[6] = "Silver";
        colors[7] = "Yellow";
        colors[8] = "Brown";
        colors[9] = "Maroon";
        
        /*

        Product product1 = new Product();
        //product1.setId(34222l);
        product1.setProductId("48582");
        product1.setUpc("63523492");
        product1.setProductNameFromProforma("sofa bed");
        product1.setBrandName("macys");
        product1.setSalePrice(45f);
        product1.setBinLocation("1A");
        product1.setImgUrl(url);
        product1.setColor(colors[1]);
        productService.addProduct(product1);

        Product product2 = new Product();
        //product2.setId(75682l);
        product2.setProductId("46673424");
        product2.setUpc("1363292");
        product2.setProductNameFromProforma("cruise sofa chair");
        product2.setBrandName("macys");
        product2.setSalePrice(245f);
        product2.setBinLocation("2E");
        product2.setImgUrl(url);
        productService.addProduct(product2);

        Product product3 = new Product();
        //product3.setId(5422l);
        product3.setProductId("63554");
        product3.setUpc("24533452");
        product3.setProductNameFromProforma("sofa bed");
        product3.setBrandName("macys");
        product3.setSalePrice(235f);
        product3.setBinLocation("2D");
        product3.setImgUrl(url);
        productService.addProduct(product3);

        Product product4 = new Product();
        //product4.setId(52223L);
        product4.setProductId("6543544");
        product4.setUpc("1239292");
        product4.setProductNameFromProforma("BED frame");
        product4.setBrandName("wayfair");
        product4.setSalePrice(245f);
        product4.setBinLocation("14C");
        product4.setImgUrl(url);
        productService.addProduct(product4);

        Product product5 = new Product();
        //product5.setId(7654L);
        product5.setProductId("78532");
        product5.setUpc("35423492");
        product5.setProductNameFromProforma("fire place");
        product5.setBrandName("amazon");
        product5.setSalePrice(80f);
        product5.setBinLocation("12A");
        product5.setImgUrl(url);
        productService.addProduct(product5);

        Product product6 = new Product();
        //product6.setId(87544L);
        product6.setProductId("55443");
        product6.setUpc("2453");
        product6.setProductNameFromProforma("tv stand");
        product6.setBrandName("better beds");
        product6.setSalePrice(65f);
        product6.setBinLocation("21A");
        product6.setImgUrl(url);
        productService.addProduct(product6);

        Product product7 = new Product();
        //product7.setId(34222l);
        product7.setProductId("87875");
        product7.setUpc("345124");
        product7.setProductNameFromProforma("12 cubes books");
        product7.setBrandName("far away books");
        product7.setSalePrice(25f);
        product7.setBinLocation("21F");
        product7.setImgUrl(url);
        productService.addProduct(product7);

        Product product8 = new Product();
        //product8.setId(34222l);
        product8.setProductId("452454");
        product8.setUpc("67566");
        product8.setProductNameFromProforma("bunk bed");
        product8.setBrandName("better beds");
        product8.setSalePrice(145f);
        product8.setBinLocation("15b");
        product8.setImgUrl(url);
        productService.addProduct(product8);

*/
    }
}
