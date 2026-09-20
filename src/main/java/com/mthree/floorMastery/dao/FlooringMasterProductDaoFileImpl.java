package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class FlooringMasterProductDaoFileImpl implements FlooringMasterProductDao {
    private Map<String,Product> allProducts = new HashMap<>();
    final String PRODUCT_FILE = "Products.txt" ;
    final String DELIMITER = ",";

    @Override
    public void loadFile() throws PersistenceException {
        Scanner sc;
        try {
            sc = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Product file could not be loaded into memory", e);
        }
        String[] productInfo;
        String productType;
        BigDecimal costPerSquareFoot;
        BigDecimal laborCostperSquareFoot;

        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isBlank()) {
                continue;
            }
            productInfo = line.split(DELIMITER);
            productType = productInfo[0];
            costPerSquareFoot = new BigDecimal(productInfo[1]);
            laborCostperSquareFoot = new BigDecimal(productInfo[2]);
            Product currentProduct =  new Product(productType,costPerSquareFoot,laborCostperSquareFoot);
            allProducts.put(productType,currentProduct);
        }
        sc.close();
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts.values());
    }
}
