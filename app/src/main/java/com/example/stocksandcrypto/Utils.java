package com.example.stocksandcrypto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    static Double TRILLION = 1_000_000_000_000d;
    static Double BILLION = 1_000_000_000d;
    static Double MILLION = 1_000_000d;
    static Double THOUSAND = 1_000d;

    public static String formatLargeNumber(String number) {
        Double num;
        try {
            num = Double.parseDouble(number);
        } catch (Exception e) {
            return "-";
        }
        Double formattedNum;
        String suffix = "";
        if (num > TRILLION) { formattedNum = num/TRILLION; suffix = "T"; }
        else if (num > BILLION) { formattedNum = num/BILLION; suffix = "B";}
        else if (num > MILLION) { formattedNum = num/MILLION; suffix = "M"; }
        else if (num > THOUSAND) { formattedNum = num/THOUSAND; suffix = "K"; }
        else { formattedNum = num; }
        return String.format("%.2f%s", formattedNum, suffix);
    }

    public static String formatDate(String date) {
        date = date.substring(0, 10);
        LocalDate datetime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
