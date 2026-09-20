package com.mthree.floorMastery.controller;

import com.mthree.floorMastery.dao.PersistenceException;
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

    public FlooringMasterController(FlooringMasterView view, FlooringMasterService service) {
        this.view = view;
        this.service = service;
    }

    public void run(){
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

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
        } catch (PersistenceException | FileNotFoundException e) {
            view.displayErrorMessage(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getMenuSelection(){
        int selection = view.displayMainMenuAndGetSelection();
        return selection;
    }

    private void displayOrders() throws FileNotFoundException {
        view.displayOrderBanner();
        List<Order> orders = service.getOrderForDate(view.getDateInput());
        view.displayOrders(orders);
    }

    private void addOrders() throws PersistenceException, IOException {
        view.displayAddOrderBanner();
        LocalDate date = view.getDateInput();
        Order newOrder = view.getAddOrderInput(service.getTaxes(), service.getProducts());
        service.addOrder(date, newOrder);
    }

    private void editOrder() throws PersistenceException, FileNotFoundException {
        view.displayEditOrderBanner();
        Order orderToEdit = service.getOrder(view.getDateInput(),view.getOrderNumberInput());
        view.displayOrderInfo(orderToEdit);
        if  (orderToEdit != null) {
            orderToEdit = service.getOrder(view.getDateInput(), view.getOrderNumberInput());
            view.displayEditOrderSuccess();
        }
    }

    private void removeOrders() throws PersistenceException, IOException {
        view.displayRemoveOrderBanner();
        Order removedOrder = service.removeOrder(view.getDateInput(), view.getOrderNumberInput());
    }

    private void exportAllData() throws PersistenceException {
        service.exportData();
    }

    private void exitMessage() {
        view.displayExitMessage();
    }

    private void unknownCommand() {
        view.getDisplayUnknownCommandMessage();
    }
}
