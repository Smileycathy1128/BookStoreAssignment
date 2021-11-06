import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;

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
            while (resultSet.next()) {
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
        printBookList(bookArrayList); 
    }
    static void printBookList(ArrayList<Book> books) {
        for(Book book : books) {
            System.out.println(book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
    }
    
    static ArrayList<Book> getAndPrintBookListOfCategory(String category) {
        ArrayList<Book> list = new ArrayList<Book>();
        int count = 0;
        for(Book book : list) {
            for(String s : book.categories) {
                if(s.equals(category)) { // TODO: check what's wrong with this
                    list.add(book);
                }
            }
            System.out.println("["+(++count)+"] "+book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
        return list;
    }
    static Boolean pickingBook2Check(UserAccount userAcc , ArrayList<Book> arr) { // Don't put the loop here. Put it somewhere else.
        int tempInt;
        System.out.print("Choose a book to check (Enter 0 to go back): "); // method called after options are already printed
        try {
            tempInt = DaoFactory.getScanner().nextInt();
            DaoFactory.closeScanner();
        } catch (InputMismatchException e) {
            System.out.print("InputMismatchException in Main.pickingBook2Check: ");
            System.out.println("-----------------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
            System.out.println("Not a number. Try again.");
            return true;
        }
        switch(tempInt) {
            case 0:
                System.out.println("Going back to categories...");
                return false;
            default:
                try {
                    Book tempBook = arr.get(tempInt-1); // tempInt-1 because the exit option is squeezed into 0
                    ask2Purchase( userAcc, tempBook);
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("-----------------------------------");
                    e.printStackTrace();
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Invalid number");
                    return true;
                }
                break;
        }
        return false;

    }
    static void ask2Purchase(UserAccount userAcc, Book book) {
        System.out.println("-----------------------------------");
        System.out.println("Title: "+ book.getTitle());
        System.out.println("Author: "+ book.getAuthor());
        System.out.println("ISBN: "+ book.getISBN());
        System.out.println("-----------------------------------");
        System.out.print("Press Enter to continue");
        DaoFactory.getScanner().next();
        boolean tempBool;
        do {
            System.out.println("Purchase \""+ book.getTitle() +"\"? (y/n)");
            switch (DaoFactory.getScanner().nextLine()) {
                case "y":
                    userAcc.addToCart(book);
                    System.out.println("Book added to cart");
                    tempBool = false;
                    break;
                case "n":
                    System.out.println("going back to list...");
                    tempBool = false;
                    break;
                default:
                    System.out.println("Invalid input. y/n");
                    tempBool = true;
                    break;
            }
        } while (tempBool);


    }
    static void printReceipt(ArrayList<Book> cart) {
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