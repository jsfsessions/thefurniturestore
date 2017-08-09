package com.itr.outlet.walmart.boundary;

import java.util.logging.Logger;
import javax.ejb.Stateless;

@Stateless
public class ProductCategoryService {

    private static final Logger logger = Logger.getLogger(ProductCategoryService.class.getName());

    private final String[] category = {"",
        "Fireplaces",
        "Tv stands",
        "Cubes",
        "Office",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "Bed frames",
        "Computer stands"
    };

    public String[] getCategory() {
        return category;
    }
}
