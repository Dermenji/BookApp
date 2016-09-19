package com.dermenji.bookapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;



@FacesConverter("com.dermrnji.bookapp.TimeConverter")
public class TimeConverter implements Converter {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        long timestamp;

        try {
            timestamp = Long.parseLong(value);
        } catch (NumberFormatException numberFormatException) {

            // Try to get the time from the input format
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            try {
                Date date = formatter.parse(value);

                timestamp = date.getTime();
            } catch (ParseException parseException) {
                throw new ConverterException("Invalid Date format", parseException);
            }
        }

        return timestamp;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        long timestamp = Long.parseLong(value.toString());

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
        String formattedDate = formatter.format(new Date(timestamp));

        return formattedDate;
    }
}
