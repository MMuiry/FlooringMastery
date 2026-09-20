package com.mthree.floorMastery.service;

import com.mthree.floorMastery.dao.FlooringMasterExportDao;
import com.mthree.floorMastery.dao.FlooringMasterOrderDao;
import com.mthree.floorMastery.dao.FlooringMasterProductDao;
import com.mthree.floorMastery.dao.FlooringMasterTaxDao;
import com.mthree.floorMastery.model.Order;
import com.mthree.floorMastery.model.Product;
import com.mthree.floorMastery.model.Tax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlooringMasterServiceImpl implements FlooringMasterService {
    FlooringMasterTaxDao taxDao;
    FlooringMasterProductDao productDao;
    FlooringMasterOrderDao orderDao;
    FlooringMasterExportDao exportDao;
    public FlooringMasterServiceImpl (FlooringMasterOrderDao orderDao, FlooringMasterProductDao productDao, FlooringMasterTaxDao taxDao , FlooringMasterExportDao exportDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.taxDao = taxDao;
        this.exportDao = exportDao;
    }



    @Override
    public Order addOrder(LocalDate orderDate, Order order) throws IOException {
        Product product = getProducts().stream()
                .filter(p -> p.getProductType().equalsIgnoreCase(order.getProductType()))
                .findFirst()
                .orElse(null);
        Tax state = getTaxes().stream()
                .filter(p -> p.getState().equalsIgnoreCase(order.getState()))
                .findFirst()
                .orElse(null);
        int orderNumber = orderDao.getNextOrderNumber();
        BigDecimal materialCost = order.getArea().multiply(product.getCostPerSquareFoot()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal labourCost = order.getArea().multiply(product.getLaborCostperSquareFoot()).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal tax = materialCost.add(labourCost).multiply(state.getTaxRate().divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalCost =  materialCost.add(labourCost).add(tax).setScale(2, BigDecimal.ROUND_HALF_UP);
        order.setMaterialCost(order.getArea());

        order.setMaterialCost(materialCost);
        order.setLaborCost(labourCost);
        order.setTax(tax);
        order.setTotal(totalCost);

        return orderDao.addOrder(orderDate, order);
    }

    @Override
    public Order getOrder(LocalDate dateOfOrder, int orderNumber) throws FileNotFoundException {
       return orderDao.getOrder(dateOfOrder, orderNumber);
    }

    @Override
    public Order editOrder(LocalDate dateOfOrder, int orderNumber, Order modifiedOrder) throws IOException {
        return orderDao.editOrder(dateOfOrder, orderNumber, modifiedOrder);
    }

    @Override
    public List<Order> getOrderForDate(LocalDate dateOfOrder) throws FileNotFoundException {
        return new ArrayList<>(orderDao.getOrders(dateOfOrder));
    }

    @Override
    public Order removeOrder(LocalDate dateOfOrder, int orderNumber) throws IOException {
        return orderDao.removeOrder(dateOfOrder, orderNumber);
    }

    @Override
    public void exportData() {
        //To Implement
    }

    @Override
    public List<Tax> getTaxes() throws FileNotFoundException {
        taxDao.loadFile();
        return new ArrayList<>(taxDao.getAllTaxes());
    }

    @Override
    public List<Product> getProducts() throws FileNotFoundException {
        productDao.loadFile();
        return new ArrayList<>(productDao.getAllProducts());
    }
}
