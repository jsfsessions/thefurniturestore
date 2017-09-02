package com.web.view;

import com.business.service.SalesReportService;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class SalesReportBean implements Serializable {

    private static final long serialVersionUID = -7608634284184143368L;

    @EJB
    private SalesReportService salesReportService;

    private Date startDate;
    private Date endDate;
    private List<Object> soldProducts;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Object> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<Object> soldProducts) {
        this.soldProducts = soldProducts;
    }

    public void searchBetweenDates() {
        LocalDateTime fromStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime fromEndDate = null;
        if (endDate != null) {
            fromEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        soldProducts = salesReportService.getReportBetweenDays(fromStartDate, fromEndDate);
    }
}
