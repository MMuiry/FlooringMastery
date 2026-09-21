package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.NoSuchOrderException;
import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class FlooringMasterOrderFileImpl implements FlooringMasterOrderDao {
    LocalDate currentLoadedDate;
    TreeMap<Integer, Order> orders = new TreeMap<>();
    String DELIMITER = "||";

    //writes the memory in order to file
    @Override
    public void writeToFile() throws PersistenceException {
        File ordersDir = new File("Orders");

        //creates directory if needed
        if (!ordersDir.exists()) {
            ordersDir.mkdirs();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        final String ORDER_FILE = "Orders/Orders_" + currentLoadedDate.format(formatter) + ".txt";
        PrintWriter out;

        //creates file
        try {
            out = new PrintWriter(new FileWriter(ORDER_FILE));
        } catch (IOException e) {
            throw new PersistenceException("Could not save Order data", e);
        }

        String currentLine;
        out.println("OrderNumber||CustomerName||State||TaxRate||ProductType||Area||CostPerSquareFoot||LaborCostPerSquareFoot||MaterialCost||LaborCost||Tax||Total");

        //converts data to formatted string
        for (Order o: orders.values()) {
            currentLine = o.getOrderNumber() + "||" + o.getCustomerName() + "||"
                    + o.getState() + "||" + o.getTaxRate() + "||"
                    + o.getProductType() + "||" + o.getArea() + "||"
                    + o.getCostPerSquareFoot() + "||" + o.getLaborCostPerSquareFoot() + "||"
                    + o.getMaterialCost() + "||" + o.getLaborCost() + "||"
                    + o.getTax() + "||" + o.getTotal();
                    out.println(currentLine);
        }

        //outputs buffer to file
        out.close();
    }

    //loads orders from a single date to memory
    @Override
    public void loadFromFile() throws PersistenceException {
        orders = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        final File ORDER_FILE = new File("Orders/Orders_" + currentLoadedDate.format(formatter) + ".txt");

        if (!ORDER_FILE.exists()) {
            return;
        }

        Scanner sc;

        try {
            sc = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not Load file in", e);
        }

        Integer orderNumber;
        String customerName;
        String state;
        BigDecimal taxRate;
        String productType;
        BigDecimal area;
        BigDecimal costPerSquareFoot;
        BigDecimal laborCostPerSquareFoot;
        BigDecimal materialCost;
        BigDecimal laborCost;
        BigDecimal tax;
        BigDecimal total;
        String[] orderInfo;

        //skips header
        if  (sc.hasNextLine()) {
            sc.nextLine();
        }

        //reads in data from file to memory
        while (sc.hasNextLine()) {
            orderInfo = sc.nextLine().split(Pattern.quote(DELIMITER));            orderNumber = Integer.parseInt(orderInfo[0]);
            customerName = orderInfo[1];
            state = orderInfo[2];
            taxRate = new BigDecimal(orderInfo[3]);
            productType = orderInfo[4];
            area = new BigDecimal(orderInfo[5]);
            costPerSquareFoot = new BigDecimal(orderInfo[6]);
            laborCostPerSquareFoot = new BigDecimal(orderInfo[7]);
            materialCost = new BigDecimal(orderInfo[8]);
            laborCost = new BigDecimal(orderInfo[9]);
            tax = new BigDecimal(orderInfo[10]);
            total = new BigDecimal(orderInfo[11]);

            Order currentOrder = new Order(orderNumber, customerName, state, taxRate, productType,
                    area, costPerSquareFoot, laborCostPerSquareFoot,
                    materialCost, laborCost, tax, total);
            orders.put(orderNumber, currentOrder);
        }

        sc.close();
    }

    //gets next order number
    @Override
    public int getNextOrderNumber(){

        //if first order in file, return 1
        if (orders.isEmpty()) {
            return 1;
        }

        //else +1 to last entered order
        int lastOrderNum = orders.lastKey() + 1;
        return lastOrderNum;
    }

    //loads order from file if exists, adds order to memory then calls the write or overwrite the old file with new version
    @Override
    public Order addOrder(LocalDate date, Order newOrder) throws PersistenceException {
        currentLoadedDate = date;
        loadFromFile();
        int nextOrderNumber = getNextOrderNumber();
        newOrder.setOrderNumber(nextOrderNumber);
        orders.put(nextOrderNumber, newOrder);
        writeToFile();
        return newOrder;
    }

    //will return a single order
    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws NoSuchOrderException, PersistenceException {
        currentLoadedDate = date;
        loadFromFile();
        Order foundOrder = orders.get(orderNumber);

        if (foundOrder == null) {
            throw new NoSuchOrderException("No order found with order number " + orderNumber + " on date " + date);
        }

        return foundOrder;
    }

    //will edit order
    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order modifiedOrder) throws PersistenceException {
        currentLoadedDate = date;
        loadFromFile();
        Order foundOrder = getOrder(date, orderNumber);

        //if left blank, it will not change it, if not left blank will update the memory of that order and write to file
        if  (modifiedOrder.getCustomerName() != null) {
            foundOrder.setCustomerName(modifiedOrder.getCustomerName());
        }

        if   (modifiedOrder.getState() != null) {
            foundOrder.setState(modifiedOrder.getState());
        }

        if   (modifiedOrder.getProductType() != null) {
            foundOrder.setProductType(modifiedOrder.getProductType());
        }

        if   (modifiedOrder.getArea() != null) {
            foundOrder.setArea(modifiedOrder.getArea());
        }

        writeToFile();
        return foundOrder;
    }

    //Loads file, removes from memory, writes to file.
    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws NoSuchOrderException, PersistenceException {
        currentLoadedDate = date;
        loadFromFile();
        Order foundOrder = orders.get(orderNumber);

        if  (foundOrder == null) {
            throw new NoSuchOrderException("No order found with order number " + orderNumber);
        }

        orders.remove(orderNumber);
        writeToFile();
        return foundOrder;
    }

    //loads order, returns an array of all order in memory
    @Override
    public List<Order> getOrders(LocalDate date) throws PersistenceException {
        currentLoadedDate = date;
        loadFromFile();
        return new ArrayList<>(orders.values());
    }
}
