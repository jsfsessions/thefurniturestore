package com.itr.outlet.walmart.boundary;

import com.itr.entity.WalmartProduct;
import static com.itr.twilio.TwilioCredentials.DEFAULT_APPLICATION_PHONE_NUMBER;
import com.itr.twilio.TwilioHelper;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
@DeclareRoles({"adminRole"})
@RolesAllowed({"adminRole"})
public class MessageService {

    @EJB
    private EmployeeService employeeService;

    @Asynchronous
    public void sendMessagesAsynchronously(List<WalmartProduct> products) {

        final List<String> employeesPhoneNumbers = employeeService.findEmployeesPhoneNumbers();

        products.forEach((product) -> {
            String messageBody = "Product: " + product.getProductName()
                    + "\nCategory: " + product.getCategory()
                    + "\nBin: " + product.getBinLocation()
                    + "\nPrice: " + product.getSalePrice();

            employeesPhoneNumbers.forEach((toPhoneNumber) -> {
                TwilioHelper.sendMessage(toPhoneNumber, DEFAULT_APPLICATION_PHONE_NUMBER, messageBody, product.getImgUrl());
            });
        });
    }
}
