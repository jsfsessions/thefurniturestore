package com.itr.outlet.walmart.control;

import com.itr.outlet.walmart.boundary.WalmartProductService;
import com.itr.entity.WalmartProduct;
import com.itr.enums.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

@Named
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
    private WalmartProduct selectedWalmartProduct;
    private String soldBy;
    private Date soldDate;  // We should use Java 8 LocalDate, but it is not currently supported by PrimeFaces p:calendar
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

        if (Faces.getExternalContext().isUserInRole("adminRole")) {
            products = walmartProductService.findAll();
        } else {
            products = walmartProductService.findAllForUserRoles();
        }
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

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public Date getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(Date soldDate) {
        this.soldDate = soldDate;
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

    // When a row switches to edit mode, keep the walmart product
    public void onRowEditInit(RowEditEvent event) {
        selectedWalmartProduct = (WalmartProduct) event.getObject();
    }

    // After product status is changed, catch new status value and update its respective datestamp
    public void statusChangeListener(ValueChangeEvent e) {
        LocalDateTime now = LocalDateTime.now();

        switch (e.getNewValue().toString()) {
            case Status.SCHEDULED:
                selectedWalmartProduct.setScheduledDatestamp(now);
                break;
            case Status.ACTIVE:
                selectedWalmartProduct.setActiveDatestamp(now);
                break;
            case Status.SOLD:
                selectedWalmartProduct.setSoldDatestamp(now);
                RequestContext.getCurrentInstance().execute("PF('soldInfoDlg').show()");
                break;
            case Status.AGING:
                selectedWalmartProduct.setAgingDatestamp(now);
                break;
            case Status.ASSEMBLE:
                selectedWalmartProduct.setAssembleDatestamp(now);
                break;
            case Status.PARTIAL:
                selectedWalmartProduct.setPartialDatestamp(now);
                break;
            case Status.DAMAGED:
                selectedWalmartProduct.setDamagedDatestamp(now);
                break;
            case Status.SHRINKAGE:
                selectedWalmartProduct.setShrinkageDatestamp(now);
                break;
            default:
                break;
        }
    }

    public void saveSoldInformations(ActionEvent e) {
        // Save sold informations
        walmartProductService.addSoldProduct(selectedWalmartProduct.getId(), soldBy, soldDate);
        RequestContext.getCurrentInstance().execute("PF('soldInfoDlg').hide()");

        // Reset dialog input fields
        soldBy = "";
        soldDate = null;
    }

    public void onRowEdit(RowEditEvent event) {
        System.out.println("EVENT: " + ((WalmartProduct) event.getObject()).getId());
        WalmartProduct walmartProduct = (WalmartProduct) event.getObject();
        //walmartProduct.setStatus(clasification);
        // The update also includes changes made in statusChangeListener() method
        walmartProductService.update(walmartProduct);
    }

    public void onRowEditCancel(RowEditEvent event) {
        // Do nothing and remove this method and the event="rowEditCancel" from scheduled.xhtml,
        // or display a growl with message like "edit cancelled"
        // But don't use the same onRowEdit() listener method on event="rowEditCancel" so that the database update won't fire
        // if nothing has changed
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
