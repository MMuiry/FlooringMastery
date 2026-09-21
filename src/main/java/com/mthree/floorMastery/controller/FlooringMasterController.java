package com.mthree.floorMastery.controller;

import com.mthree.floorMastery.exceptions.NoSuchOrderException;
import com.mthree.floorMastery.exceptions.PersistenceException;
import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.service.FlooringMasterService;
import com.mthree.floorMastery.ui.FlooringMasterView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasterController {
    FlooringMasterView view;
    FlooringMasterService service;

    //Used for test units
    public FlooringMasterController(FlooringMasterView view, FlooringMasterService service) {
        this.view = view;
        this.service = service;
    }

    //Method for starting the main menu selection
    public void run(){
        boolean keepGoing = true;
        int menuSelection = 0;

        try {

            while (keepGoing) {
                //gets selection from one to 6 and will action based on input
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrders();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrders();
                        break;
                    case 5:
                        exportAllData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                }

            }

            exitMessage();
        } catch (PersistenceException | NoSuchOrderException e) {
            view.displayErrorMessage(e.getMessage());
        }

    }

    //displays and gets menu selection
    private int getMenuSelection(){
        int selection = view.displayMainMenuAndGetSelection();
        return selection;
    }

    //Displays all orders on a given date
    private void displayOrders() throws PersistenceException {
        view.displayOrderBanner();
        List<Order> orders = service.getOrderForDate(view.getDateInput(false));
        view.displayOrders(orders);
    }

    //adds an order
    private void addOrders() throws PersistenceException {
        view.displayAddOrderBanner();
        LocalDate date = view.getDateInput(true);
        Order newOrder = view.getAddOrderInput(service.getTaxes(), service.getProducts());
        boolean usrConfirmation = view.getConfirmation();

        if  (usrConfirmation) {
            service.addOrder(date, newOrder);
            view.displayAddOrderSuccess();
        }

    }

    //edits an order
    private void editOrder() throws PersistenceException {
        view.displayEditOrderBanner();
        LocalDate  date = view.getDateInput(false);
        int orderNumber = view.getOrderNumberInput();
        Order orderToEdit = service.getOrder(date, orderNumber);
        view.displayOrderInfo(orderToEdit);
        Order updatedorder = view.getEditOrderInput(service.getTaxes(),service.getProducts());
        boolean usrConfirmed = view.getConfirmation();

        if  (usrConfirmed) {
            orderToEdit = service.editOrder(date, orderNumber, updatedorder);
            view.displayEditOrderSuccess();
        }

    }

    //Removes order from the user input
    private void removeOrders() throws PersistenceException {
        view.displayRemoveOrderBanner();
        LocalDate date = view.getDateInput(false);
        int orderNumber = view.getOrderNumberInput();
        Order orderToRemove = service.getOrder(date,orderNumber);
        view.displayOrderInfo(orderToRemove);
        boolean usrConfirmed = view.getConfirmation();

        if (usrConfirmed) {
            Order removedOrder = service.removeOrder(date, orderNumber);
            view.displayRemoveOrderSuccess(removedOrder);
        }
    }

    //exports all the data to a single file
    private void exportAllData() throws PersistenceException {
        service.exportAllData();
    }

    //displays exit message
    private void exitMessage() {
        view.displayExitMessage();
    }

}
