package com.mthree.floorMastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostperSquareFoot;

    public Product(String productType,  BigDecimal costPerSquareFoot, BigDecimal laborCostperSquareFoot) {
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostperSquareFoot = laborCostperSquareFoot;
    }

    //getters
    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostPerSquareFoot() {
        return costPerSquareFoot;
    }

    public BigDecimal getLaborCostperSquareFoot() {
        return laborCostperSquareFoot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productType, product.productType) && Objects.equals(costPerSquareFoot, product.costPerSquareFoot) && Objects.equals(laborCostperSquareFoot, product.laborCostperSquareFoot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, costPerSquareFoot, laborCostperSquareFoot);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productType='" + productType + '\'' +
                ", costPerSquareFoot=" + costPerSquareFoot +
                ", laborCostperSquareFoot=" + laborCostperSquareFoot +
                '}';
    }
}
