package com.olinnova.mentordoctor.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


import java.text.SimpleDateFormat;

import java.util.TimeZone;

public class GetDateActual {

    private static final String MEXICO_TIME_ZONE = "America/Mexico_City";

    public static String getDateActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(MEXICO_TIME_ZONE));
        return dateFormat.format(new Date());
    }

    public static String getTimeActual() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone(MEXICO_TIME_ZONE));
        return timeFormat.format(new Date());
    }
}