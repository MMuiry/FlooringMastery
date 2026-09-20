package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface FlooringMasterOrderDao {
        void writeToFile() throws PersistenceException;
        void loadFromFile() throws PersistenceException;
        int getNextOrderNumber();
        Order addOrder(LocalDate date, Order newOrder) throws PersistenceException;
        Order getOrder(LocalDate date, int orderNumber ) throws PersistenceException;
        Order editOrder(LocalDate date, int orderNumber, Order modifiedOrder) throws PersistenceException;
        Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException;
        List<Order> getOrders(LocalDate date) throws PersistenceException;
}
