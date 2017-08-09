package com.itr.outlet.walmart.control;

import com.itr.outlet.walmart.boundary.WalmartProductService;
import com.itr.entity.WalmartProduct;
import com.itr.enums.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

@ManagedBean
@ViewScoped
public class ScheduledWalmartProductBean implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(ScheduledWalmartProductBean.class.getName());
    private static final long serialVersionUID = 1L;
    private List<WalmartProduct> products;
    private List<WalmartProduct> selectedWalmartProducts;
    private List<WalmartProduct> filteredWalmartProducts;
    private String selectedZipcode;
    private String status;
    private List<String> galeria;
    private Status statusSelection;
    //private String clasification;

    @EJB
    private WalmartProductService walmartProductService;

    @PostConstruct
    public void init() {
        statusSelection = new Status();
        //clasification = Status.ACTIVE;
        galeria = new ArrayList<>();
        selectedWalmartProducts = new ArrayList<>();
        filteredWalmartProducts = new ArrayList<>();
        products = walmartProductService.findAll();
    }

    public Status getStatusSelection() {
        return statusSelection;
    }

//    public String getClasification() {
//        return clasification;
//    }
//
//    public void setClasification(String clasification) {
//        this.clasification = clasification;
//    }    
    public String getSelectedZipcode() {
        return selectedZipcode;
    }

    public void setSelectedZipcode(String selectedZipcode) {
        this.selectedZipcode = selectedZipcode;
    }

    public List<String> getGaleria() {
        return galeria;
    }

    public void setGaleria(List<String> galeria) {
        this.galeria = galeria;
    }

    public List<WalmartProduct> getSelectedWalmartProducts() {
        return selectedWalmartProducts;
    }

    public void setSelectedWalmartProducts(List<WalmartProduct> selectedWalmartProducts) {
        this.selectedWalmartProducts = selectedWalmartProducts;
    }

    public List<WalmartProduct> getProducts() {
        //this.products = walmartProductService.findAll();
        return this.products;
    }

    public void setProducts(List<WalmartProduct> products) {
        this.products = products;
    }

    public WalmartProductService getWalmartProductService() {
        return walmartProductService;
    }

    public void setWalmartProductService(WalmartProductService productService) {
        this.walmartProductService = productService;
    }

    public List<WalmartProduct> getFilteredWalmartProducts() {
        return filteredWalmartProducts;
    }

    public void setFilteredWalmartProducts(List<WalmartProduct> filteredWalmartProducts) {
        this.filteredWalmartProducts = filteredWalmartProducts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<WalmartProduct> updateAllProducts() {
        this.products = walmartProductService.findAll();
        return this.products;
    }

    public void extractImages(String images) {
        this.galeria = Arrays.asList(images.split(","));

        System.out.println(this.galeria);
    }

    public String countProducts() {
        Long total;
        total = walmartProductService.countProducts();
        return (total > 0 ? "(" + total + ")" : "");
    }

    public String countSubproducts(String status) {
        Long total;
        total = walmartProductService.countSubproducts(status);
        return (total > 0 ? "(" + total + ")" : "");
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("EVENT: " + ((WalmartProduct) event.getObject()).getId());
        WalmartProduct walmartProduct = (WalmartProduct) event.getObject();
        //walmartProduct.setStatus(clasification);
        walmartProductService.update(walmartProduct);
    }

    public void activateProductAction(String status) {

        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq: " + status);

        walmartProductService.activateProduct(selectedWalmartProducts, status);
    }

    public void updateStoreAction() {
        walmartProductService.updateStore(products, selectedZipcode);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

//    public void cellEdit(CellEditEvent event) {
//        Object oldValue = event.getOldValue();
//        Object newValue = event.getNewValue();
//        
//        
//        DataTable dataTable = (DataTable) event.getSource();
//        if (newValue != null && !newValue.equals(oldValue)) {
//            
//            WalmartProduct product = (WalmartProduct) dataTable.getRowData();
//            
//             
//            
//        }
//         
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//    }
}
