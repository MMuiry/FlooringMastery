package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.model.Tax;

import java.io.FileNotFoundException;
import java.util.List;

public interface FlooringMasterTaxDao {
    void loadFile() throws FileNotFoundException;
    List<Tax> getAllTaxes();
}
