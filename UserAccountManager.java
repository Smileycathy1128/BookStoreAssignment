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
    
    void removeBook(Book book) {
        if(cart.contains(book)) {
            cart.remove(book);
            System.out.println(
                "\""+ book.getTitle() +"\" (ISBN: "+ book.getISBN()
                +") is removed from cart"
            );
            return;
        }
        else {
            System.err.println("Cannot removeBook. title: "+book.getTitle());
        }
    }
}

public class UserAccountManager {
    protected static UserAccount findAccount(String username) {
        try {
            DaoFactory.getPreparedStatement("select * from \"?\";").setString(1, Main.bookTableName);
            ResultSet rs = DaoFactory.getPreparedStatement().executeQuery();
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
            e.printStackTrace();
        }
        return null;
    }
    protected static void addAccount(UserAccount acc) {
            DaoFactory.getPreparedStatement("insert into \"?\" (\"username\",\"password\") values(\"?\",\"?\");");
        try {
            DaoFactory.getPreparedStatement().setString(1, Main.userAccountTableName);
            DaoFactory.getPreparedStatement().setString(2, acc.getUsername());
            DaoFactory.getPreparedStatement().setString(3, acc.getPassword());
            DaoFactory.getPreparedStatement().executeQuery();
            
            System.out.println("Account created");
        } catch (SQLException e) {
            System.out.print("SQLException in UserAccountManager.addAccount: ");
            e.printStackTrace();
        }
    }
}
