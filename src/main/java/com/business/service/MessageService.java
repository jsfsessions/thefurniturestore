package com.business.service;

import com.business.entity.Employee;
import com.business.entity.WalmartProduct;
import static com.util.twilio.TwilioCredentials.DEFAULT_APPLICATION_PHONE_NUMBER;
import com.util.twilio.TwilioHelper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

@Stateless
@DeclareRoles({"adminRole"})
@RolesAllowed({"adminRole"})
public class MessageService {

    @Asynchronous
    public void sendMessages(List<Employee> employees, List<WalmartProduct> products) {

        products.forEach((product) -> {
            String messageBody = "Product: " + product.getProductName()
                    + "\nCategory: " + product.getCategory()
                    + "\nBin: " + product.getBinLocation()
                    + "\nPrice: " + product.getSalePrice();

            employees.forEach((employee) -> {
                TwilioHelper.sendMessage(employee.getPhoneNumber(), DEFAULT_APPLICATION_PHONE_NUMBER, messageBody, product.getImgUrl());
                // TwilioHelper.sendMessage(employee.getPhoneNumber(), TEST_PHONE_NUMBER, messageBody, product.getImgUrl());

                sleep(1);   // Twillio restricts to 1 SMS per second. See here: https://goo.gl/w6XVpS
            });
        });
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
