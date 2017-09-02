package com.web.view;

import com.business.service.WalmartProductService;
import com.util.constants.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class ProductCount implements Serializable {

    private static final long serialVersionUID = 8307095944185204565L;

    @EJB
    private WalmartProductService walmartProductService;

    private List<Long> counts;

    public List<Long> getCounts() {
        if (counts == null) {
            counts = new ArrayList<>();
            counts.add(walmartProductService.countProductsWithoutStatus() + walmartProductService.countSubproducts(Status.SCHEDULED));
            counts.add(walmartProductService.countSubproducts(Status.ACTIVE));
            counts.add(walmartProductService.countSubproducts(Status.SOLD));
            counts.add(walmartProductService.countSubproducts(Status.AGING));
            counts.add(walmartProductService.countSubproducts(Status.ASSEMBLE));
            counts.add(walmartProductService.countSubproducts(Status.PARTIAL));
            counts.add(walmartProductService.countSubproducts(Status.DAMAGED));
            counts.add(walmartProductService.countSubproducts(Status.SHRINKAGE));
        }
        return counts;
    }

    /* To avoid hitting the database with COUNT queries when we switch between pages, keep all count values
     * into a session bean. After a product status changes, destroy the List holding the count so that it
     * will reinitialize.
     */
    public void setCounts(List<Long> counts) {
        this.counts = counts;
    }
}
