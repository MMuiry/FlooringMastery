package com.mthree.floorMastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

    void print(String message);

    String readString(String prompt);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    LocalDate readDate(String prompt, String format);

    BigDecimal readBigDecimal(String prompt);

    BigDecimal readBigDecimal(String prompt, double  minimum, boolean allowBlank);

    boolean readBoolean(String prompt);

}