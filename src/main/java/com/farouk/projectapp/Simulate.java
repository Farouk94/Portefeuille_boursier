/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

import java.math.BigDecimal;

/**
 * Class that calculates Buy And Sell prices of a company.
 *
 * @author farou_000
 */
public class Simulate {

    /**
     * Simulates buy of a company by taking it's name, the quantity desired, and
     * the user making the operation.
     *
     * @param name
     * @param quantity
     * @param userID
     * @return result
     */
    public static BigDecimal simulateBuy(String name, int quantity, int userID) {
        SQLConnectEmp.refreshOneCompanyOnlyAtSearch(name);
        Company com = SQLConnectEmp.lookForCompany(name, userID);
        String disturbed = Integer.toString(quantity);
        BigDecimal aa = new BigDecimal(disturbed);
        BigDecimal result = com.getBidPrice().multiply(aa);
        return result;
    }

    /**
     * Simulates sell of a company by taking it's name, the quantity desired,
     * and the user making the operation.
     *
     * @param name
     * @param quantity
     * @param userID
     * @return result
     */
    public static BigDecimal simulateSell(String name, int quantity, int userID) {
        SQLConnectEmp.refreshOneCompanyOnlyAtSearch(name);
        Company com = SQLConnectEmp.lookForCompany(name, userID);
        String disturbed = Integer.toString(quantity);
        BigDecimal aa = new BigDecimal(disturbed);
        BigDecimal result = com.getAskPrice().multiply(aa);
        return result;
    }

}
