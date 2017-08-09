
/*
package com.itr.control;

import com.itr.boundary.WalmartProductService;
import com.itr.entity.Product;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@SessionScoped
public class LazyLoadProductBean implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(ProformaFileHandlerBean.class.getName());
    private static final long serialVersionUID = 1L;
    private Product product;
    private List<Product> filteredProducts;
    private LazyDataModel<Product> lazyModel;

    @EJB
    private WalmartProductService productService;

    @PostConstruct
    public void init() {

        this.lazyModel = new LazyDataModel<Product>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Product getRowData(String rowKey) {

                for (Product p : filteredProducts) {
                    if (String.valueOf(p.getId()).equals(rowKey)) {
                        System.out.println("String.valueOf(p.getId()).equals(rowKey): " + rowKey);
                        LOGGER.log(Level.INFO, "product is : {0}", p);
                        return p;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Product p) {
                LOGGER.log(Level.INFO, "product id is : {0}", p.getId());
                return p.getId();
            }

            @Override
            public List<Product> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                filteredProducts = productService.findPage(first, pageSize, sortField, sortOrder, filters);
                lazyModel.setRowCount(productService.findPage(-1, -1, null, null, filters).size());
                return filteredProducts;
            }
        };
    }

    public LazyDataModel<Product> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Product> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    public void saveProductAction() {
        // NOPE
    }

    //this applies to findAll in WalmartProductService.java
//    public List<Product> retrieveAll() {
//        return productService.findAll();
//    }
    public void onRowSelect(SelectEvent event) {
        //FacesMessage msg = new FacesMessage("Product Selected", ((Product) event.getObject()).getId().toString());
        //FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
*/