package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.model.Product;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface FlooringMasterProductDao {
    void loadFile() throws FileNotFoundException;
    List<Product> getAllProducts();
}
