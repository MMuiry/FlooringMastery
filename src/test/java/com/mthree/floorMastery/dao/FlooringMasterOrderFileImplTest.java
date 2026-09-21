package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.NoSuchOrderException;
import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlooringMasterOrderFileImplTest {
    FlooringMasterOrderFileImpl dao;
    LocalDate testDate = LocalDate.of(2099, 12, 31);
    File testOrderFile;

    @BeforeEach
    void setUp() {
        dao = new FlooringMasterOrderFileImpl();
        testOrderFile = new File("Orders/Orders_12312099.txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testOrderFile.toPath());
    }

    private void writeKnownData(String... lines) throws IOException {
        try (FileWriter writer = new FileWriter(testOrderFile)) {
            writer.write("OrderNumber||CustomerName||State||TaxRate||ProductType||Area||CostPerSquareFoot||LaborCostPerSquareFoot||MaterialCost||LaborCost||Tax||Total\n");
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }
    }

    @Test
    void writeToFile_createsFileWithHeaderAndOrders() throws PersistenceException, IOException {
        dao.currentLoadedDate = testDate;
        Order order = new Order(1, "John Snow", "Texas", new BigDecimal("4.45"), "Tile",
                new BigDecimal("250.00"), new BigDecimal("3.50"), new BigDecimal("4.15"),
                new BigDecimal("875.00"), new BigDecimal("1037.50"), new BigDecimal("85.06"), new BigDecimal("1997.56"));
        dao.orders.put(1, order);

        dao.writeToFile();

        assertTrue(testOrderFile.exists());
        List<String> lines = Files.readAllLines(testOrderFile.toPath());
        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("John Snow"));
    }

    @Test
    void loadFromFile_readsOrdersFromFile() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");
        dao.currentLoadedDate = testDate;

        dao.loadFromFile();

        assertEquals(1, dao.orders.size());
        assertEquals("John Snow", dao.orders.get(1).getCustomerName());
    }

    @Test
    void getNextOrderNumber_returnsOneWhenEmpty() {
        assertEquals(1, dao.getNextOrderNumber());
    }

    @Test
    void getNextOrderNumber_returnsMaxPlusOne() {
        dao.orders.put(4, new Order());
        dao.orders.put(2, new Order());
        assertEquals(5, dao.getNextOrderNumber());
    }

    @Test
    void addOrder_assignsOrderNumberAndSaves() throws Exception {
        Order newOrder = new Order("Ada Lovelace", "CA", "Wood", new BigDecimal("300.00"));

        Order result = dao.addOrder(testDate, newOrder);

        assertEquals(1, result.getOrderNumber());
        assertTrue(testOrderFile.exists());
    }

    @Test
    void getOrder_returnsMatchingOrder() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");

        Order result = dao.getOrder(testDate, 1);

        assertEquals("John Snow", result.getCustomerName());
    }

    @Test
    void getOrder_throwsWhenNotFound() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");

        assertThrows(NoSuchOrderException.class, () -> dao.getOrder(testDate, 99));
    }

    @Test
    void editOrder_updatesOnlyProvidedFields() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");
        Order changes = new Order();
        changes.setCustomerName("Jon Snow");

        Order result = dao.editOrder(testDate, 1, changes);

        assertEquals("Jon Snow", result.getCustomerName());
        assertEquals("Texas", result.getState());
    }

    @Test
    void removeOrder_removesAndReturnsOrder() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");

        Order removed = dao.removeOrder(testDate, 1);

        assertEquals("John Snow", removed.getCustomerName());
        assertThrows(NoSuchOrderException.class, () -> dao.getOrder(testDate, 1));
    }

    @Test
    void removeOrder_throwsWhenNotFound() throws Exception {
        writeKnownData("1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56");

        assertThrows(NoSuchOrderException.class, () -> dao.removeOrder(testDate, 99));
    }

    @Test
    void getOrders_returnsAllOrdersForDate() throws Exception {
        writeKnownData(
                "1||John Snow||Texas||4.45||Tile||250.00||3.50||4.15||875.00||1037.50||85.06||1997.56",
                "2||Teehan Muir||CA||25.00||Wood||300.00||2.25||2.10||675.00||630.00||326.25||1631.25"
        );

        List<Order> result = dao.getOrders(testDate);

        assertEquals(2, result.size());
    }
}