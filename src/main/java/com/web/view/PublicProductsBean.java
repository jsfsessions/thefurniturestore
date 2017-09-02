package com.web.view;

import com.business.entity.WalmartProduct;
import com.business.service.WalmartProductService;
import com.util.constants.SearchBy;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class PublicProductsBean implements Serializable {

    private static final long serialVersionUID = -6536272133532560174L;

    @EJB
    private WalmartProductService walmartProductService;

    private int page;
    private int pageSize = 50;
    private String searchText = "";
    private String filter = SearchBy.ALL;
    private List<WalmartProduct> products;

    @PostConstruct
    public void init() {
        findProducts();
    }

    public List<WalmartProduct> getProducts() {
        return products;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void searchAll(ActionEvent actionEvent) {
        page = 0;
        filter = SearchBy.ALL;
        findProducts();
    }

    public void searchByCategory(ActionEvent actionEvent) {
        page = 0;
        filter = SearchBy.CATEGORY;
        findProducts();
    }

    public void searchByName(ActionEvent actionEvent) {
        page = 0;
        filter = SearchBy.PRODUCT_NAME;
        findProducts();
    }

    public void findProducts() {
        products = walmartProductService.findAllActive(0, pageSize, filter, searchText);
    }

    public void findNextProducts() {
        page++;
        products = walmartProductService.findAllActive(page, pageSize, filter, searchText);
    }

    public void findPreviousProducts() {
        page--;
        products = walmartProductService.findAllActive(page, pageSize, filter, searchText);
    }
}
