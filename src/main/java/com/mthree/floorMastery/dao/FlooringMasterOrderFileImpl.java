package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FlooringMasterOrderFileImpl implements FlooringMasterOrderDao {
    LocalDate currentLoadedDate;
    TreeMap<Integer, Order> orders = new TreeMap<>();
    String DELIMITER = "||";
    @Override

    public void writeToFile() throws IOException {
        File ordersDir = new File("Orders");
        if (!ordersDir.exists()) {
            ordersDir.mkdirs();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        final String ORDER_FILE = "Orders/Orders_" + currentLoadedDate.format(formatter) + ".txt";
        PrintWriter out = new PrintWriter(new FileWriter(ORDER_FILE));
        String currentLine;
        for (Order o: orders.values()) {
            currentLine = o.getOrderNumber() + "||" + o.getCustomerName() + "||"
                    + o.getState() + "||" + o.getTaxRate() + "||"
                    + o.getProductType() + "||" + o.getArea() + "||"
                    + o.getCostPerSquareFoot() + "||" + o.getLaborCostPerSquareFoot() + "||"
                    + o.getMaterialCost() + "||" + o.getLaborCost() + "||"
                    + o.getTax() + "||" + o.getTotal();
                    out.println(currentLine);
        }
        out.close();
    }

    @Override
    public void loadFromFile() throws FileNotFoundException {
        orders = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        final File ORDER_FILE = new File("Orders/Orders_" + currentLoadedDate.format(formatter) + ".txt");

        if (!ORDER_FILE.exists()) {
            return;
        }

        Scanner sc = new Scanner(new BufferedReader(new FileReader(ORDER_FILE)));
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


        if (sc.hasNextLine()) {
            sc.nextLine();
        }

        while (sc.hasNextLine()) {
            orderInfo = sc.nextLine().split(DELIMITER);
            String line = sc.nextLine();
            orderNumber = Integer.parseInt(orderInfo[0]);
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

    @Override
    public int getNextOrderNumber() throws FileNotFoundException {
        if (orders.isEmpty()) {
            return 1;
        }

        int lastOrderNum = orders.lastKey() + 1;
        return lastOrderNum;
    }

    @Override
    public Order addOrder(LocalDate date, Order newOrder) throws IOException {

        currentLoadedDate = date;
        loadFromFile();
        int nextOrderNumber = getNextOrderNumber();
        newOrder.setOrderNumber(nextOrderNumber);
        orders.put(nextOrderNumber, newOrder);
        writeToFile();
        return newOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws FileNotFoundException {
        currentLoadedDate = date;
        loadFromFile();
        Order foundOrder = orders.get(orderNumber);
        return foundOrder;
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order modifiedOrder) throws IOException {
        loadFromFile();
        Order foundOrder = getOrder(date, orderNumber);
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

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws IOException {
        loadFromFile();
        Order foundOrder = orders.get(orderNumber);
        orders.remove(orderNumber);
        writeToFile();
        return foundOrder;
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FileNotFoundException {
        loadFromFile();
        return new ArrayList<>(orders.values());
    }
}
