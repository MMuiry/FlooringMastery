package com.mthree.floorMastery.dao;

import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class FlooringMasterExportDaoFileImpl implements FlooringMasterExportDao {
    String EXPORT_FILE = "DataExport.txt";
    String DELIMITER = "||";
    Map<String, Map<Integer, Order>> allOrders = new HashMap<>();

    @Override
    public void export() throws PersistenceException {
        File folder = new File("Orders");
        File[] txtFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (txtFiles != null) {

            for (File file : txtFiles) {
                String fileName = file.getName();
                String digits = fileName.replaceAll("[^0-9]", "");
                String mm = digits.substring(0, 2);
                String dd = digits.substring(2, 4);
                String yyyy = digits.substring(4, 8);
                String stringDate = mm + "-" + dd + "-" + yyyy;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate date = LocalDate.parse(stringDate, formatter);

                if (date.isBefore(LocalDate.now())) {
                    continue;
                }

                Map<Integer, Order> ordersForOneDate = new TreeMap<>();

                try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {
                    if (sc.hasNextLine()) {
                        sc.nextLine();
                    }

                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        String[] orderInfo = line.split(Pattern.quote(DELIMITER));

                        int orderNumber = Integer.parseInt(orderInfo[0]);
                        Order order = new Order();
                        order.setOrderNumber(orderNumber);
                        order.setCustomerName(orderInfo[1]);
                        order.setState(orderInfo[2]);
                        order.setTaxRate(new BigDecimal(orderInfo[3]));
                        order.setProductType(orderInfo[4]);
                        order.setArea(new BigDecimal(orderInfo[5]));
                        order.setCostPerSquareFoot(new BigDecimal(orderInfo[6]));
                        order.setLaborCostPerSquareFoot(new BigDecimal(orderInfo[7]));
                        order.setMaterialCost(new BigDecimal(orderInfo[8]));
                        order.setLaborCost(new BigDecimal(orderInfo[9]));
                        order.setTax(new BigDecimal(orderInfo[10]));
                        order.setTotal(new BigDecimal(orderInfo[11]));

                        ordersForOneDate.put(orderNumber, order);
                    }
                } catch (FileNotFoundException e) {
                    continue;
                }
                allOrders.put(stringDate, ordersForOneDate);
            }
            writeToFile();
        }

    }


    private void writeToFile() throws PersistenceException {
        File ordersDir = new File("Backup");
        if (!ordersDir.exists()) {
            ordersDir.mkdirs();
        }
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter("Backup/"+EXPORT_FILE));
        } catch (IOException e) {
            throw new PersistenceException("Backup File could not be created", e);
        }
        out.println("OrderNumber||CustomerName||State||TaxRate||ProductType||Area||CostPerSquareFoot||LaborCostPerSquareFoot||MaterialCost||LaborCost||Tax||Total||Date");

        for (Map.Entry<String, Map<Integer, Order>> entry : allOrders.entrySet()) {
            Map<Integer, Order> ordersForDate = entry.getValue();
            String currentLine;
            String date = entry.getKey();
            for (Map.Entry<Integer, Order> orderEntry : ordersForDate.entrySet()) {
                Order o = orderEntry.getValue();
                currentLine = o.getOrderNumber() + "||" + o.getCustomerName() + "||"
                        + o.getState() + "||" + o.getTaxRate() + "||"
                        + o.getProductType() + "||" + o.getArea() + "||"
                        + o.getCostPerSquareFoot() + "||" + o.getLaborCostPerSquareFoot() + "||"
                        + o.getMaterialCost() + "||" + o.getLaborCost() + "||"
                        + o.getTax() + "||" + o.getTotal() + "||" + date;
                out.println(currentLine);
            }
        }
        out.close();
    }

}
