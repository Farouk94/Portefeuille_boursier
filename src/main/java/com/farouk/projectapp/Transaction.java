/*
 * TeamPirates
 */
package com.farouk.projectapp;

/**
 * Class used to store transactions of an employee. Used by manager.
 *
 * @author farou_000
 */
class Transaction {

    private String user;
    private String symbol;
    private String operation;
    private int quantity;
    private double pricePaid;
    private String date;

    /**
     * Constructor to fill form
     *
     * @param user
     * @param symbol
     * @param operation
     * @param quantity
     * @param pricePaid
     * @param date
     */
    public Transaction(String user, String symbol, String operation, int quantity, double pricePaid, String date) {
        this.user = user;
        this.symbol = symbol;
        this.operation = operation;
        this.quantity = quantity;
        this.pricePaid = pricePaid;
        this.date = date;
    }

    /**
     * Another constructor.
     *
     * @param symbol
     * @param operation
     * @param quantity
     * @param pricePaid
     * @param date
     */
    public Transaction(String symbol, String operation, int quantity, double pricePaid, String date) {
        this.symbol = symbol;
        this.operation = operation;
        this.quantity = quantity;
        this.pricePaid = pricePaid;
        this.date = date;
    }

    /**
     *
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @return symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @return operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     *
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @return pricePaid
     */
    public double getPricePaid() {
        return pricePaid;
    }

    /**
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

}
