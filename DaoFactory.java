import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DaoFactory { // creates, manages, and makes sure there is just one of each DAO
    // =================================================================
    private static String connectionString = "jdbc:sqlite:BookStore.db";
    // =================================================================
    
    static Connection connect = null;
    static Statement statement = null;
    static PreparedStatement prep = null;
    static ResultSet rs = null;
    static Scanner scan = null;
    
    private static Connection getConnection(){
            // try {
            //     ResourceBundle reBundle = ResourceBundle.getBundle(
            //         "src/CathyNguyenProject0/dbConfig" // unused if commented out
            //     ); // At this point,what's the point?
            //     String dbString = reBundle.getString("DatabaseLocation");
            //     connect = DriverManager.getConnection(dbString);
            // } catch (SQLException sqlE) {
            //     sqlE.printStackTrace();
            // } catch (Exception e1) {
            //     System.out.println("Check database config file");
            //     e1.printStackTrace();
        if(connect == null) {
            try {
                connect = DriverManager.getConnection(connectionString);
            } catch (SQLException e2) {
                System.out.println("[Unable to get Connection]: ---------------------------");
                e2.printStackTrace();
                System.out.println("-------------------------------------------------------");
            }
            
        }
        return connect;
    }
    private static void closeConnection() {
        if(connect == null) {
            System.out.println("[Cannot close a Connection that is null]");
        }
        else {
            try {
                connect.close();
            } catch (SQLException e) {
                System.out.println("[Cannot closeConnection]: -----------------------");
                e.printStackTrace();
                System.out.println("-------------------------------------------------");
            }
            connect = null;
        }
    }
    
    private static Statement getStatement() {
        if(statement == null) {
            try {
                statement = getConnection().createStatement();
            } catch (SQLException e) {
                System.out.println("[Cannot createStatement]: ---------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
        }
        return statement;
    }
    private static void closeStatement() {
        if(statement == null) {
            System.out.println("[Cannot close a Statement that is null]");
        }
        else {
            try {
                statement.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("[Cannot closeStatement]: ----------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
            statement = null;
        }
    }
    
    static PreparedStatement getPreparedStatement() {
        if(prep == null) {
            System.out.println("[getPrepareStatement should pass a string]");
            System.exit(0);
        }
        return prep;
    }
    static PreparedStatement getPreparedStatement(String str) {
        if(prep == null) {
            try {
                prep = getConnection().prepareStatement(str);
            } catch (SQLException e) {
                System.out.println("[Cannot create PrepareStatement]: -------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
        }
        else {
            closePreparedStatement();
            prep = getPreparedStatement(str);
        }
        return prep;
    }
    static void closePreparedStatement() {
        if(prep == null) {
            System.out.println("[Cannot close a PreparedStatement that is null]");
        }
        else {
            try {
                prep.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println("[Cannot closePreparedStatement]: --------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
            prep = null;
        }
    }
    
    static ResultSet getResultSet(String sqlString) {
        if(rs == null) {
            try {
                rs = getStatement().executeQuery(sqlString);   
            } catch (SQLException e) {
                System.out.println("[Cannot get ResultSet]: --------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
        }
        return rs;
    }
    static void closeResultSet() {
        if(rs == null) {
            System.out.println("[Cannot close a ResultSet that is null]");
        }
        else {
            try {
                rs.close();
                closeStatement();
            } catch (SQLException e) {
                System.out.println("[Cannot closeResultSet]: -----------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
            rs = null;
        }
    }
    static void executeStatement(String str) { // use this if you are not expecting a ResultSet
        try {
            getStatement().execute(str);
        } catch (SQLException e) {
            System.out.println("[Cannot executeStatement]: --------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
    }
    static Scanner getScanner() {
        while(scan == null) {
            scan = new Scanner(System.in);
        }
        return scan;
    }
    static void closeScanner() { // closing the scanner causes issues. Save 'til the end?
        if(scan == null) {
            System.out.println("[Cannot close a Scanner that is null]");
        }
        else {
            scan.close();
            scan = null;
        }
    }
    
}
