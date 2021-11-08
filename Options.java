import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class Options {
    protected static boolean welcomeLoop = true;
    static void welcome() {
        do {
            welcomeLoop = false;
            System.out.println(
                "============\n"+
                "[1] Log in\n"+
                "[2] Register\n"+
                "[3] Exit\n"+
                "-----------"
            );
            
            int num1;
            try {
                num1 = DaoFactory.getScanner().nextInt();
            } catch (InputMismatchException e) {
                System.out.println("in Options.welcome: ---------------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
                System.out.println("Cannot accept strings");
                welcomeLoop = true;
                continue;
            }            
            switch (num1) {
                case 1: welcomeLoop = true;loggingIn();
                    break;
                case 2: welcomeLoop = true; register();
                    break;
                case 3: return;
                    // break;
                default:
                    System.out.println("Invalid number");
                    welcomeLoop = true;
                    break;
            }

        } while (welcomeLoop);

    }
    
    static void loggedIn(UserAccount userAcc) {
        int temp1;
        boolean rememberMe = false; // Haha. I'm funny.
        do {
            System.out.println(
                "=======================\n"+
                "Browsing by categories:\n"+
                "-----------------------"
            );
            System.out.println( // if adding more options, change shift to the int after 'all books'
                "[0] Log out\n"+
                "[1] Check cart\n"+
                "----------------------\n"+
                "[2] all books"
            );
            int shift = 3; 
            int count = shift;
            for(String c : Main.allCategories) {
                System.out.println("["+(count++)+"] "+c);
            }
            System.out.println("----------------------");
            try {
                temp1 = DaoFactory.getScanner().nextInt();
            } catch (InputMismatchException e) {
                System.out.println("in Options.loggedIn: --------------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
                System.out.println("Not a number. Try again.");
                rememberMe = true;
                continue;
            } 
            catch(NoSuchElementException e) {
                e.printStackTrace();
                System.out.println("[automatically setting to 0...]");
                temp1 = 0;
            }
            switch(temp1) {
                case 0:
                    System.out.println("Logging out...");
                    rememberMe = false;
                    break;
                case 1:
                    cartOptions(userAcc, userAcc.getCart());
                    rememberMe = true;
                    break;
                case 2:
                    Boolean temp4;
                    do{
                        temp4 = false;
                        Main.printBookList(shift);
                        temp4 = Main.pickingBook2Check(userAcc, Main.bookArrayList, true, 2);                        
                    } while(temp4);
                    rememberMe = true;
                    break;
                default:
                    try {
                        Boolean temp3;
                        do {
                            temp3 = false;
                            // remember temp1 is shifted because of all books, log out, and check cart
                            ArrayList<Book> temp2 = Main.getAndPrintBookListOfCategory(Main.allCategories[temp1-shift], shift);
                            temp3 = Main.pickingBook2Check(userAcc, temp2, false, 1);
                        } while (temp3);
                                                    
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("in Options.loggedIn: --------------------------------");
                        e.printStackTrace();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Invalid number. Try again.");
                        rememberMe = true;
                    }
                    rememberMe = true;
                    break;
            }
            // DaoFactory.closeScanner();
        } while (rememberMe);
    }
    
    static void loggingIn() {
        System.out.print("Username: ");
        String username = DaoFactory.getScanner().next();
        UserAccount temp = UserAccountManager.findAccount(username);
        if(temp==null) {
            System.out.println("Username not found");
        }
        else {
            System.out.print("Password (not hidden): ");
            String password = DaoFactory.getScanner().next();
            if(temp.getPassword().equals(password)) {
                System.out.println("logging in...");
                // DaoFactory.closeScanner();
                loggedIn(temp);
            }
        }

    }
    
    static void register() {
        boolean registerLoop;
        do {
            registerLoop = false;
            UserAccount userAcc = new UserAccount();
            System.out.print("Create a username: ");
            String username = DaoFactory.getScanner().next();
            if(UserAccountManager.findAccount(username)==null) {
                System.out.println("Username is available!");
                System.out.print("Create a password (not hidden): ");
                String password = DaoFactory.getScanner().next();
                userAcc.setUsername(username);
                userAcc.setPassword(password);
                UserAccountManager.addAccount(userAcc);
            }
            else {
                System.out.println("Username is already taken");
                registerLoop = true;
            }
        } while (registerLoop);
    }
    
    static void cartOptions(UserAccount userAcc, ArrayList<Book> cart) {
        boolean loop1;
        int shift = 2;
        // int tempInt;
        do {
            loop1 = false;

            System.out.println("====================================");
            System.out.println("Your Cart");
            System.out.println("------------------------------------");
            Main.printBookList(cart, shift);
            System.out.println("Select an item, press 0 to cancel, press 1 to print receipt.");
            // try {
            //     tempInt = DaoFactory.getScanner().nextInt();
            //     loop = false;
            // } catch (InputMismatchException e) {
            //     System.out.println("This put only accepts numbers. Try again.");
            //     loop = true;
            // }
            int tempInt;
            try {
                tempInt = DaoFactory.getScanner().nextInt();
                switch (tempInt) {
                    case 0:
                        loop1 = false;
                        break;
                    case 1:
                        Main.printBookList(cart, 1);
                        break;      
                    default:
                        loop1 = Main.pickingBook2Check(userAcc, cart, tempInt, true, shift);
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("must enter a number");
                loop1 = true;
                continue;
            }
            

            
           
        } while (loop1);
        
        // TODO: check other todo
    }
    
}
