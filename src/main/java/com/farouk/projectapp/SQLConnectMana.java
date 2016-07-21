package com.farouk.projectapp;

import static com.farouk.projectapp.SQLConnectEmp.ConnectDB;
import static com.farouk.projectapp.SQLConnectEmp.getCurrentDate;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;

/**
 * Class for the manager interface that interacts with the 'sqlite' database.
 *
 * @author TeamPirates.
 */
public class SQLConnectMana {

    private static Connection con = null;

    public static void main(String[] args) {

    }

    /**
     * method that sends messages to employees.
     *
     * @param userID
     * @param message the message from the manager
     */
    public static void sendMessage(int userID, String message) {
        try {
            con = ConnectDB();
            try (Statement st = con.createStatement()) {
                String sql = "insert into messages(userID, message,date,read) values(" + userID + ",'" + message + "','" + getCurrentDate().toLocaleString() + "','No')";
                st.addBatch(sql);
                st.executeBatch();
            } catch (Exception e) {
                System.err.println("Prob in send dd messa.\n" + e);
            }
        } catch (Exception e) {
            System.err.println("Problem in sending the messages.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damnit .\n" + e);
            }
        }
    }

    /**
     * method to show sent messages to an employee.
     *
     * @param userID
     * @return list of messages
     */
    public static Collection<Message> getMessages(int userID) {
        Collection<Message> messages = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select * from messages where userID=" + userID + " order by date DESC";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                messages.add(new Message(userID, res.getString("message"), res.getString("date"), res.getString("read")));
            }
            if (messages.isEmpty()) {
                messages.add(new Message(0, "No messages", "", ""));
            }
            return messages;
        } catch (Exception e) {
            System.err.println("Problem in getting the messages.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damnit .\n" + e);
            }
        }
        return null;
    }

    /**
     * method to show what are the companies that are banned for all employees.
     *
     * @return list of companies
     */
    public static Collection<String> getBannedCompForAll() {
        Collection<String> ban = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select comName from bannedForAll";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                ban.add(res.getString("comName"));
            }
            if (ban.isEmpty()) {
                ban.add("Nothing banned.");
            }
            return ban;
        } catch (Exception e) {
            System.err.println("Problem in getting the names of banned comps.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }

        return null;
    }

    /**
     * remove ban of a company for all employees.
     *
     * @param comName
     */
    public static void removeBanForAll(String comName) {
        String reportedToBan = SQLConnectEmp.getSymbolOfCompanyFromDB(comName);
        Collection<Integer> ids = getIdsOfAll();
        try {
            con = ConnectDB();
            for (int userID : ids) {
                String sql = "update companiesList set banned" + userID + "=0 where SYMBOL='" + reportedToBan + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
            }
            try (Statement stat = con.createStatement()) {
                String sql = "delete from bannedForAll where comSymbol='" + reportedToBan + "'";
                stat.addBatch(sql);
                stat.executeBatch();
            } catch (Exception e) {
                System.err.println("Inserting into bannedForAll.\n" + e);
            }
        } catch (Exception e) {
            System.err.println("Problem in setting ban.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
    }

    /**
     * method that check if a company is banned for all employees.
     *
     * @param symbol
     * @return state of banned
     */
    public static int ifBannedForAll(String symbol) {
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select * from bannedForAll where comSymbol='" + symbol + "'";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet res = st.executeQuery();
            while (res.next()) {
                a++;
            }
            return a;
        } catch (Exception e) {
            System.err.println("Problem in getting ban.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
        return a;
    }

    /**
     * method to make a company banned for all employees.
     *
     * @param symbol
     */
    public static void addBannedForAll(String symbol) {
        String comName = getComNameFromSymbol(symbol);
        Collection<Integer> ids = getIdsOfAll();
        try {
            con = ConnectDB();
            for (int userID : ids) {
                String sql = "update companiesList set banned" + userID + "=1 where SYMBOL='" + symbol + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
                try (Statement stat = con.createStatement()) {
                    String sql_ = "delete from automationTable where userID=" + userID + " and comSymbol='" + symbol + "'";
                    stat.addBatch(sql_);
                    stat.executeBatch();
                } catch (Exception e) {
                    System.err.println("Inserting into bannedForAll.\n" + e);
                }
            }
            try (Statement stat = con.createStatement()) {
                String sql = "insert into bannedForAll(comSymbol,comName) values('" + symbol + "', '" + comName + "')";
                stat.addBatch(sql);
                stat.executeBatch();
            } catch (Exception e) {
                System.err.println("Inserting into bannedForAll.\n" + e);
            }
        } catch (Exception e) {
            System.err.println("Problem in setting ban.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
    }

    /**
     * method to get ids of employees from database.
     *
     * @return list of ids
     */
    private static Collection<Integer> getIdsOfAll() {
        Collection<User> users = getEmployeesFromDb();
        Collection<Integer> ids = new ArrayList<>();
        for (User u : users) {
            ids.add(u.getId());
        }
        return ids;
    }

    /**
     * method to get transactions made by all employees for a company.
     *
     * @param symbol
     * @return list of transactions
     */
    public static Collection<Transaction> getTransByCompany(String symbol) {
        Collection<Transaction> trn = new ArrayList<>();

        try {
            con = ConnectDB();
            String sql = "select * from transactions where comSymbol='" + symbol + "' order by date DESC";
            PreparedStatement st = con.prepareStatement(sql);

            ResultSet res = st.executeQuery();
            while (res.next()) {
                trn.add(new Transaction(SQLConnectEmp.getUserName(res.getInt("userID")), symbol, res.getString("operation"), res.getInt("quantity"), res.getDouble("pricePaid"), res.getString("date")));
            }
            if (trn.isEmpty()) {
                trn.add(new Transaction("No one", symbol, "No Operation", 0, 0, "Nothing"));
            }
            return trn;
        } catch (Exception e) {
            System.err.println("Problem in getting transactions Comp.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
        return null;
    }

    /**
     * method to filter transaction search.
     *
     * @param userID
     * @param start date
     * @param end date
     * @return list of transactions
     */
    public static Collection<Transaction> getTransactionsBtwnDates(int userID, String start, String end) {
        Collection<Transaction> trn = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select * from transactions where userID=" + userID + " and date between '" + start + "' and '" + end + "' order by date DESC";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                String name = getComNameFromSymbol(res.getString("comSymbol"));
                trn.add(new Transaction(name, res.getString("operation"), res.getInt("quantity"), res.getDouble("pricePaid"), res.getString("date")));
            }
            if (trn.isEmpty()) {
                trn.add(new Transaction("Nothing", "No operation", 0, 0, "Nothing"));
            }
            return trn;
        } catch (Exception e) {
            System.err.println("Problem in getting transactions DATES.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
        return null;
    }

    /**
     * method that check if a company is banned.
     *
     * @param userID
     * @param symbol
     * @return state of banned
     */
    public static int ifBanned(int userID, String symbol) {
        int a = 0;
        try {
            con = ConnectDB();
            String sql = "select banned" + userID + " from companiesList where SYMBOL= '" + symbol + "'";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                a = res.getInt("banned" + userID);
            }

            return a;
        } catch (Exception e) {
            System.err.println("Problem in getting the banned Company.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
        return a;
    }

    /**
     * method that makes a company banned for an employee.
     *
     * @param userID
     * @param reportedToBan
     */
    public static void addBanned(int userID, String reportedToBan) {
        int a = ifBanned(userID, reportedToBan);
        try {
            con = ConnectDB();
            if (a == 0) {
                String sql = "update companiesList set banned" + userID + "=1 where SYMBOL='" + reportedToBan + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
                try (Statement stat = con.createStatement()) {
                    String sql_ = "delete from automationTable where userID=" + userID + " and comSymbol='" + reportedToBan + "'";
                    stat.addBatch(sql_);
                    stat.executeBatch();
                } catch (Exception e) {
                    System.err.println("Inserting into bannedForAll.\n" + e);
                }

            } else {
                String sql = "update companiesList set banned" + userID + "=0 where SYMBOL='" + reportedToBan + "'";
                PreparedStatement st = con.prepareStatement(sql);
                st.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("Problem in setting ban.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }

    }

    /**
     * method that gives the reported companies by an employee
     *
     * @param userID
     * @return list of reported companies
     */
    public static Collection<Company> getNameOfReported(int userID) {
        Collection<Company> names = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select CNAME,SYMBOL,STOCKPRICE,user" + userID + " from companiesList where reported" + userID + "=1";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                names.add(new Company(res.getString("CNAME"), res.getString("SYMBOL"), res.getBigDecimal("STOCKPRICE"), BigDecimal.ZERO, BigDecimal.ZERO, res.getInt("user" + userID)));

            }
            if (names.isEmpty()) {
                names.add(new Company("Nothing", "Nothing", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0));
            }
            return names;
        } catch (Exception e) {
            System.err.println("Problem in getting the reported Companies.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }

        return null;
    }

    /**
     * method that gives the value of the portfolio of an employee
     *
     * @param userID
     * @return total
     */
    public static double getLatestTotal(int userID) {
        double total = 0;
        try {
            con = ConnectDB();
            String sql = "select TOTAL from historyTable where userID=" + userID + " order by TIMESTAMP ASC";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                total = res.getDouble("TOTAL");
            }
            return total;
        } catch (Exception e) {
            System.err.println("Problem in getting the name.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }

        return 0;
    }

    /**
     * method that returns the transactions that an employee did.
     *
     * @param userID
     * @return list of transactions
     */
    public static Collection<Transaction> getTransactions(int userID) {
        Collection<Transaction> trn = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "select * from transactions where userID=" + userID + " order by date DESC";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                String name = getComNameFromSymbol(res.getString("comSymbol"));
                trn.add(new Transaction(name, res.getString("operation"), res.getInt("quantity"), res.getDouble("pricePaid"), res.getString("date")));
            }
            if (trn.isEmpty()) {
                trn.add(new Transaction("Nothing", "No Operation", 0, 0, "Nothing"));
            }
            return trn;
        } catch (Exception e) {
            System.err.println("Problem in getting transactions.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }
        return null;
    }

    /**
     * method that gets name of a company based on the symbol.
     *
     * @param sym
     * @return name of company
     */
    public static String getComNameFromSymbol(String sym) {
        String a = "";
        try {
            con = ConnectDB();
            String sql = "select CNAME from companiesList where SYMBOL='" + sym + "'";
            PreparedStatement stat = con.prepareStatement(sql);

            ResultSet res = stat.executeQuery();
            while (res.next()) {
                a += res.getString("CNAME");
            }
            return a;
        } catch (Exception e) {
            System.err.println("Problem in getting the name.\n" + e);
        } finally {
            try {
                con.close();
            } catch (Exception e) {
                System.err.println("Coudln't close it damn.\n" + e);
            }
        }

        return null;
    }

    /**
     * method that gets all the users info from the database
     *
     * @return a collection of users.
     */
    public static Collection<User> getEmployeesFromDb() {
        Collection<User> users = new ArrayList<>();
        try {
            con = ConnectDB();
            String sql = "SELECT * FROM logins where CLEARANCE = 2 ";
            PreparedStatement get = con.prepareStatement(sql);
            ResultSet result = get.executeQuery();
            while (result.next()) {
                users.add(new User(result.getInt("userID"), result.getString("LOGIN"), result.getString("PASSWORD"), result.getInt("CLEARANCE"), result.getBigDecimal("BUDGET")));
            }
            if (users.isEmpty()) {
                users.add(new User(0, "No Emplyees Yet. :(", "", 2, BigDecimal.ZERO));
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

}
