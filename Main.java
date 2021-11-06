import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    protected static String bookTableName = "library";
    protected static String userAccountTableName = "UserAccount";
    protected static String[] allCategories = { // TODO: add category names here:
        "tech",
        "literature",
        "non-fiction"
    };
    protected static ArrayList<Book> bookArrayList;
    protected static ArrayList<UserAccount> userAccountArrayList;
    
    public static void main(String[] args) {
        sql2ArrayListAccounts();
        sql2ArrayListBooks();
        Options.welcome();
        System.out.println("Bye bye!");
    }
    
    static ArrayList<Book> sql2ArrayListBooks() { // refreshes the bookArrayList
        bookArrayList = new ArrayList<Book>();
        ResultSet resultSet = DaoFactory.getResultSet("select * from \""+Main.bookTableName+"\";");
        try {
            while (resultSet.next()) { // TODO: this returns false
                Book temp2 = new Book();
                temp2.setISBN(resultSet.getString(1));
                temp2.setTitle(resultSet.getString(2));
                temp2.setAuthor(resultSet.getString(3));
                temp2.categories = resultSet.getString(4).split(", ");
                bookArrayList.add(temp2);
            }
            DaoFactory.closeResultSet();
        } catch (SQLException e) {
            System.err.print("Error in Main.sql2ArrayListBooks(): ");
            System.out.println("-----------------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
        if(bookArrayList.size()>0) {
            return bookArrayList;
        }
        else {
            System.err.println("[No books are in the ArrayList]");
            return null;
        }
        
        
    }
    static ArrayList<UserAccount> sql2ArrayListAccounts() {
        userAccountArrayList = new ArrayList<UserAccount>();
        ResultSet resultSet = DaoFactory.getResultSet("select * from \""+Main.userAccountTableName+"\";");
        try {
            while (resultSet.next()) {
                UserAccount temp = new UserAccount();
                temp.setUsername(resultSet.getString(1));
                temp.setPassword(resultSet.getString(2));
                userAccountArrayList.add(temp);
            }
            DaoFactory.closeResultSet();
        } catch (SQLException e) {
            System.err.print("Error in Main.sql2ArrayListAccounts(): ");
            System.out.println("-----------------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
        }
        if(userAccountArrayList.size()>0) {
            return userAccountArrayList;
        }
        else {
            System.err.println("No user accounts are in the ArrayList.");
            return null;
        }
    }
    
    static void printBookList() {
        for(Book book : bookArrayList) {
            System.out.println(book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
        
    }
    static ArrayList<Book> getAndPrintBookListOfCategory(String category) {
        ArrayList<Book> list = new ArrayList<Book>();
        for(Book book : list) {
            for(String s : book.categories) {
                if(s.equals(category)) {
                    list.add(book);
                }
            }
            System.out.println(book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
        return list;
    }
    static void pickBook2Check(ArrayList<Book> arr) {
        
    }
    void printReceipt(ArrayList<Book> cart) {
        System.out.println("Receipt:");;
        for(Book book : cart) {
            System.out.println(book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
    }
}

class Book {
    private String isbn;
    private String title;
    private String author;
    protected String[] categories;
    
    public Book () {

    }
    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    public String getISBN() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}