package com.mthree.floorMastery.service;

import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasterService {
    Order addOrder(LocalDate orderDate, Order order) throws IOException;
    Order getOrder(LocalDate dateOfOrder, int orderNumber) throws FileNotFoundException;
    Order editOrder(LocalDate dateOfOrder, int orderNumber,  Order modifiedOrder) throws IOException;
    List<Order> getOrderForDate(LocalDate dateOfOrder) throws FileNotFoundException;
    Order removeOrder(LocalDate dateOfOrder, int orderNumber) throws IOException;
    void exportData();
    List<Tax> getTaxes() throws FileNotFoundException;
    List<Product> getProducts() throws FileNotFoundException;
}
