/*
 * TeamPirates
 */
package com.farouk.projectapp;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * This is a Company class that is used to store data from Yahoo Finance.
 *
 * @author farou_000
 *
 */
public class Company {

    private String name;
    private String symbol;
    private BigDecimal stockPrice;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private int numberOwned;

    /**
     * Empty Constructor for the class
     */
    public Company() {

    }

    public Company(String name, String symbol, BigDecimal stockPrice, BigDecimal bidPrice, BigDecimal askPrice, int numberOwned) {
        this.name = name;
        this.symbol = symbol;
        this.stockPrice = stockPrice;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.numberOwned = numberOwned;

    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     *
     * @param symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     *
     *
     * @return stockPrice
     */
    public BigDecimal getStockPrice() {
        return stockPrice;
    }

    /**
     *
     *
     * @param stockPrice
     */
    public void setStockPrice(BigDecimal stockPrice) {
        this.stockPrice = stockPrice;
    }

    /**
     *
     *
     * @return bidPrice
     */
    public BigDecimal getBidPrice() {
        return bidPrice;
    }

    /**
     *
     * @param bidPrice
     */
    public void setBidPrice(BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }

    /**
     *
     *
     * @return askPrice
     */
    public BigDecimal getAskPrice() {
        return askPrice;
    }

    /**
     *
     *
     * @param askPrice
     */
    public void setAskPrice(BigDecimal askPrice) {
        this.askPrice = askPrice;
    }

    /**
     *
     *
     * @return numberOwned
     */
    public int getNumberOwned() {
        return numberOwned;
    }

    /**
     *
     *
     * @param numberOwned
     */
    public void setNumberOwned(int numberOwned) {
        this.numberOwned = numberOwned;
    }

    @Override
    public String toString() {
        return "Company{" + "name=" + name + ", symbol=" + symbol + ", stockPrice=" + stockPrice + ", bidPrice=" + bidPrice + ", askPrice=" + askPrice + ", numberOwned=" + numberOwned + '}';
    }

}
