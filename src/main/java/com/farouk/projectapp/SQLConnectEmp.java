/*
 * TeamPirates.
 */
package com.farouk.projectapp;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 * This is a class to interact with the database.
 *
 * @author farou_000
 */
public class SQLConnectEmp {

    private static Connection con = null;
    private static final java.text.SimpleDateFormat sdf
            = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

    /**
     * 
     * @param args useless
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException {

    }

    /**
     * method that looks for messages received
     *
     * @param userID
     * @return number of messages not read.
     */
    public static int checkForMessages(int userID) {
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select * from messages where read='No' and userID=" + userID;
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet r = st.executeQuery();
            while (r.next()) {
                a++;
            }
            return a;
        } catch (Exception e) {
            System.err.println("Prob in checking mess.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("No close operation.\n" + e);
            }
        }

        return a;
    }

    /**
     * method that changes read status of a message sent by a manager to an
     * employee.
     *
     * @param userID
     */
    public static void markMessageAsRead(int userID) {
        try {
            con = ConnectDB();
            String sql = "update messages set read='YES' where userID=" + userID;
            PreparedStatement st = con.prepareStatement(sql);
            st.executeUpdate();

        } catch (Exception e) {
            System.err.println("Prob in registering transaction.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("No close operation.\n" + e);
            }
        }
    }

    /**
     * method to register transactions made by an employee
     *
     * @param userID
     * @param symbol
     * @param ope operation 'buy or sell'
     * @param quantity
     * @param price price paid
     */
    public static void registerTransaction(int userID, String symbol, String ope, int quantity, BigDecimal price) {
        try {
            con = ConnectDB();
            try (Statement st = con.createStatement()) {
                String sql = "insert into transactions(userID,comSymbol,operation,quantity,pricePaid,date) values(" + userID + ", '" + symbol + "', '" + ope + "', " + quantity + "," + price + ",'" + getCurrentDate().toLocaleString() + "')";
                st.addBatch(sql);
                st.executeBatch();
            } catch (Exception e) {
                System.err.println("Prob in registering transaction first.\n" + e);
            }
        } catch (Exception e) {
            System.err.println("Prob in registering transaction.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("No close operation.\n" + e);
            }
        }
    }

    /**
     * method used for word suggestion in the search area.
     *
     * @param beg a character or a string that may compose a company name
     * @return Collection of possible company names.
     */
    public static Collection<String> getNameForSearch(String beg) {
        Collection<String> names = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select CNAME from companiesList where CNAME like '%" + beg + "%'";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                names.add(res.getString("CNAME"));
            }
            if (names.isEmpty()) {
                names.add("Sorry, no match!");
            }
            return names;
        } catch (Exception e) {
            System.err.println("Prob in searchname completion.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Con no close.\n" + e);
            }
        }
        return null;
    }

    //Report Companies
    /**
     * method that checks if a company is reported by an employee
     *
     * @param userID
     * @param comName
     * @return value.
     */
    public static int ifReported(int userID, String comName) {
        String symbol = getSymbolOfCompanyFromDB(comName);
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select reported" + userID + " from companiesList where SYMBOL='" + symbol + "'";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                a = res.getInt("reported" + userID);
            }
            return a;
        } catch (Exception e) {
            System.err.println("Sorry problem in getting reported.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.2.\n" + e);
            }
        }
        return a;
    }

    /**
     * method to update company info in the user's column, and inserting the
     * name and userID into a table, all in the database.
     *
     * @param userID
     * @param comName name of company
     */
    public static void markCompanyAsReported(int userID, String comName) {
        String symbol = getSymbolOfCompanyFromDB(comName);
        int a = ifReported(userID, comName);
        try {
            con = ConnectDB();
            if (a == 0) {
                String sql = "update companiesList set reported" + userID + "=1 where SYMBOL='" + symbol + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
                try (Statement st2 = con.createStatement()) {
                    String sql2 = "insert into reportingCom(userID,comSymbol,date) values(" + userID + ", '" + symbol + "', '" + getCurrentDate().toLocaleString() + "')";
                    st2.addBatch(sql2);
                    st2.executeBatch();
                } catch (Exception e) {
                    System.err.println("Prob in registering report first.\n" + e);
                }
            } else {
                String sql = "update companiesList set reported" + userID + "=0 where SYMBOL='" + symbol + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Sorry problem in reported.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.2.\n" + e);
            }
        }
    }

    //Automation process
    /**
     * method to perform automation of several companies chose by an employee.
     *
     * @param userID
     */
    public static void performAutomation(int userID) {
        System.out.println("Performing auto !!!");
        Collection<String> symb = getCompAutomated(userID);
        int buy = 0;
        int sell = 0;
        int quantity = 0;
        BigDecimal minPrice = new BigDecimal(0);
        BigDecimal maxPrice = new BigDecimal(0);
        try {
            con = ConnectDB();

            for (String a : symb) {
                System.out.println("for :" + a);
                String sql = "select minPrice,maxPrice,quantity,buy, sell from automationTable where userID=" + userID + " and comSymbol='" + a + "'";
                PreparedStatement st = con.prepareStatement(sql);
                ResultSet res = st.executeQuery();
                while (res.next()) {
                    buy = res.getInt("buy");
                    sell = res.getInt("sell");
                    quantity = res.getInt("quantity");
                    minPrice = res.getBigDecimal("minPrice");
                    maxPrice = res.getBigDecimal("maxPrice");
                }
                if ((StockInformation.getStockPriceOfCompany(a).compareTo(minPrice) == 1) && (StockInformation.getStockPriceOfCompany(a).compareTo(maxPrice) == -1)) {
                    if (buy == 1) {
                        updateQuantityOfComapnyAfterBuy(a, quantity, userID);
                        registerTransaction(userID, a, "Buy", quantity, StockInformation.getStockPriceOfCompany(a).multiply(new BigDecimal(quantity)));
                    } else if (sell == 1) {
                        updateQuantityOfComapnyAfterSell(a, quantity, userID);
                        registerTransaction(userID, a, "Sell", quantity, StockInformation.getStockPriceOfCompany(a).multiply(new BigDecimal(quantity)));
                    }
                } else if ((StockInformation.getStockPriceOfCompany(a).compareTo(maxPrice) == 1) && (buy == 0) && (sell == 0)) {
                    JOptionPane.showMessageDialog(null, "The company : " + a + " has reached your desired price of : " + String.valueOf(StockInformation.getStockPriceOfCompany(a).doubleValue()) + "€.");
                }
            }

        } catch (Exception e) {
            System.err.println("Sorry problem in performing auto.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.2.\n" + e);
            }
        }
    }

    /**
     * method that checks if a company is automated by an employee.
     *
     * @param userID
     * @param name
     * @return value on that company.
     */
    public static int ifAutomated(int userID, String comName) {
        String symbol = getSymbolOfCompanyFromDB(comName);
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select automated" + userID + " from companiesList where SYMBOL='" + symbol + "'";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();

            while (res.next()) {
                a = res.getInt("automated" + userID);
            }
            return a;
        } catch (Exception e) {
            System.err.println("Sorry problem in getting automa.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.2.\n" + e);
            }
        }
        return a;
    }

    /**
     * method that checks if a company is automated by an employee.
     *
     * @param userID
     * @param name
     * @return text with info on that automation.
     */
    public static String checkIfAutomated(int userID, String name) {
        String sym = getSymbolOfCompanyFromDB(name);
        int buy = 0;
        int sell = 0;
        int quantity = 0;
        double minPrice = 0;
        double maxPrice = 0;
        try {
            con = ConnectDB();
            String sql = "select minPrice,maxPrice,quantity,buy, sell from automationTable where userID=" + userID + " and comSymbol='" + sym + "'";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                buy = res.getInt("buy");
                sell = res.getInt("sell");
                quantity = res.getInt("quantity");
                minPrice = res.getDouble("minPrice");
                maxPrice = res.getDouble("maxPrice");
            }

            if (buy == 1) {
                return "This company is automated for buying " + quantity + " at price between : " + minPrice + "€ and " + maxPrice + "€.";
            } else if (sell == 1) {
                return "This company is automated for selling " + quantity + " at price between : " + minPrice + "€ and " + maxPrice + "€.";
            } else if ((buy == 0) && (sell == 0)) {
                return "Alerted for price between : " + minPrice + "€ and " + maxPrice + "€.";
            }

        } catch (Exception e) {
            System.err.println("Sorry problem in performing auto.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.2.\n" + e);
            }
        }

        return "";
    }

    /**
     * method that deletes automation of a company of an employee from the
     * database.
     *
     * @param userID
     * @param name
     */
    public static void removeAutomation(int userID, String name) {
        String sym = getSymbolOfCompanyFromDB(name);
        try {
            con = ConnectDB();
            PreparedStatement get = con.prepareStatement("DELETE FROM automationTable where userID=" + userID + " and comSymbol='" + sym + "'");
            get.executeUpdate();
            PreparedStatement pa = con.prepareStatement("update companiesList set automated" + userID + "=0 where SYMBOL ='" + sym + "'");
            pa.executeUpdate();
        } catch (Exception e) {
            System.err.println("prob in removing auto.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("cant close co.\n" + e);
            }
        }
    }

    /**
     * method that gets the automated companies of an employee from the
     * database.
     *
     * @param userID
     * @return Collection of company symbols.
     */
    private static Collection getCompAutomated(int userID) {
        Collection<String> symbols = new ArrayList<>();
        try {
            con = ConnectDB();
            String check = " select comSymbol from automationTable where userID=" + userID;
            PreparedStatement chk = con.prepareStatement(check);
            ResultSet res = chk.executeQuery();
            while (res.next()) {
                symbols.add(res.getString("comSymbol"));
            }

            return symbols;
        } catch (Exception e) {
            System.err.println("Prob in saving auto.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close con in save auto.\n" + e);
            }
        }

        return null;
    }

    /**
     * method for saving parameters used to perform an automation of a company
     * in the database.
     *
     * @param userID
     * @param sym symbol of company
     * @param minPrice
     * @param maxPrice
     * @param quantity
     * @param buy
     * @param sell
     */
    public static void saveAutomateSettings(int userID, String sym, BigDecimal minPrice, BigDecimal maxPrice, int quantity, int buy, int sell) {
        Collection<String> temp = getCompAutomated(userID);
        boolean flag = true;
        try {
            con = ConnectDB();
            for (String a : temp) {
                if (a.equals(sym)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }

            if (flag == false || temp.isEmpty()) {
                try (Statement st = con.createStatement()) {
                    String sql = "insert into automationTable(userID,comSymbol,minPrice,maxPrice,quantity,buy,sell,date) values(" + userID + ",'" + sym + "'," + minPrice + "," + maxPrice + "," + quantity + "," + buy + "," + sell + ", '" + getCurrentDate().toLocaleString() + "')";
                    st.addBatch(sql);
                    st.executeBatch();
                    if ((buy == 1) || (sell == 1)) {
                        PreparedStatement pa = con.prepareStatement("update companiesList set automated" + userID + "=1 where SYMBOL ='" + sym + "'");
                        pa.executeUpdate();
                    } else if ((buy == 0) && (sell == 0)) {
                        PreparedStatement pa = con.prepareStatement("update companiesList set automated" + userID + "=2 where SYMBOL ='" + sym + "'");
                        pa.executeUpdate();
                    }

                }

            }
            if (flag == true) {
                String sql2 = "update automationTable set minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", quantity=" + quantity + ", buy=" + buy + ", sell=" + sell + ", date='" + getCurrentDate().toLocaleString() + "' where userID=" + userID + " and comSymbol='" + sym + "'";
                PreparedStatement st = con.prepareStatement(sql2);
                st.executeUpdate();
            }

        } catch (Exception e) {
            System.err.println("Prob in saving auto.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close con in save auto.\n" + e);
            }
        }
    }

    //History changes of total of Portfolio
    /**
     * method that fetches portfolio's total of an employee to draw it using
     * jFreeChart API.
     *
     * @param userID of user connected
     * @return XYDataset
     */
    public static XYDataset getInfoToDrawChart(int userID) {
        final TimeSeries series = new TimeSeries("changes of total");
        Map<java.util.Date, Double> data = new HashMap<>();
        try {
            con = ConnectDB();
            String sql = "select TOTAL,TIMESTAMP from historyTable where userID = " + userID + " order by TIMESTAMP DESC";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                data.put(sdf.parse(res.getString("TIMESTAMP")), res.getDouble("TOTAL"));
            }
            Map<java.util.Date, Double> sortedData = new TreeMap<>(data);
            try {
                Set s = sortedData.entrySet();
                Iterator it = s.iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    java.util.Date key = (java.util.Date) entry.getKey();
                    Double value = (Double) entry.getValue();

                    series.add(new Second(key), value);
                }

            } catch (Exception e) {
                System.err.println("Seurry.\n" + e);
            }

            return new TimeSeriesCollection(series);

        } catch (Exception e) {
            System.err.println("Sorry problem in chart.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Close con no.\n" + e);
            }
        }

        return null;
    }

    /**
     * method that saves total of a portfolio of a user in the database with a
     * timestamp.
     *
     * @param userID
     * @param b total of protfolio of a user.
     */
    public static void registerTotalChanges(int userID, BigDecimal b) {
        try {
            con = ConnectDB();
            try (Statement st = con.createStatement()) {

                String sql = "insert into historyTable(userID,TOTAL,TIMESTAMP) values(" + userID + "," + b + ", '" + getCurrentDate().toLocaleString() + "')";
                st.addBatch(sql);
                st.executeBatch();

            } catch (Exception e) {
                System.err.println("Problem in first try registering changes.\n" + e);
            }
        } catch (Exception e) {
            System.err.println("Problem in registering changes.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("can noot close connection.\n" + e);
            }
        }
    }

    //Login's part.
    /**
     * method used by admin to delete a certain user.
     *
     * @param name username
     */
    public static void deleteUser(String name) {
        int userID = getUserID(name);
        try {
            con = ConnectDB();
            PreparedStatement get = con.prepareStatement("DELETE FROM logins WHERE userID='" + userID + "'");
            get.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e + "sorry ");

        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Couldn't close.\n" + e);
            }
        }
    }

    /**
     * method used by the admin to perform changes on users.
     *
     * @param name username
     * @param Password
     * @param clearance
     */
    public static void editUser(String name, String Password, int clearance) {
        try {
            con = ConnectDB();
            PreparedStatement get = con.prepareStatement("UPDATE logins SET LOGIN='" + name + "', PASSWORD='" + Password + "', CLEARANCE='" + clearance + "'" + "WHERE LOGIN='" + name + "';");
            get.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e + "sorry ");

        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Couldn't close.\n" + e);
            }
        }

    }

    /**
     * Method for creating a new user.
     *
     * @param name username
     * @param password
     * @param clearance
     * @param budget
     * @return true if all is successful.
     */
    public static boolean createNewUser(String name, String password, int clearance, BigDecimal budget) {
        Collection<String> a = getLoginsFromDB();
        boolean position = true;
        try {
            con = ConnectDB();
            try (Statement st = con.createStatement()) {

                for (String s : a) {
                    if (s.equalsIgnoreCase(name)) {
                        JOptionPane.showMessageDialog(null, "Sorry, the username : '" + name + "' already exists.");
                        position = false;
                        break;
                    } else {
                        position = true;
                    }
                }
                if (position) {
                    String sql = "INSERT INTO logins(LOGIN, PASSWORD, CLEARANCE, BUDGET) VALUES('" + name + "','" + password + "','" + clearance + "','" + budget + "')";
                    st.addBatch(sql);
                    st.executeBatch();
                    st.clearBatch();
                    if (clearance == 2) {
                        int tmp = getUserID(name);
                        String sql1 = "ALTER TABLE companiesList ADD user" + tmp + " INTEGER";
                        st.addBatch(sql1);
                        st.executeBatch();
                        String sql2 = "ALTER TABLE companiesList ADD reported" + tmp + " INTEGER";
                        st.addBatch(sql2);
                        st.executeBatch();
                        String sql2_ = "ALTER TABLE companiesList ADD automated" + tmp + " INTEGER";
                        st.addBatch(sql2_);
                        st.executeBatch();
                        String sql3 = "ALTER TABLE companiesList ADD banned" + tmp + " INTEGER";
                        st.addBatch(sql3);
                        st.executeBatch();
                        String sql3_ = "update companiesList set user" + tmp + "=0, reported" + tmp + "=0, automated" + tmp + "=0, banned" + tmp + "=0";
                        st.addBatch(sql3_);
                        st.executeBatch();
                    }
                }

            }
            return position;
        } catch (SQLException e) {
            System.err.println(e + "sorry");

        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Couldn't close.\n" + e);
            }

        }
        return position;
    }

    /**
     * method that returns budget of a user.
     *
     * @param user username.
     * @return clearance.
     */
    public static int getClearanceFromDB(String user) {
        int Clear = 0;
        try {
            con = ConnectDB();
            PreparedStatement stat = con.prepareStatement("SELECT CLEARANCE FROM logins WHERE LOGIN='" + user + "'");
            ResultSet result = stat.executeQuery();
            while (result.next()) {
                Clear = result.getInt("CLEARANCE");
            }
            return Clear;
        } catch (SQLException e) {
            System.err.println("Problem in clearance.\n" + e);

        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Couldn't close.\n" + e);
            }

        }
        return 0;
    }

    /**
     * method that allows the user to change his budget.
     *
     * @param b budget
     * @param userID
     */
    public static void changeLimit(BigDecimal b, int userID) {
        try {
            con = ConnectDB();
            try (Statement stat = con.createStatement()) {
                String sql = "UPDATE logins set BUDGET=" + b + " where userID=" + userID;
                stat.addBatch(sql);
                stat.executeBatch();
            } catch (Exception e) {
                System.err.println("Problem in update budget.\n" + e);
            } finally {
                try {
                    con.close();
                } catch (Exception e) {
                    System.err.println("Ca'nt close conn.\n" + e);
                }
            }

        } catch (Exception e) {
            System.err.println("Problem in update budget.\n" + e);
        }
    }

    /**
     * method to get budget of a user.
     *
     * @param userID
     * @return budget.
     */
    public static BigDecimal getBudgetFromDB(int userID) {
        BigDecimal budget = new BigDecimal(0);
        try {
            con = ConnectDB();
            String sql = "select BUDGET from logins where userID = " + userID + "";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet r = stat.executeQuery();
            while (r.next()) {
                budget = r.getBigDecimal("BUDGET");
            }
            return budget;

        } catch (Exception e) {
            System.err.println("Problem in getbudget.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Cannot close con.\n" + e);
            }
        }

        return null;
    }

    /**
     * method to get username of a given ID.
     *
     * @param userID
     * @return username
     */
    public static String getUserName(int userID) {
        String username = "";
        try {
            con = ConnectDB();
            String sql = "select LOGIN from logins where userID ='" + userID + "'";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet r = stat.executeQuery();
            while (r.next()) {
                username = r.getString("LOGIN");
            }
            return username;
        } catch (Exception e) {
            System.err.println("Problem in get id.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection...\n" + e);
            }
        }

        return username;
    }

    /**
     * method for getting the userID for a username.
     *
     * @param username
     * @return user ID
     */
    public static int getUserID(String username) {
        int id = 0;
        try {
            con = ConnectDB();
            String sql = "select userID from logins where LOGIN ='" + username + "'";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet r = stat.executeQuery();
            while (r.next()) {
                id = r.getInt("userID");
            }
            return id;
        } catch (Exception e) {
            System.err.println("Problem in get id.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection...\n" + e);
            }
        }

        return 0;
    }

    /**
     * method to get all users info from the database.
     *
     * @return collection of Users
     */
    public static Collection<User> getUsersFromDB() {
        Collection<User> users = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "SELECT * FROM logins ";
            PreparedStatement get = con.prepareStatement(sql);
            ResultSet result = get.executeQuery();
            while (result.next()) {
                users.add(new User(result.getInt("userID"), result.getString("LOGIN"), result.getString("PASSWORD"), result.getInt("CLEARANCE"), result.getBigDecimal("BUDGET")));
            }

            return users;
        } catch (SQLException e) {
            System.err.println("Problem in get users.\n" + e);

        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Couldn't close connection.\n" + e);
            }
        }
        return null;
    }

    /**
     * method that fetches usernames from the database.
     *
     * @return Collection of usernames
     */
    public static Collection<String> getLoginsFromDB() {
        Collection<String> logins = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "Select LOGIN from logins";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                logins.add(res.getString("LOGIN"));
            }
            return logins;
        } catch (Exception e) {
            System.err.println("Problem in getLogins.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
        return null;
    }

    /**
     * method that checks if the user is in the database.
     *
     * @param L
     * @param P
     * @return true if user found in database
     */
    public static Boolean Login(String L, String P) {
        String checkUser = "";
        String checkPass = "";
        boolean login = false;
        try {
            con = ConnectDB();
            PreparedStatement get = con.prepareStatement("SELECT LOGIN,PASSWORD FROM logins WHERE LOGIN='" + L + "' AND PASSWORD= '" + P + "'");
            ResultSet result = get.executeQuery();
            while (result.next()) {
                checkUser = result.getString("LOGIN");
                checkPass = result.getString("PASSWORD");
            }

            login = (checkUser.equals(L)) && (checkPass.equals(P));

            return login;
        } catch (SQLException ex) {
            System.err.println("Sorry ");
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }

        }

        return false;
    }

    // Companies' part.
    /**
     * method used to selling all actions bought of a company
     *
     * @param name of company
     * @param userID of the user connected
     */
    public static void removeCompanyFromPortfolio(String name, int userID) {
        try {
            con = ConnectDB();
            try (
                    Statement secondStat = con.createStatement()) {
                String query = "UPDATE companiesList SET user" + userID + " =0 where CNAME ='" + name + "'";
                secondStat.addBatch(query);
                secondStat.executeBatch();

            } catch (Exception e) {
                System.err.println("Problem in first catch in update sell.\n" + e);
            }

        } catch (Exception e) {
            System.err.println("Problem in remove company.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }

    }

    /**
     * This method is called when a transaction is being held, updates sell info
     * in database.
     *
     * @param name of company
     * @param quantity
     * @param userID of the user connected
     */
    public static void updateQuantityOfComapnyAfterSell(String name, int quantity, int userID) {
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select user" + userID + " from companiesList where SYMBOL ='" + name + "'";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                a = res.getInt("user" + userID);
            }
            if (a == 0) {
                JOptionPane.showMessageDialog(null, "Sorry, You can't sell something you don't own.");
            } else if (a < quantity) {
                JOptionPane.showMessageDialog(null, "Sorry, You can't sell too much of what you have.\nYou only have " + a + ".");
            } else {
                System.out.println(a);
                int r = a - quantity;
                try (
                        Statement secondStat = con.createStatement()) {
                    String query = "UPDATE companiesList SET user" + userID + " =" + r + " where SYMBOL ='" + name + "'";
                    secondStat.addBatch(query);
                    secondStat.executeBatch();

                } catch (Exception e) {
                    System.err.println("Problem in first catch in update sell.\n" + e);
                }
            }
        } catch (Exception e) {
            System.err.println("Sorry, problem in update quantity.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
    }

    /**
     * This method is called when a transaction is being held, updates buy info
     * in database.
     *
     * @param name of company
     * @param quantity
     * @param userID of the user connected
     */
    public static void updateQuantityOfComapnyAfterBuy(String name, int quantity, int userID) {
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select user" + userID + " from companiesList where SYMBOL ='" + name + "'";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                a = res.getInt("user" + userID);
            }
            System.out.println(a);
            int r = a + quantity;
            try (
                    Statement secondStat = con.createStatement()) {
                String query = "UPDATE companiesList SET user" + userID + " =" + r + " where SYMBOL ='" + name + "'";
                secondStat.addBatch(query);
                secondStat.executeBatch();
                secondStat.close();

            } catch (Exception e) {
                System.err.println("Problem in first catch in update buy.\n" + e);
            }

        } catch (Exception e) {
            System.err.println("Sorry, problem in update quantity.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
    }

    /**
     * Refreshes prices of only one company
     *
     * @param name given.
     */
    public static void refreshOneCompanyOnlyAtSearch(String name) {
        try {
            String symbol = getSymbolOfCompanyFromDB(name);
            con = ConnectDB();
            try (
                    Statement secondStat = con.createStatement()) {
                BigDecimal price = StockInformation.getStockPriceOfCompany(symbol);
                BigDecimal bidPrice = StockInformation.getBidPriceOfCompany(symbol);
                BigDecimal askPrice = StockInformation.getAskPriceOfCompany(symbol);
                String query = "UPDATE companiesList SET STOCKPRICE=" + price + ", BIDPRICE=" + bidPrice + ", ASKPRICE=" + askPrice + ", refreshDate='" + getCurrentDate().toLocaleString() + "' where SYMBOL='" + symbol + "'";
                secondStat.addBatch(query);

                secondStat.executeBatch();

            }
        } catch (Exception e) {
            System.err.println("Problem in refresh one company.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
    }

    /**
     * Refreshing the prices of a portfolio.
     *
     * @param userID of the user connected.
     */
    public static void refreshPortfolioDataOnly(int userID) {
        Collection<String> symbols = new ArrayList<>();
        try {
            con = ConnectDB();

            String statement = "select SYMBOL from companiesList where user" + userID + " is not 0";
            PreparedStatement symbol = con.prepareStatement(statement);
            ResultSet res = symbol.executeQuery();
            while (res.next()) {
                symbols.add(res.getString("SYMBOL"));
            }
            try (
                    Statement secondStat = con.createStatement()) {

                for (String aa : symbols) {
                    BigDecimal price = StockInformation.getStockPriceOfCompany(aa);
                    BigDecimal bidPrice = StockInformation.getBidPriceOfCompany(aa);
                    BigDecimal askPrice = StockInformation.getAskPriceOfCompany(aa);
                    String query = "UPDATE companiesList SET STOCKPRICE=" + price + ", BIDPRICE=" + bidPrice + ", ASKPRICE=" + askPrice + ", refreshDate='" + getCurrentDate().toLocaleString() + "' where SYMBOL='" + aa + "'";
                    secondStat.addBatch(query);

                    secondStat.executeBatch();

                }
            }

        } catch (Exception e) {
            System.err.println("Problem in refreshPortfolio.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }

    }

    /**
     * Gets bought companies of a user.
     *
     * @param userID
     * @return Collection of companies
     */
    public static Collection<Company> getPortfolioCompanies(int userID) {
        Collection<Company> coms = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select SYMBOL,CNAME,STOCKPRICE,BIDPRICE,ASKPRICE,user" + userID + " from companiesList where user" + userID + " is not 0";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet res = stat.executeQuery();
            while (res.next()) {

                coms.add(new Company(res.getString("CNAME"), res.getString("SYMBOL"), res.getBigDecimal("STOCKPRICE"), res.getBigDecimal("BIDPRICE"), res.getBigDecimal("ASKPRICE"), res.getInt("user" + userID)));
            }
            return coms;
        } catch (Exception e) {
            System.err.println("Problem in portfolio companies.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
        return null;
    }

    /**
     * Fills an instance Company with the required info from the database.
     *
     * @param name of company
     * @param userID
     * @return Company
     */
    public static Company lookForCompany(String name, int userID) {
        Company com = new Company();
        try {
            String symbol = getSymbolOfCompanyFromDB(name);
            con = ConnectDB();
            System.out.println(symbol);
            String sql = "Select SYMBOL,CNAME,STOCKPRICE,BIDPRICE,ASKPRICE,user" + userID + " from companiesList where SYMBOL='" + symbol + "'";
            PreparedStatement stat = con.prepareStatement(sql);
            ResultSet res = stat.executeQuery();
            while (res.next()) {
                com.setSymbol(res.getString("SYMBOL"));
                com.setName(res.getString("CNAME"));
                com.setStockPrice(res.getBigDecimal("STOCKPRICE"));
                com.setBidPrice(res.getBigDecimal("BIDPRICE"));
                com.setAskPrice(res.getBigDecimal("ASKPRICE"));
                com.setNumberOwned(res.getInt("user" + userID));
            }
            return com;
        } catch (Exception e) {
            System.err.println("Sorry, there was a problem in lookForCompany.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }
        return null;
    }

    /**
     * Returns all names of the companies from the database.
     *
     * @return collection of name of companies.
     */
    public static Collection<String> getAllCompaniesNames() {
        Collection<String> cnames = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "Select CNAME from companiesList";
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                cnames.add(res.getString("CNAME"));
            }
            return cnames;

        } catch (Exception e) {
            System.err.println("Sorry There was a problem.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }

        return null;
    }

    /**
     * Gets the symbols of all the companies from the database.
     *
     * @return symbols
     */
    public static Collection getSymbols() {
        Collection<String> symbols = new ArrayList<>();
        try {
            String statement = "select SYMBOL from companiesList";
            PreparedStatement symbol = con.prepareStatement(statement);
            ResultSet res = symbol.executeQuery();
            while (res.next()) {
                symbols.add(res.getString("SYMBOL"));
            }
            return symbols;

        } catch (Exception e) {
            System.err.println("There is a problem");
        }

        return null;

    }

    /**
     * Updating all prices of companies in the database.
     */
    public static void refreshAllData() {
        Collection<String> symbols = new ArrayList<>();
        try {
            con = ConnectDB();
            symbols = getSymbols();

            try (
                    Statement secondStat = con.createStatement()) {
                final int batchSize = 10;
                int count = 0;
                for (String aa : symbols) {
                    BigDecimal price = StockInformation.getStockPriceOfCompany(aa);
                    BigDecimal bidPrice = StockInformation.getBidPriceOfCompany(aa);
                    BigDecimal askPrice = StockInformation.getAskPriceOfCompany(aa);
                    String query = "UPDATE companiesList SET STOCKPRICE=" + price + ", BIDPRICE=" + bidPrice + ", ASKPRICE=" + askPrice + ", refreshDate='" + getCurrentDate().toLocaleString() + "' where SYMBOL='" + aa + "'";
                    secondStat.addBatch(query);
                    System.out.println(count);
                    if (++count % batchSize == 0) {
                        secondStat.executeBatch();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection.\n" + e);
            }
        }

    }

    /**
     * Gets the symbol of a company from the database.
     *
     * @param s name of company.
     * @return Symbol
     */
    public static String getSymbolOfCompanyFromDB(String s) {
        String a = "";
        try {
            con = ConnectDB();
            String statement = "select SYMBOL from companiesList where CNAME='" + s + "'";
            PreparedStatement symbol = con.prepareStatement(statement);

            ResultSet res = symbol.executeQuery();
            while (res.next()) {
                a = res.getString("SYMBOL");
            }
            return a;

        } catch (Exception e) {
            System.err.println("Sorry Company '" + s + "' not found.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Can't close connection");
            }
        }

        return null;
    }

    /**
     * Connects to 'sqlite' database.
     *
     * @return Connection
     */
    public static Connection ConnectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:project.sqlite");
            return con;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Sorry, connection can't be established.\nCall for your IT Departement and show them this message :\n" + e);
        }
        return null;
    }

    /**
     * Getting current date
     *
     * @return date
     */
    public static Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new Date(today.getTime());
    }

}
