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
    String ordersFolderPath = "Orders";
    String backupFolderPath = "Backup";
    String EXPORT_FILE = "DataExport.txt";
    String DELIMITER = "||";
    Map<String, Map<Integer, Order>> allOrders = new HashMap<>();

    //will be used in production
    public FlooringMasterExportDaoFileImpl() {
    }

    //Used for testing units
    public FlooringMasterExportDaoFileImpl(String ordersFolderPath, String backupFolderPath) {
        this.ordersFolderPath = ordersFolderPath;
        this.backupFolderPath = backupFolderPath;
    }

    //will get all the information in Orders folder thats "Active" and then calls the other methods to write to file
    @Override
    public void export() throws PersistenceException {
        File folder = new File(ordersFolderPath);
        File[] txtFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        //checks if there any files
        if (txtFiles != null) {

            //if theres files, read them while pulling out the data
            for (File file : txtFiles) {
                //pulls date and filename
                String fileName = file.getName();
                String digits = fileName.replaceAll("[^0-9]", "");
                String mm = digits.substring(0, 2);
                String dd = digits.substring(2, 4);
                String yyyy = digits.substring(4, 8);
                String stringDate = mm + "-" + dd + "-" + yyyy;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                LocalDate date = LocalDate.parse(stringDate, formatter);

                //confirms if the file is sitll "active"
                if (date.isBefore(LocalDate.now())) {
                    continue;
                }

                Map<Integer, Order> ordersForOneDate = new TreeMap<>();

                //reads file to memory
                try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)))) {

                    //skips header
                    if (sc.hasNextLine()) {
                        sc.nextLine();
                    }

                    //reads
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

                //saves
                allOrders.put(stringDate, ordersForOneDate);
            }

            //after all data is retrived, runs the write method
            writeToFile();
        }

    }

    //writes the saved data to file
    private void writeToFile() throws PersistenceException {
        File ordersDir = new File(backupFolderPath);

        //checks folder exists
        if (!ordersDir.exists()) {
            ordersDir.mkdirs();
        }

        PrintWriter out;

        //creates file
        try {
            out = new PrintWriter(new FileWriter(backupFolderPath+"/"+EXPORT_FILE));
        } catch (IOException e) {
            throw new PersistenceException("Backup File could not be created", e);
        }

        //writes header
        out.println("OrderNumber||CustomerName||State||TaxRate||ProductType||Area||CostPerSquareFoot||LaborCostPerSquareFoot||MaterialCost||LaborCost||Tax||Total||Date");

        //reads the memory to buffer
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

        //outputs buffer to file
        out.close();
    }

}
