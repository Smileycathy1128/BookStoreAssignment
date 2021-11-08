import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Main {
    // ==========================================================
    protected static String bookTableName = "library";
    protected static String userAccountTableName = "UserAccount";
    protected static String[] allCategories = { 
        "tech",
        "literature",
        "sci-fi",
        "non-fiction",
        "history",
        "fantasy",
        "comedy",
        "romance",
        "historical fiction",
        "comic",
        "action",
        "horror"
    };
    // ==========================================================
    protected static ArrayList<Book> bookArrayList;
    protected static ArrayList<UserAccount> userAccountArrayList;
    
    public static void main(String[] args) {
        sql2ArrayListAccounts();
        sql2ArrayListBooks();
        Options.welcome();
        System.out.println("Bye bye!");
        DaoFactory.closeScanner(); 
        // if you close it earlier, you'll get a NoSuchElementException
        // Use it for testing
    }
    
    static ArrayList<Book> sql2ArrayListBooks() { // refreshes the bookArrayList
        bookArrayList = new ArrayList<Book>();
        ResultSet resultSet = DaoFactory.getResultSet("select * from \""+Main.bookTableName+"\";");
        try {
            while (resultSet.next()) {
                Book temp2 = new Book(
                    resultSet.getString(1), 
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4).split(", ")
                );
                bookArrayList.add(temp2);
            }
            DaoFactory.closeResultSet();
        } catch (SQLException e) {
            System.err.println("in Main.sql2ArrayListBooks(): -----------------------");
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
            System.err.println("in Main.sql2ArrayListAccounts(): --------------------");
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
        int count = 0;
        System.out.println("------------------------------------------------------------");
        System.out.println("ISBN\t\tTitle\t\t\tAuthor");
        System.out.println("------------------------------------------------------------");
        for(Book book : books) {
            System.out.println("["+(++count)+"] "+book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
        }
        System.out.println("------------------------------------------------------------");
    }
    
    static ArrayList<Book> getAndPrintBookListOfCategory(String category) {
        ArrayList<Book> list = new ArrayList<Book>();
        System.out.println("============================================================");
        System.out.println("Category: "+ category);
        // System.out.println("------------------------------------------------------------");
        // System.out.println("ISBN\t\tTitle\t\t\tAuthor");
        // System.out.println("------------------------------------------------------------");
        for(Book book : bookArrayList) {
            for(String s : book.getCategories()) {
                if(s.equals(category)) {
                    list.add(book);
                    // System.out.println("["+(++count)+"] "+book.getISBN()+"\t"+book.getTitle()+"\t"+book.getAuthor());
                }
                break; // Probably not necessary
            }
        }
        printBookList(list);
        return list;
    }
    
    // TODO: separate these into two methods pickBook and buyBook. Use with Options.cartOptions
    static Boolean pickingBook2Check(UserAccount userAcc , ArrayList<Book> arr) { // Don't put the loop here. Put it somewhere else.
        int tempInt;
        System.out.print("Choose a book to check (Enter 0 to go back): "); // method called after options are already printed
        try {
            tempInt = DaoFactory.getScanner().nextInt();
        } catch (InputMismatchException e) {
            System.out.println("in Main.pickingBook2Check: --------------------------");
            e.printStackTrace();
            System.out.println("-----------------------------------------------------");
            System.out.println("Not a number. Try again.");
            return true;
        } 
        catch (NoSuchElementException e2) {
            e2.printStackTrace();
            System.out.println("[Entering 0...]");
            tempInt = 0;
        }
        switch(tempInt) {
            case 0:
                System.out.println("Going back to categories...");
                return false;
            default:
                try {
                    Book tempBook = arr.get(tempInt-1); // tempInt-1 because the exit option is squeezed into 0
                    ask2Purchase( userAcc, tempBook);
                    return true;
                }
                catch (IndexOutOfBoundsException e) {
                    System.out.println("in Main.pickingBook2Check: --------------------------");
                    e.printStackTrace();
                    System.out.println("-----------------------------------------------------");
                    System.out.println("Invalid number");
                    return true;
                }

        }

    }
    static void ask2Purchase(UserAccount userAcc, Book book) {
        System.out.println("-----------------------------------");
        System.out.println("Title: "+ book.getTitle());
        System.out.println("Author: "+ book.getAuthor());
        System.out.println("ISBN: "+ book.getISBN());
        System.out.println("-----------------------------------");
        System.out.println("Press Enter to continue");
        try {
            DaoFactory.getScanner().nextLine();
        } 
        catch (NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("[auto-advancing...]");
        }
        boolean tempBool;
        String purchaseStr;
        do {
            System.out.println("Purchase \""+ book.getTitle() +"\"? (y/n)");
            try {
                purchaseStr = DaoFactory.getScanner().nextLine();
            } 
            catch (NoSuchElementException e) {
                e.printStackTrace();
                System.out.println("[Automatically selecting n]");
                purchaseStr = "n";
            }
            switch (purchaseStr) {
                case "y":
                    userAcc.addToCart(book);
                    System.out.println("Book added to cart");
                    System.out.print("returning to book selection...");
                    try {
                        DaoFactory.getScanner().next();
                    } 
                    catch (NoSuchElementException e) {
                    System.out.println("[Automatically advancing...]");
                    }
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
    private String[] categories;
    
    public Book(String isbn, String title, String author, String[] categories) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.categories = categories;
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
    public String[] getCategories() {
        return categories;
    }
}
