package com.app.leon.abfa.Models.ViewModels;

import java.math.BigDecimal;

/**
 * Created by Leon on 1/16/2018.
 */

public class PrintableObject {
    String value, key;

    public <T1, T2> PrintableObject(T1 value, T2 key) {
        this.key = key.toString();
        if (value instanceof BigDecimal || value instanceof Double) {
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
            nf.setMinimumFractionDigits(2);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(nf.format(Double.parseDouble((String) value)));
            this.value = stringBuilder.toString();
        } else
            this.value = value.toString();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
