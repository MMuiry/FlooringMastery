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

    //displays menu and gets selection
    public int displayMainMenuAndGetSelection(){
        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Exit");
        int selection = io.readInt("Please choose an option from 1-6: ", 1,6);
        return selection;
    }

    //gets date input from user, has an option to require date to be in future as its required for adding new orders
    public LocalDate getDateInput(boolean needsToBeFuture){
        LocalDate date = io.readDate("Please enter a date", "MM/dd/yyyy");
        boolean dateIsFuture = false;

        while (needsToBeFuture && !dateIsFuture) {

            if (date.isBefore(LocalDate.now())) {
                io.print("Input Invalid: Please enter a future date");
                date = io.readDate("Please enter a future date", "MM/dd/yyyy");
            } else {
                dateIsFuture = true;
            }

        }

        return date;
    }

    //displays all orders given
    public void displayOrders(List<Order> orders) {

        for (Order o : orders) {
            io.print("Order Number " + o.getOrderNumber() + ": ");
            io.print("customer Name: " + o.getCustomerName());
            io.print("State: " + o.getState());
            io.print("Tax Rate: " + o.getTaxRate());
            io.print("Product Type: " + o.getProductType());
            io.print("Area: " + o.getArea());
            io.print("Cost Per Square Foot: " + o.getCostPerSquareFoot());
            io.print("Labor Cost Per Square Foot: " + o.getLaborCostPerSquareFoot());
            io.print("Material Cost: " + o.getMaterialCost());
            io.print("Labor Cost: " + o.getLaborCost());
            io.print("Tax: " + o.getTax());
            io.print("Total: " + o.getTotal());
            io.print("------------");
        }

        if (orders.isEmpty()) {
            io.print("No Orders Found");
        }

        io.readString("Please press enter to continue");

    }

    //displays OrderInfo
    public void displayOrderInfo(Order order){

        if (order != null) {
            io.print("Order Found!");
            io.print("------------");
            io.print("Order Number " + order.getOrderNumber() + ": ");
            io.print("customer Name: " + order.getCustomerName());
            io.print("State: " + order.getState());
            io.print("Tax Rate: " + order.getTaxRate());
            io.print("Product Type: " + order.getProductType());
            io.print("Area: " + order.getArea());
            io.print("Cost Per Square Foot: " + order.getCostPerSquareFoot());
            io.print("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot());
            io.print("Material Cost: " + order.getMaterialCost());
            io.print("Labor Cost: " + order.getLaborCost());
            io.print("Tax: " + order.getTax());
            io.print("Total: " + order.getTotal());
            io.print("------------");
        } else {io.print("Order not found");}

    }


    //gets the order number input
    public int getOrderNumberInput() {
        int orderNum = io.readInt("Enter order number");
        return orderNum;
    }

    //gets and validates customer name, allows blanks to be toggled for edit
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

    //gets and validates state name, allows blanks to be toggled for edit
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

    //gets and validates Product type, allows blanks to be toggled for edit
    private String getAndValidateProductType(List<Product> products, boolean allowBlank) {
        String productType = "";
        boolean valid = false;
        List<String> productList = new ArrayList<>();
        io.print("These are the product currently on sale: ");

        for (Product p : products) {
            io.print("Product Type: " + p.getProductType());
            io.print("Material Cost per Square Foot: " + p.getCostPerSquareFoot());
            io.print("Labour Cost per Square Foot: " + p.getLaborCostperSquareFoot());
            io.print("----------------");
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

    //gets and validates customer name, allows blanks to be toggled for edit
    private BigDecimal getAndValidateArea(boolean allowBlank) {
        BigDecimal area = io.readBigDecimal("Please enter sq feet area (minimum 100): ", 100, allowBlank);
        return area;
    }

    //gets add order information, the rest of the fields are calculated so just needs the 4 fields
    public Order getAddOrderInput(List<Tax> taxes, List<Product> products) {
        String customerName = getAndValidateCustomerName(false);
        String state = getAndValidateState(taxes, false);
        String productType = getAndValidateProductType(products, false);
        BigDecimal area = getAndValidateArea(false);
        Order newOrder = new Order(customerName, state, productType, area);

        io.print("Customer Name : " + customerName);
        io.print("State : " + state);
        io.print("Product Type : " + productType);
        io.print("Area : " + area);

        return newOrder;
    }

    //gets the information needed to edit order
    public Order getEditOrderInput(List<Tax> taxes, List<Product> products) {
        String customerName = getAndValidateCustomerName(true);
        String state = getAndValidateState(taxes, true);
        String productType = getAndValidateProductType(products, true);
        BigDecimal area = getAndValidateArea(true);

        Order newOrder = new Order(customerName, state, productType, area);
        return newOrder;
    }

    //gets confirmaiton from user, used for edit/adding/removing data
    public boolean getConfirmation(){
        boolean usrChoice = io.readBoolean("Would you like to continue? (yes/no): ");
        return usrChoice;
    }

    //displays error message
    public void displayErrorMessage(String errorMessage) {
        io.print("=== ERROR ===");
        io.print(errorMessage);
    }

    //--------
    //Visual additions that arn't dynamic

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
