package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.model.Order;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface FlooringMasterOrderDao {
        void writeToFile() throws IOException;
        void loadFromFile() throws FileNotFoundException;
        int getNextOrderNumber() throws FileNotFoundException;
        Order addOrder(LocalDate date, Order newOrder) throws IOException;
        Order getOrder(LocalDate date, int orderNumber ) throws FileNotFoundException;
        Order editOrder(LocalDate date, int orderNumber, Order modifiedOrder) throws IOException;
        Order removeOrder(LocalDate date, int orderNumber) throws IOException;
        List<Order> getOrders(LocalDate date) throws FileNotFoundException;
}
