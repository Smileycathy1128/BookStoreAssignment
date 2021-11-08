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
        getCart().add(book);
    }
    
    Book getBook(String title) {
        for(Book book : cart) {
            if(book.getTitle().equals(title)) {
                return book;
            }
        }
        System.err.println("Cannot getBook: Cannot find book with ISBN "+ title);
        return null;
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
    protected static void addAccount(UserAccount acc) {
        String u = acc.getUsername();
        String p = acc.getPassword();
        String tempString = "insert into "+Main.userAccountTableName
            +" (\"username\",\"password\") values(\""+ u +"\",\""+ p +"\");";
        DaoFactory.executeStatement(tempString);
        System.out.println("Account created");
        Main.userAccountArrayList.add(acc);
    }
    
}
