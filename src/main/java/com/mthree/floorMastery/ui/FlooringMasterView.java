package com.mthree.floorMastery.ui;

import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlooringMasterView {
    private UserIO io;

    public FlooringMasterView(UserIO io) {
        this.io = io;
    }
    public int displayMainMenuAndGetSelection(){
        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Exit");
        int selection = io.readInt("Please choose an option from 1-6", 1,6);
        return selection;
    }

    public LocalDate getDateInput(){
        LocalDate date = io.readDate("Please enter a date", "MM/dd/yyyy");
        return date;
    }

    public void displayOrders(List<Order> orders){
        for (Order o : orders){
            io.print(o.toString());
        }
        io.readString("Please press enter to continue");
    }

    public void displayOrderInfo(Order order){
        if (order != null) {
            io.print(order.toString());
        } else {io.print("Order not found");}
        io.readString("Please hit enter to continue");
    }



    public int getOrderNumberInput() {
        int orderNum = io.readInt("Enter order number");
        return orderNum;
    }

    private String getAndValidateCustomerName(boolean allowBlank) {
        boolean valid = false;
        String customerName = "";
        while (!valid) {
            customerName = io.readString("Please enter customer name: ");
            if (allowBlank && customerName.isBlank()) {
                return null;
            }
            if (!customerName.isBlank() && customerName.matches("^[a-zA-Z0-9.,\\s]+$")) {
                valid = true;
            } else {
                io.print("Invalid name: cannot be blank, and can only contain letters, numbers, periods, and commas.");
            }
        }
        return customerName;
    }

    private String getAndValidateState(List<Tax> taxes, boolean allowBlank) {
        String state = "";
        boolean valid =  false;
        List<String> stateList = new ArrayList<>();
        io.print("These are the States we sell too");

        for (Tax t: taxes) {
            io.print(t.getState());
            stateList.add(t.getState());
        }

        while (!valid) {
            state = io.readString("Please enter state: ");
            if (allowBlank && state.isBlank()) {
                return null;
            }
            final String currentState = state;
            valid = stateList.stream().anyMatch(s -> s.equalsIgnoreCase(currentState));
            if (!valid) {
                io.print("Invalid State, if input is not one of the above, we cannot sell there");
            }
        }
        return state;
    }


    private String getAndValidateProductType(List<Product> products, boolean allowBlank) {
        String productType = "";
        boolean valid = false;
        List<String> productList = new ArrayList<>();
        io.print("These are the product currently on sale: ");

        for (Product p : products) {
            io.print(p.toString());
            productList.add(p.getProductType());
        }

        while (!valid) {
            productType = io.readString("Please select one of these products: ");
            if (allowBlank && productType.isBlank()) {
                return null;
            }
            final String currentProductType = productType;
            valid = productList.stream().anyMatch(s -> s.equalsIgnoreCase(currentProductType));
            if (!valid) {
                io.print("Invalid product, please check spelling");
            }
        }
        return productType;
    }

    private BigDecimal getAndValidateArea(boolean allowBlank) {
        BigDecimal area = io.readBigDecimal("Please enter sq feet area (minimum 100): ", 100, allowBlank);
        return area;
    }


    public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
        String customerName = getAndValidateCustomerName(false);
        String state = getAndValidateState(taxes, false);
        String productType = getAndValidateProductType(products, false);
        BigDecimal area = getAndValidateArea(false);
        Order newOrder = new Order(customerName, state, productType, area);

        return newOrder;
    }

    public Order getEditOrderInput(List<Tax> taxes, List<Product> products) {
        String customerName = getAndValidateCustomerName(true);
        String state = getAndValidateState(taxes, true);
        String productType = getAndValidateProductType(products, true);
        BigDecimal area = getAndValidateArea(true);

        Order newOrder = new Order(customerName, state, productType, area);
        return newOrder;
    }

    public boolean getConfirmation(){
        boolean usrChoice = io.readBoolean("Would you like to continue? (yes/no): ");
        return usrChoice;
    }


    public void displayErrorMessage(String errorMessage) {
        io.print("=== ERROR ===");
        io.print(errorMessage);
    }
    public void getDisplayUnknownCommandMessage() {
        io.print("Please enter a valid command");
    }

    public void displayExitMessage() {
        io.print("Goodbye!");
    }

    public void displayAddOrderSuccess() {
        io.print("ORDER HAS BEEN ADDED SUCCESSFULLY");
    }

    public void displayEditOrderSuccess() {
        io.print("ORDER HAS BEEN EDITED SUCCESSFULLY");
    }

    public void displayRemoveOrderSuccess(Order removedOrder) {
        if (removedOrder != null) {
        io.print("ORDER HAS BEEN DELETED SUCCESSFULLY");
        } else {io.print("ORDER NOT FOUND, NOTHING HAS BEEN DELETED");}
    }


    public void displayExportDataSuccess() {
        io.print("ORDER HAS BEEN EXPORTED SUCCESSFULLY");
    }

    public void displayOrderBanner() {
        io.print("=== DISPLAYING ORDERS ===");
    }
    public void displayAddOrderBanner() {
        io.print("=== ADD ORDER ===");
    }

    public void displayEditOrderBanner(){
        io.print("=== EDIT ORDER ===");
    }

    public void displayRemoveOrderBanner() {
        io.print("=== REMOVE ORDER ===");
    }

}
