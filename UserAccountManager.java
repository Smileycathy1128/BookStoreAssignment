import java.sql.ResultSet;
import java.util.ArrayList;

class UserAccount {
    String username;
    String password;
    ArrayList<Book> cart;
    
    String getUsername() {
        return username;
    }
    String getPassword() {
        return password;
    }
    
    void setUsername(String username) {
        this.username = username;
    }
    void setPassword(String password) {
        this.password = password;
    }
    
    ArrayList<Book> getCart() {
        if(cart==null) {
            cart = new ArrayList<Book>();
        }
        return cart;
    }
    void addToCart(Book book) {
        cart.add(book);
    }
    
    Book getBook(String isbn) {
        for(Book book : cart) {
            if(book.getTitle().equals(isbn)) {
                return book;
            }
        }
        System.err.println("Cannot getBook: Cannot find book with ISBN "+ isbn);
        return new Book();
    }
    
    void removeFromCart(Book book) {
        if(cart.contains(book)) {
            cart.remove(book);
            System.out.println(
                "\""+ book.getTitle() +"\" (ISBN: "+ book.getISBN()
                +") is removed from cart"
            );
            return;
        }
        else {
            System.err.println("Cannot removeFromCart. title: "+book.getTitle());
        }
    }
}

public class UserAccountManager {
    protected static UserAccount findAccount(String username) {
        for(UserAccount u : Main.userAccountArrayList) {
            if(u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    // protected static UserAccount findAccount(String username) {
    //     try {
    //         // DaoFactory.getPreparedStatement("select * from \"?\";").setString(1, Main.bookTableName);
    //         // ResultSet rs = DaoFactory.getPreparedStatement().executeQuery();
    //         ResultSet rs = DaoFactory.getResultSet("select * from "+Main.bookTableName+";");
    //         while(rs.next()) {
    //             System.out.println(rs.getString(1)); // for testing
    //             if(rs.getString(1).equals(username)) {
    //                 UserAccount acc = new UserAccount();
    //                 acc.setUsername(rs.getString(1));
    //                 acc.setPassword(rs.getString(2));
    //                 return acc;
    //             }
    //         }
    //     } catch (SQLException e) {
    //         System.out.print("SQLException in UserAccountManager.findAccount: ");
    //         System.out.println("-----------------------------------");
    //         e.printStackTrace();
    //         System.out.println("-----------------------------------------------------");
    //     }
    //     return null;
    // }
    protected static void addAccount(UserAccount acc) {
        String u = acc.getUsername();
        String p = acc.getPassword();
        String tempString = "insert into "+Main.userAccountTableName
            +" (\"username\",\"password\") values(\""+ u +"\",\""+ p +"\");";
        System.out.println(tempString);
        ResultSet rs = DaoFactory.getResultSet(tempString);
        if(rs==null) {
            System.err.println("[ResultSet is null in UserManager.addAccount]");
        }
        else {
            System.out.println("Account created");
            Main.userAccountArrayList.add(acc);
        }
        // DaoFactory.getPreparedStatement("insert into "+Main.userAccountTableName
        //     +" (\"username\",\"password\") values(\""
        //     +u+"\",\""+p+"\");");
        // try {
            // DaoFactory.getPreparedStatement().setString(1, Main.userAccountTableName);
            // DaoFactory.getPreparedStatement().setString(1, u);
            // DaoFactory.getPreparedStatement().setString(2, p);
            // DaoFactory.getPreparedStatement().executeQuery();
            // System.out.println("Account created");
        // } catch (SQLException e) {
        //     System.out.print("SQLException in UserAccountManager.addAccount: ");
        //     System.out.println("-----------------------------------");
        //     e.printStackTrace();
        //     System.out.println("-----------------------------------------------------");
        // }
        DaoFactory.closeResultSet();
    }
}
