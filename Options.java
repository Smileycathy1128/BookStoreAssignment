import java.util.ArrayList;
import java.util.InputMismatchException;

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
                System.out.println("-----------------------------------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
                System.out.println("Cannot accept strings");
                welcomeLoop = true;
                continue;
            }            
            switch (num1) {
                case 1: loggingIn(); welcomeLoop = true;
                    break;
                case 2: register(); welcomeLoop = true;
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
                "-----------------------\n"
            );
            System.out.println( // if adding more options, add to int shift
                "[0] Log out\n"+
                "[1] Check cart\n"+
                "----------------------\n"+
                "[2] all books"
            );            
            // System.out.println(
            //     "[1] all\n"+
            //     "[2] tech\n"
            // );
            int shift = 2; 
            int count = shift;
            for(String c : Main.allCategories) {
                System.out.println("["+(++count)+"] "+c);
            }
            System.out.println("----------------------");
            try {
                temp1 = DaoFactory.getScanner().nextInt();
            } catch (InputMismatchException e) {
                System.out.print("InputMismatchException in Options.loggedIn: ");
                System.out.println("-----------------------------------");
                e.printStackTrace();
                System.out.println("-----------------------------------------------------");
                System.out.println("Not a number. Try again.");
                rememberMe = true;
                continue;
            } 
            switch(temp1) {
                case 0:
                    System.out.println("Logging out...");
                    break;
                case 1:
                    Main.printBookList(userAcc.getCart());
                    break;
                case 2: Main.printBookList();
                    break;
                default:
                    try {
                        Boolean temp3;
                        do {
                            temp3 = false;
                            // remember temp1 is shifted because of all books, log out, and check cart
                            ArrayList<Book> temp2 = Main.getAndPrintBookListOfCategory(Main.allCategories[temp1-shift]);
                            temp3 = Main.pickingBook2Check(userAcc, temp2);
                        } while (temp3);
                                                    
                    } catch (IndexOutOfBoundsException e) {
                        System.out.print("IndexOutOfBoundsException: ");
                        System.out.println("-----------------------------------");
                        e.printStackTrace();
                        System.out.println("-----------------------------------------------------");
                        System.out.println("Invalid number. Try again.");
                        rememberMe = true;
                    }
                    break;
            }
            
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
                // System.out.println("username is available");
                System.out.print("Create a password: ");
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
    
}
