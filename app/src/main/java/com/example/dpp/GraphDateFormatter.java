package com.example.dpp;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GraphDateFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Long millis = ((long) value) * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        return sdf.format(millis);
    }
}
