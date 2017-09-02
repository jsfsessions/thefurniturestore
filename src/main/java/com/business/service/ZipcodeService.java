package com.business.service;

import com.util.Zipcode;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ZipcodeService {

    public List<Zipcode> populate() {

        List<Zipcode> zipcodes = new ArrayList<>();
        zipcodes.add(new Zipcode("75207", "Irving"));
        zipcodes.add(new Zipcode("75247", "Topline"));
        zipcodes.add(new Zipcode("73129", "SE 15th"));
        return zipcodes;
    }
}
