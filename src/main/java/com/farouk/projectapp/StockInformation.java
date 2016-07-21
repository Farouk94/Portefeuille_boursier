/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

import java.math.BigDecimal;
import java.math.MathContext;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

/**
 * Class that uses YahooFinance API to get stock info of a company.
 *
 * @author farou_000
 */
public class StockInformation {

    private final static BigDecimal EUR_CONVERSION = new BigDecimal(0.910);
    private final static MathContext MC = new MathContext(4);

    /**
     * Get Stock Price of company from Yahoo! finance.
     *
     * @param s
     * @return stockPrice.
     */
    public static BigDecimal getStockPriceOfCompany(String s) {
        BigDecimal stockPrice;
        Stock company = YahooFinance.get(s);
        StockQuote r = company.getQuote();
        stockPrice = r.getPrice().multiply(EUR_CONVERSION, MC);
        return stockPrice;
    }

    /**
     * Get Bid Price of company from Yahoo! finance.
     *
     * @param s
     * @return bidPrice
     */
    public static BigDecimal getBidPriceOfCompany(String s) {
        BigDecimal bidPrice;
        Stock company = YahooFinance.get(s);
        StockQuote r = company.getQuote();
        bidPrice = r.getBid().multiply(EUR_CONVERSION, MC);
        return bidPrice;
    }

    /**
     * Get Ask Price of company from Yahoo! finance.
     *
     * @param s
     * @return askPrice
     */
    public static BigDecimal getAskPriceOfCompany(String s) {
        BigDecimal askPrice;
        Stock company = YahooFinance.get(s);
        StockQuote r = company.getQuote();
        askPrice = r.getAsk().multiply(EUR_CONVERSION, MC);
        return askPrice;
    }

}
