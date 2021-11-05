import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DaoFactory { // creates, manages, and makes sure there is just one of each DAO
    static Connection connect = null;
    static Statement statement = null;
    static PreparedStatement prep = null;
    static ResultSet rs = null;
    static Scanner scan = null;
    
    private static Connection getConnection(){
        if(connect == null) {
            try {
                connect = DriverManager.getConnection("jdbc:sqlite:BookStore.db");
            } catch (SQLException e2) {
                System.out.print("[Unable to get Connection]: ");
                System.out.println("-----------------------------------");
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
                System.out.print("[Cannot closeConnection]: ");
                System.out.println("-----------------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
            connect = null;
        }
    }
    
    private static Statement getStatement() {
        if(statement == null) {
            try {
                statement = getConnection().createStatement();
            } catch (SQLException e) {
                System.out.print("[Cannot createStatement]: ");
                System.out.println("-----------------------------------");
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
                System.out.print("[Cannot closeStatement]: ");
                System.out.println("-----------------------------------");
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
                System.out.print("[Cannot create PrepareStatement]: ");
                System.out.println("-----------------------------------");
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
                System.out.print("[Cannot closePreparedStatement]: ");
                System.out.println("-----------------------------------");
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
                System.out.println("This error again! :D");
                System.out.print("[Cannot create ResultSet]: ");
                System.out.println("-----------------------------------");
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
                System.out.println("-----------------------------------");
                System.out.print("[Cannot closeResultSet]: ");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
            }
            rs = null;
        }
    }
    
    static Scanner getScanner() {
        if(scan == null) {
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