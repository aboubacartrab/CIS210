package com.example.cis120gui; /**
 * Clothing Store Project
 * Diego Cortes and Aboubacar Traore
 * CSC210
 * December 4, 2024
 * This is my main class where we made clothing store with Multiple Purchases Selections possible
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClothingStore {
    // Constants
    private static final double TAX_RATE = 0.08;
    private static final int CREDENTIAL_USERNAME_INDEX = 0;
    private static final int CREDENTIAL_PASSWORD_INDEX = 1;

    // The data of my store
    private final String[][] credentials;
    private final String[] itemNames;
    private final double[] itemPrices;

    // PurchaseItem inner class
    public static class PurchaseItem {
        private String itemName;
        private int quantity;
        private double itemTotal;

        public PurchaseItem(String itemName, int quantity) {
            this.itemName = itemName;
            this.quantity = quantity;
        }

        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getItemTotal() { return itemTotal; }
        public void setItemTotal(double itemTotal) { this.itemTotal = itemTotal; }
    }

    // Constructor
    public ClothingStore() {
        this.credentials = new String[][]{{"Macys", "1 Herald Square"}};
        this.itemNames = new String[]{"T-Shirt", "Jeans", "Jacket", "Shoes"};
        this.itemPrices = new double[]{19.99, 49.99, 79.99, 59.99};
    }

    // Validating credential
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return inputUsername.equals(getUsername()) &&
                inputPassword.equals(getPassword());
    }

    // Let's letter the Methods
    public String getUsername() {
        return credentials[0][CREDENTIAL_USERNAME_INDEX];
    }

    public String getPassword() {
        return credentials[0][CREDENTIAL_PASSWORD_INDEX];
    }

    public String[] getItemNames() {
        return itemNames.clone();
    }

    public double[] getItemPrices() {
        return itemPrices.clone();
    }

    // those are the price Retrieval
    public double getItemPrice(String itemName) {
        if (itemName == null) return -1;

        for (int i = 0; i < itemNames.length; i++) {
            if (itemNames[i].equalsIgnoreCase(itemName)) {
                return itemPrices[i];
            }
        }
        return -1;
    }

    // Flexibility about Multi-Item Purchase Method
    public PurchaseResult calculateMultiItemPurchase(List<PurchaseItem> items) {
        if (items == null || items.isEmpty()) {
            return new PurchaseResult(-1, "No items selected");
        }

        double totalPreTax = 0;
        Map<String, Double> itemBreakdown = new HashMap<>();

        // Let's calculate pre-tax cost for each item
        for (PurchaseItem item : items) {
            String itemName = item.getItemName();
            int quantity = item.getQuantity();

            // Let's validate item and quantity
            if (quantity <= 0) {
                return new PurchaseResult(-1, "Invalid quantity for " + itemName);
            }

            double itemPrice = getItemPrice(itemName);
            if (itemPrice <= 0) {
                return new PurchaseResult(-1, "Invalid item: " + itemName);
            }

            double itemTotal = itemPrice * quantity;
            totalPreTax += itemTotal;
            itemBreakdown.put(itemName, itemTotal);
            item.setItemTotal(itemTotal);
        }

        // Let's calculate tax and total
        double tax = calculateTax(totalPreTax);
        double totalWithTax = totalPreTax + tax;

        return new PurchaseResult(totalWithTax, itemBreakdown, totalPreTax, tax);
    }

    // Existing tax calculation method
    public double calculateTax(double cost) {
        return cost > 0 ? cost * TAX_RATE : -1;
    }

    // This  is the result wrapper class for multi-item purchase
    public static class PurchaseResult {
        private final double totalCost;
        private final Map<String, Double> itemBreakdown;
        private final double preTaxTotal;
        private final double tax;
        private final String errorMessage;

        // This is my constructor for successful purchase
        public PurchaseResult(double totalCost, Map<String, Double> itemBreakdown,
                              double preTaxTotal, double tax) {
            this.totalCost = totalCost;
            this.itemBreakdown = itemBreakdown;
            this.preTaxTotal = preTaxTotal;
            this.tax = tax;
            this.errorMessage = null;
        }

        // My constructor for error
        public PurchaseResult(double totalCost, String errorMessage) {
            this.totalCost = totalCost;
            this.itemBreakdown = null;
            this.preTaxTotal = -1;
            this.tax = -1;
            this.errorMessage = errorMessage;
        }

        // Getters
        public double getTotalCost() { return totalCost; }
        public Map<String, Double> getItemBreakdown() { return itemBreakdown; }
        public double getPreTaxTotal() { return preTaxTotal; }
        public double getTax() { return tax; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isValid() { return errorMessage == null; }
    }
}