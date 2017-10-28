package com.web.view;

import com.business.entity.Employee;
import com.business.entity.WalmartProduct;
import com.business.service.EmployeeService;
import com.business.service.MessageService;
import com.business.service.WalmartProductService;
import com.business.service.ZipcodeService;
import com.util.Zipcode;
import com.util.constants.Status;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class WalmartProductBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ProductCount productCount;

    @Inject
    private EmployeesBean employeesBean;

    @EJB
    private MessageService messageService;

    @EJB
    private EmployeeService employeeService;

    @EJB
    private ZipcodeService zipcodeService;

    @EJB
    private WalmartProductService walmartProductService;

    private String soldBy;
    private Date soldDate;
    private String selectedZipcode;

    private List<String> galeria;
    private List<Zipcode> zipcodes;
    private List<Employee> selectedEmployees;
    private WalmartProduct newWalmartProduct;
    private WalmartProduct selectedWalmartProduct;
    private LazyDataModel<WalmartProduct> lazyModel;
    private List<WalmartProduct> selectedWalmartProducts;

    @PostConstruct
    public void init() {
        galeria = new ArrayList<>();
        selectedEmployees = new ArrayList<>();
        newWalmartProduct = new WalmartProduct();
        selectedWalmartProducts = new ArrayList<>();
        zipcodes = zipcodeService.populate();

        final String viewName = Faces.getViewName().toUpperCase();   // Use page name as filter to select only products with status = page name

        this.lazyModel = new LazyDataModel<WalmartProduct>() {
            private static final long serialVersionUID = 1L;

            private List<WalmartProduct> datasource;

            @Override
            public WalmartProduct getRowData(String rowKey) {
                for (WalmartProduct product : datasource) {
                    if (rowKey.equals(product.getId().toString())) {
                        return product;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(WalmartProduct product) {
                return product.getId();
            }

            @Override
            public List<WalmartProduct> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                datasource = walmartProductService.findPage(first, pageSize, viewName, sortField, sortOrder, filters);
                lazyModel.setRowCount(walmartProductService.findPage(-1, -1, viewName, sortField, sortOrder, filters).size());
                return datasource;
            }
        };
    }

    public WalmartProduct getNewWalmartProduct() {
        return newWalmartProduct;
    }

    public void setNewWalmartProduct(WalmartProduct newWalmartProduct) {
        this.newWalmartProduct = newWalmartProduct;
    }

    public Status getStatus() {
        return new Status();
    }

    public List<String> getGaleria() {
        return galeria;
    }

    public List<Zipcode> getZipcodes() {
        return zipcodes;
    }

    public LazyDataModel<WalmartProduct> getLazyModel() {
        return lazyModel;
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

    public String getSelectedZipcode() {
        return selectedZipcode;
    }

    public void setSelectedZipcode(String selectedZipcode) {
        this.selectedZipcode = selectedZipcode;
    }

    public List<Employee> getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(List<Employee> selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public List<WalmartProduct> getSelectedWalmartProducts() {
        return selectedWalmartProducts;
    }

    public void setSelectedWalmartProducts(List<WalmartProduct> selectedWalmartProducts) {
        this.selectedWalmartProducts = selectedWalmartProducts;
    }

    public void extractImages(String images) {
        this.galeria = Arrays.asList(images.split(","));
        System.out.println(this.galeria);
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
        productCount.setCounts(null);
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
        walmartProductService.update(walmartProduct);
    }

    public void activateProductAction(String status) {
        walmartProductService.activateProduct(selectedWalmartProducts, status);
    }

    public void updateStoreAction() {
        if (selectedWalmartProducts.isEmpty()) {
            Faces.getContext().addMessage(null, new FacesMessage("Select from the table the product(s) that need shipping"
                    + " and then click the 'Ship' button."));
        } else {
            walmartProductService.updateStore(selectedWalmartProducts, selectedZipcode);

            Faces.getContext().addMessage(null, new FacesMessage(String.format("Product(s) shipped to store '%s'.", selectedZipcode)));
            Ajax.update("storeForm", "productTableForm:mul-sel-dat");
            // Reset selections
            selectedWalmartProducts = new ArrayList<>();
            selectedZipcode = null;
        }
    }

    public void sendSMS() {
        if (selectedEmployees.isEmpty() || selectedWalmartProducts.isEmpty()) {
            Faces.getContext().addMessage(null, new FacesMessage("Select the product(s) to be sent via SMS "
                    + " and the employee(s) to receive it, and then click the 'Send SMS' button."));
        } else {

            messageService.sendMessages(selectedEmployees, selectedWalmartProducts);

            Faces.getContext().addMessage(null, new FacesMessage("Sending " + selectedWalmartProducts.size() + " SMS to "
                    + selectedEmployees.size() + " employee(s).. This may take around "
                    + (selectedWalmartProducts.size() * selectedEmployees.size()) + " seconds.."));

            Ajax.update("emp-frm", "productTableForm:mul-sel-dat");
            // Reset selections
            selectedWalmartProducts = new ArrayList<>();
            selectedEmployees = new ArrayList<>();
        }
    }

    public void deleteEmployee() {
        if (selectedEmployees.isEmpty()) {
            Faces.getContext().addMessage(null, new FacesMessage("Select the employee(s) that you wish to be deleted."));
        } else {

            int employeesDeleted = employeeService.deleteEmployees(selectedEmployees) / 2;

            if (selectedEmployees.size() == employeesDeleted) {
                Faces.getContext().addMessage(null, new FacesMessage("Selected employee(s) have been successfully deleted."));
            } else {
                Faces.getContext().addMessage(null, new FacesMessage("An error occured and employee(s) may not have been deleted."));
            }

            Ajax.update("emp-frm");
            // Reset selections and employees list kept in session
            selectedEmployees = new ArrayList<>();
            employeesBean.setEmployees(null);
        }
    }

    public void addProduct() {
        walmartProductService.addProduct(newWalmartProduct);
        RequestContext.getCurrentInstance().execute("PF('addProductDialog').hide()");
        Faces.getContext().addMessage(null, new FacesMessage("Product successfully saved.."));

        newWalmartProduct = new WalmartProduct();
    }
}
