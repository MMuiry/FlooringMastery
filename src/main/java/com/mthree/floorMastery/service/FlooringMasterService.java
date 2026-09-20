package com.mthree.floorMastery.service;

import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasterService {
    Order addOrder(LocalDate orderDate, Order order) throws PersistenceException;
    Order getOrder(LocalDate dateOfOrder, int orderNumber) throws PersistenceException;
    Order editOrder(LocalDate dateOfOrder, int orderNumber,  Order modifiedOrder) throws PersistenceException;
    List<Order> getOrderForDate(LocalDate dateOfOrder) throws PersistenceException;
    Order removeOrder(LocalDate dateOfOrder, int orderNumber) throws PersistenceException;
    void exportAllData() throws PersistenceException;
    List<Tax> getTaxes() throws PersistenceException;
    List<Product> getProducts() throws PersistenceException;
}
