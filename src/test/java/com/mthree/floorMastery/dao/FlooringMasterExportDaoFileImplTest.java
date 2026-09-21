package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlooringMasterExportDaoFileImplTest {
    @TempDir
    File ordersDir;

    @TempDir
    File backupDir;

    FlooringMasterExportDaoFileImpl dao;

    @BeforeEach
    void setUp() throws IOException {
        dao = new FlooringMasterExportDaoFileImpl(ordersDir.getPath(), backupDir.getPath());

        File testOrderFile = new File(ordersDir, "Orders_12312099.txt");
        try (FileWriter writer = new FileWriter(testOrderFile)) {
            writer.write("OrderNumber||CustomerName||State||TaxRate||ProductType||Area||CostPerSquareFoot||LaborCostPerSquareFoot||MaterialCost||LaborCost||Tax||Total\n");
            writer.write("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56\n");
        }
    }

    @Test
    void export_writesOrderToBackupFile() throws Exception {
        dao.export();

        File backupExportFile = new File(backupDir, "DataExport.txt");
        List<String> lines = Files.readAllLines(backupExportFile.toPath());

        assertEquals(2, lines.size(), "Should have a header line plus one order line");
        assertTrue(lines.get(1).contains("John Snow"));
    }
}