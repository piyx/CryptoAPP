package com.example.stocksandcrypto;

import java.time.LocalDate;
import java.util.HashMap;

enum Timeline {
    ONEDAY, ONEWEEK, ONEMONTH, THREEMONTHS, ONEYEAR, FIVEYEARS
}

public class PricesTimeline {
    public static String[] getStartAndEndDate(Timeline timeline) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (timeline) {
            case ONEDAY:
                startDate = LocalDate.now().minusDays(1);
                break;
            case ONEWEEK:
                startDate = LocalDate.now().minusDays(7);
                break;
            case ONEMONTH:
                startDate = LocalDate.now().minusDays(30);
                break;
            case THREEMONTHS:
                startDate = LocalDate.now().minusDays(90);
                break;
            case ONEYEAR:
                startDate = LocalDate.now().minusDays(365);
                break;
            case FIVEYEARS:
                startDate = LocalDate.now().minusDays(365*5);
                break;
            default:
                startDate = LocalDate.now().minusDays(1);
                break;
        }

        String time = "T00%3A00%3A00Z";
        return new String[]{startDate.toString() + time, endDate.toString() + time};
    }
}