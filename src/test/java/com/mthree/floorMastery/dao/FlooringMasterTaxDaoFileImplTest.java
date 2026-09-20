package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.model.Tax;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlooringMasterTaxDaoFileImplTest {
    FlooringMasterTaxDaoFileImpl dao;
    File taxFile;
    File backupFile;

    @BeforeEach
    void setUp() throws IOException {
        dao = new FlooringMasterTaxDaoFileImpl();
        taxFile = new File("Taxes.txt");
        backupFile = new File("Taxes.txt.bak");


        if (taxFile.exists()) {
            Files.copy(taxFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try (FileWriter writer = new FileWriter(taxFile)) {
            writer.write("StateAbbreviation,StateName,TaxRate\n");
            writer.write("TX,Texas,4.45\n");
            writer.write("CA,California,25.00\n");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        if (backupFile.exists()) {
            Files.move(backupFile.toPath(), taxFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(taxFile.toPath());
        }
    }
    @Test
    void loadFile_readsTaxesFromFile() throws Exception {
        List<Tax> expectedTaxes = new ArrayList<>();
        expectedTaxes.add(new Tax("TX", "Texas", new BigDecimal("4.45")));
        expectedTaxes.add(new Tax("CA", "California", new BigDecimal("25.00")));

        dao.loadFile();
        List<Tax> Readtaxes = dao.getAllTaxes();

        assertEquals(2, Readtaxes.size());
        assertTrue(Readtaxes.containsAll(expectedTaxes), "The expected memory and read from file should be the same");
    }

    @Test
    void getAllTaxes_returnsEmptyBeforeLoadFileCalled() {
        List<Tax> taxes = dao.getAllTaxes();

        assertTrue(taxes.isEmpty());
    }
}
