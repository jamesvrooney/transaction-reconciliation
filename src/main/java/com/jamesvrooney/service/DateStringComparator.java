package com.jamesvrooney.service;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class DateStringComparator implements Comparator {

    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public int compare(Object firstDate, Object secondDate) {

        try {
//            return dateTimeFormatter.parse((String)firstDate).compareTo(dateTimeFormatter.parse((String)secondDate));
//            dateTimeFormatter.parse(firstDate.toString()).
            return dateFormat.parse((String) firstDate).compareTo(dateFormat.parse((String) secondDate));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

//new Comparator<String>() {
//        DateFormat f = new SimpleDateFormat("dd/MM/yyyy '@'hh:mm a");
//@Override
//public int compare(String o1, String o2) {
//        try {
//        return f.parse(o1).compareTo(f.parse(o2));
//        } catch (ParseException e) {
//        throw new IllegalArgumentException(e);
//        }
//        }