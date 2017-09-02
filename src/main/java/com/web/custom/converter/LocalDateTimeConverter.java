package com.web.custom.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Add support for Java 8 LocalDateTime class in JSF 2.2
 */
@FacesConverter(value = "localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return LocalDateTime.parse(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        LocalDateTime dateValue = (LocalDateTime) value;
        return dateValue.format(DateTimeFormatter.ofPattern("MM-dd-yyyy, h:mm"));
    }
}
