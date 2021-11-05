import java.sql.ResultSet;
import java.sql.SQLException;
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
        try {
            // DaoFactory.getPreparedStatement("select * from \"?\";").setString(1, Main.bookTableName);
            // ResultSet rs = DaoFactory.getPreparedStatement().executeQuery();
            ResultSet rs = DaoFactory.getResultSet("select * from "+Main.bookTableName+";");
            while(rs.next()) {
                if(rs.getString(1).equals(username)) {
                    UserAccount acc = new UserAccount();
                    acc.setUsername(rs.getString(1));
                    acc.setPassword(rs.getString(2));
                    return acc;
                }
            }
        } catch (SQLException e) {
            System.out.print("SQLException in UserAccountManager.findAccount: ");
            System.out.println("-----------------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
        return null;
    }
    protected static void addAccount(UserAccount acc) {
            DaoFactory.getPreparedStatement("insert into "+Main.userAccountTableName+" (\"username\",\"password\") values(\"?\",\"?\");");
        try {
            // DaoFactory.getPreparedStatement().setString(1, Main.userAccountTableName);
            DaoFactory.getPreparedStatement().setString(1, acc.getUsername());
            DaoFactory.getPreparedStatement().setString(2, acc.getPassword());
            DaoFactory.getPreparedStatement().executeQuery();
            System.out.println("Account created");
        } catch (SQLException e) {
            System.out.print("SQLException in UserAccountManager.addAccount: ");
            System.out.println("-----------------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
    }
}
