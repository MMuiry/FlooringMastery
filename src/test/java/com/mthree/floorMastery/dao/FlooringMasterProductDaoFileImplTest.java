package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Product;
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

class FlooringMasterProductDaoFileImplTest {
    FlooringMasterProductDaoFileImpl dao;
    File productFile;
    File backupFile;



    @BeforeEach
    void setUp() throws IOException {
        dao = new FlooringMasterProductDaoFileImpl();
        productFile = new File("Products.txt");
        backupFile = new File("Products.txt.bak");


        if (productFile.exists()) {
            Files.copy(productFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try (FileWriter writer = new FileWriter(productFile)) {
            writer.write("ProductType,CostPerSquareFoot,LaborCostPerSquareFoot\n");
            writer.write("Carpet,2.25,2.10\n");
            writer.write("Laminate,1.75,2.10\n");
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        if (backupFile.exists()) {
            Files.move(backupFile.toPath(), productFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.deleteIfExists(productFile.toPath());
        }
    }

    @Test
    void loadFile() throws PersistenceException {
        List<Product> expectedProduct = new ArrayList<>();
        expectedProduct.add(new Product("Carpet",new BigDecimal("2.25"),new BigDecimal("2.10")));
        expectedProduct.add(new Product("Laminate",new BigDecimal("1.75"),new BigDecimal("2.10")));

        dao.loadFile();
        List<Product> ReadProduct = dao.getAllProducts();

        assertEquals(2, ReadProduct.size(), "There should be 2 product in the file");
        assertTrue(ReadProduct.containsAll(expectedProduct), "The expected memory and read from file memory should be the same");
    }

    @Test
    void getAllProducts() {
    }
}