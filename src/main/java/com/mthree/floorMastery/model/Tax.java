package com.mthree.floorMastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    String state;
    String stateAbr;
    String taxRate;

    //Getters
    public String getState() {
        return state;
    }

    public String getStateAbr() {
        return stateAbr;
    }

    public String getTaxRate() {
        return taxRate;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(state, tax.state) && Objects.equals(stateAbr, tax.stateAbr) && Objects.equals(taxRate, tax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, stateAbr, taxRate);
    }

    @Override
    public String toString() {
        return "Tax{" +
                "state='" + state + '\'' +
                ", stateAbr='" + stateAbr + '\'' +
                ", taxRate='" + taxRate + '\'' +
                '}';
    }
}
