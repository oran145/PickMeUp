package com.example.liron.finalproject.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Oran on 06/08/2017.
 */

public class utilities {
    public static long convertDateStringToMillis(String date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // day/month/year

        Date d = null;
        try {
            d = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = d.getTime();
        return millis;
    }

}
