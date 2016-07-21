/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author farou_000
 */
public class SQLConnectManaTest {
    
    public SQLConnectManaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class SQLConnectMana.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        SQLConnectMana.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendMessage method, of class SQLConnectMana.
     */
    @Test
    public void testSendMessage() {
        System.out.println("sendMessage");
        int userID = 0;
        String message = "";
        SQLConnectMana.sendMessage(userID, message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessages method, of class SQLConnectMana.
     */
    @Test
    public void testGetMessages() {
        System.out.println("getMessages");
        int userID = 0;
        Collection<Message> expResult = null;
        Collection<Message> result = SQLConnectMana.getMessages(userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBannedCompForAll method, of class SQLConnectMana.
     */
    @Test
    public void testGetBannedCompForAll() {
        System.out.println("getBannedCompForAll");
        Collection<String> expResult = null;
        Collection<String> result = SQLConnectMana.getBannedCompForAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeBanForAll method, of class SQLConnectMana.
     */
    @Test
    public void testRemoveBanForAll() {
        System.out.println("removeBanForAll");
        String comName = "";
        SQLConnectMana.removeBanForAll(comName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ifBannedForAll method, of class SQLConnectMana.
     */
    @Test
    public void testIfBannedForAll() {
        System.out.println("ifBannedForAll");
        String symbol = "";
        int expResult = 0;
        int result = SQLConnectMana.ifBannedForAll(symbol);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addBannedForAll method, of class SQLConnectMana.
     */
    @Test
    public void testAddBannedForAll() {
        System.out.println("addBannedForAll");
        String symbol = "";
        SQLConnectMana.addBannedForAll(symbol);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransByCompany method, of class SQLConnectMana.
     */
    @Test
    public void testGetTransByCompany() {
        System.out.println("getTransByCompany");
        String symbol = "";
        Collection<Transaction> expResult = null;
        Collection<Transaction> result = SQLConnectMana.getTransByCompany(symbol);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransactionsBtwnDates method, of class SQLConnectMana.
     */
    @Test
    public void testGetTransactionsBtwnDates() {
        System.out.println("getTransactionsBtwnDates");
        int userID = 0;
        String start = "";
        String end = "";
        Collection<Transaction> expResult = null;
        Collection<Transaction> result = SQLConnectMana.getTransactionsBtwnDates(userID, start, end);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ifBanned method, of class SQLConnectMana.
     */
    @Test
    public void testIfBanned() {
        System.out.println("ifBanned");
        int userID = 0;
        String symbol = "";
        int expResult = 0;
        int result = SQLConnectMana.ifBanned(userID, symbol);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addBanned method, of class SQLConnectMana.
     */
    @Test
    public void testAddBanned() {
        System.out.println("addBanned");
        int userID = 0;
        String reportedToBan = "";
        SQLConnectMana.addBanned(userID, reportedToBan);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNameOfReported method, of class SQLConnectMana.
     */
    @Test
    public void testGetNameOfReported() {
        System.out.println("getNameOfReported");
        int userID = 0;
        Collection<Company> expResult = null;
        Collection<Company> result = SQLConnectMana.getNameOfReported(userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLatestTotal method, of class SQLConnectMana.
     */
    @Test
    public void testGetLatestTotal() {
        System.out.println("getLatestTotal");
        int userID = 0;
        double expResult = 0.0;
        double result = SQLConnectMana.getLatestTotal(userID);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransactions method, of class SQLConnectMana.
     */
    @Test
    public void testGetTransactions() {
        System.out.println("getTransactions");
        int userID = 0;
        Collection<Transaction> expResult = null;
        Collection<Transaction> result = SQLConnectMana.getTransactions(userID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getComNameFromSymbol method, of class SQLConnectMana.
     */
    @Test
    public void testGetComNameFromSymbol() {
        System.out.println("getComNameFromSymbol");
        String sym = "";
        String expResult = "";
        String result = SQLConnectMana.getComNameFromSymbol(sym);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmployeesFromDb method, of class SQLConnectMana.
     */
    @Test
    public void testGetEmployeesFromDb() {
        System.out.println("getEmployeesFromDb");
        Collection<User> expResult = null;
        Collection<User> result = SQLConnectMana.getEmployeesFromDb();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
