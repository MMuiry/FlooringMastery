package com.mthree.floorMastery.dao;


import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class FlooringMasterTaxDaoFileImpl implements FlooringMasterTaxDao {
    private Map<String, Tax> allTaxes = new HashMap<>();
    final String TAX_FILE = "Taxes.txt" ;
    final String DELIMITER = ",";

    @Override
    public void loadFile() throws FileNotFoundException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
        String[] taxInfo;
        String stateShort;
        String stateLong;
        BigDecimal taxRate;

        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isBlank()) {
                continue;
            }
            taxInfo = line.split(DELIMITER);
            stateShort = taxInfo[0];
            stateLong = taxInfo[1];
            taxRate = new BigDecimal(taxInfo[2]);
            Tax currentTax =  new Tax(stateShort,stateLong,taxRate);
            allTaxes.put(stateShort,currentTax);
        }
        sc.close();
    }

    @Override
    public List<Tax> getAllTaxes() {
        return new ArrayList<>(allTaxes.values());
    }


}
