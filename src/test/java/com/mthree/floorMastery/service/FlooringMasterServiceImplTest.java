package com.mthree.floorMastery.service;

import com.mthree.floorMastery.dao.FlooringMasterExportDao;
import com.mthree.floorMastery.dao.FlooringMasterOrderDao;
import com.mthree.floorMastery.dao.FlooringMasterProductDao;
import com.mthree.floorMastery.dao.FlooringMasterTaxDao;
import com.mthree.floorMastery.exceptions.NoSuchOrderException;
import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlooringMasterServiceImplTest {
    @Mock
    FlooringMasterExportDao exportDao;
    @Mock
    FlooringMasterProductDao productDao;
    @Mock
    FlooringMasterTaxDao taxDao;
    @Mock
    FlooringMasterOrderDao orderDao;

    FlooringMasterServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FlooringMasterServiceImpl(orderDao, productDao, taxDao, exportDao);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addOrder_TestingCalculation() throws PersistenceException {
        Product tile = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        Tax texas = new Tax("TX", "Texas", new BigDecimal("4.45"));

        when(productDao.getAllProducts()).thenReturn(List.of(tile));
        when(taxDao.getAllTaxes()).thenReturn(List.of(texas));
        when(orderDao.getNextOrderNumber()).thenReturn(1);
        when(orderDao.addOrder(any(), any())).thenAnswer(inv -> inv.getArgument(1));

        Order newOrder = new Order("John Snow", "Texas", "Tile", new BigDecimal("250.00"));
        Order result = service.addOrder(LocalDate.now().plusDays(1), newOrder);

        assertEquals(new BigDecimal("875.00"), result.getMaterialCost(), "The matCost should be 875.00");
        assertEquals(new BigDecimal("1037.50"), result.getLaborCost(), "The labourCost should be 1037.50");
        assertEquals(new BigDecimal("85.11"), result.getTax(), "The Tax should be 85.11");
        assertEquals(new BigDecimal("1997.61"), result.getTotal(), "The totalCost should be 1,997.61");

    }


    @Test
    void getOrder_returnSuccess() throws PersistenceException  {
        Order returnedOrder = new Order("John Snow", "Texas", "Tile", new BigDecimal("250.00"));
        when(orderDao.getOrder(any(),anyInt())).thenReturn(returnedOrder);

        Order result = service.getOrder(LocalDate.now(),1);

        assertEquals(returnedOrder,result, "The order should be returned correctly");
    }

    @Test
    void getOrder_ThrowError() throws PersistenceException  {
        when(orderDao.getOrder(any(),anyInt())).thenThrow(new NoSuchOrderException("No Order Found"));

        assertThrows(NoSuchOrderException.class,() -> service.getOrder(LocalDate.now(), 99));
    }

    @Test
    void editOrder_returnSuccess() throws PersistenceException {
        Order editedOrder = new Order("John Snow", "Texas", "Tile", new BigDecimal("250.00"));
        when(orderDao.editOrder(any(), anyInt(), any())).thenReturn(editedOrder);

        Order result = service.editOrder(LocalDate.now(), 1, editedOrder);
        assertEquals(editedOrder,result, "The order should be returned correctly");
    }

    @Test
    void editOrder_throwsWhenNotFound() throws PersistenceException {
        when(orderDao.editOrder(any(), anyInt(), any())).thenThrow(new NoSuchOrderException("No Order Found"));

        assertThrows(NoSuchOrderException.class,() -> service.editOrder(LocalDate.now(), 1, new Order()));
    }
    @Test
    void getOrderForDate_returnsOrdersForDate() throws PersistenceException {
        Order order1 = new Order("John Snow", "Texas", "Tile", new BigDecimal("250.00"));
        Order order2 = new Order("Teehan Muir", "Texas", "Wood", new BigDecimal("300.00"));
        when(orderDao.getOrders(any())).thenReturn(List.of(order1, order2));

        List<Order> result = service.getOrderForDate(LocalDate.now());

        assertEquals(2, result.size());
        assertEquals(order1, result.get(0));
        assertEquals(order2, result.get(1));
    }

    @Test
    void getOrderForDate_returnsEmptyListWhenNoOrders() throws PersistenceException {
        when(orderDao.getOrders(any())).thenReturn(List.of());

        List<Order> result = service.getOrderForDate(LocalDate.now());

        assertTrue(result.isEmpty());
    }

    @Test
    void removeOrder_returnSuccess() throws PersistenceException  {
        Order removeOrder = new Order("John Snow", "Texas", "Tile", new BigDecimal("250.00"));
        when(orderDao.getOrder(any(),anyInt())).thenReturn(removeOrder);

        Order result = service.getOrder(LocalDate.now(),1);

        assertEquals(removeOrder,result, "The order should be returned correctly");
    }

    @Test
    void removeOrder_ThrowError() throws PersistenceException  {
        when(orderDao.removeOrder(any(),anyInt())).thenThrow(new NoSuchOrderException("No Order Found"));

        assertThrows(NoSuchOrderException.class,() -> service.removeOrder(LocalDate.now(), 99));
    }
    @Test
    void exportAllData_callsExportDao() throws PersistenceException {
        service.exportAllData();
        verify(exportDao).export();
    }

    @Test
    void getTaxes() throws PersistenceException {
        Tax texas = new Tax("TX", "Texas", new BigDecimal("4.45"));
        when(taxDao.getAllTaxes()).thenReturn(List.of(texas));

        List<Tax> result = service.getTaxes();

        assertEquals(1, result.size(), "state list size should be 1");
        assertEquals(texas, result.get(0), "Given state should be the same as returned state");
        verify(taxDao).loadFile();
    }

    @Test
    void getProducts() throws PersistenceException {
        Product tile = new Product("Tile", new BigDecimal("3.50"), new BigDecimal("4.15"));
        when(productDao.getAllProducts()).thenReturn(List.of(tile));

        List<Product> result = service.getProducts();

        assertEquals(1, result.size(), "Product list size should be 1");
        assertEquals(tile, result.get(0), "given product should be same as returned product");
        verify(productDao).loadFile();
    }
}