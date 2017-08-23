package com.validators;

import com.itr.twilio.TwilioHelper;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("com.itr.phoneNumberValidator")
public class PhoneNumberValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        /* Valid formats:
                (734) 555 1212
                (734) 555.1212
                (734) 555-1212
                (734) 5551212
                (734)5551212
                734 555 1212
                734.555.1212
                734-555-1212
                7345551212
         */
        if (!TwilioHelper.isValidPhoneNumber(value.toString())) {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
                    "Please enter a valid phone number.");

            throw new ValidatorException(message);
        }
    }
}
