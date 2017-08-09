package com.itr.outlet.walmart.control;

import com.itr.entity.Zipcode;
import com.itr.outlet.walmart.boundary.ZipcodeService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ZipcodeBean implements Serializable {
    
    private List<Zipcode> zipcodes;
    private Zipcode selectedZipcode;
    
    @EJB
    ZipcodeService zipcodeService;
    
    @PostConstruct
    public void init() { 
        zipcodes = zipcodeService.populate();
    }

    public List<Zipcode> getZipcodes() {
        return zipcodes;
    }

    public void setZipcodeService(ZipcodeService zipcodeService) {
        this.zipcodeService = zipcodeService;
    }

    public Zipcode getSelectedZipcode() {
        return selectedZipcode;
    }

    public void setSelectedZipcode(Zipcode selectedZipcode) {
        this.selectedZipcode = selectedZipcode;
    }
}
