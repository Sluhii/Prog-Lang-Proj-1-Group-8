import java.io.*;
//import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
//import java.lang.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/*
* Mga fre pakilagay nalang ulit yung mga UI hehe
* ----
* Kayo narin bahala kung lalagay nyo sa JOption yung mga keme
* or yung magbubukas ng window para sa program natin ung parang
* software na talaga sya HAHAHAHA eme
* ----
* Di ako sure kung pano ba yung system("cls") at pause dito
* HAHAHAHAHA
* ----
*
* */

//----------------------[GLOBAL VARIABLES]----------------------
class Vars{
    public static final int KEY = 70;
    public static final String LMSFolderDB = ".\\LMS Database";
    public static final String LMSLogs = ".\\LMS Database\\LMS Logs";
    public static final String LMSFlashDrive = "D:\\LMS Details";
    public static final String fill = "NONE";
    public static final String adminFillName = "LibrarianADMIN";
    public static final String adminFillID = "LibrarianADMIN123";
    public static int currentSKey, currentLKey, currentViolation;
    public static String currentStudentID, currentStudentName;
    public static String currentLibrarianID, currentLibrarianName;
}

public class LMS_Project_By_Group_8 {
    private static LinkedList<AccountDetails> accountList = new LinkedList<>();
    private static LinkedList<BookDetails> bookList = new LinkedList<>();
    private static LinkedList<BookDetails> bookRquestList = new LinkedList<>();
    //private static LinkedList<LogDetails> logList = new LinkedList<>();
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        int userNum;

        //create directory for the Database and Logs
        File directoryDB = new File(Vars.LMSFolderDB);
        File directoryLogs = new File(Vars.LMSLogs);
        directoryDB.mkdir();
        directoryLogs.mkdir();

        //Create instance of class to call non-static methods from main
        LMS_Project_By_Group_8 obj = new LMS_Project_By_Group_8();

        switch (obj.insertcard()){
            case 1: //system("cls");
                    obj.registerStudent();
                    saveAccountFD();
                    saveAccounts();
                    logs("IN", Vars.fill);
                    break;
            case 2: //system("cls");
                    obj.registerLibrarian();
                    saveAccountFD();
                    saveAccounts();
                    logs("IN", Vars.fill);
                    break;
            case 3: //system("cls");
                    exitMessage();
                    System.exit(0);
                    break;
            case 4: //system("cls");
                    userLOGIN();
                    break;
            default: //system("cls");
                     System.out.println("INVALID INPUT.");
                     //system("pause");
                     //system("cls");
                     break;
        }

        obj.retrieveBooks();
        //system("cls");

        if (Vars.currentSKey == 1 && Vars.currentLKey == 0){ //student
            while (true){
                //system("cls");
                switch (menu(1)){
                    case 1: //system("cls");
                            displayBooks();
                            //system("pause"); '
                            break;
                    case 2: //system("cls");
                            displayBooks();
                            borrowBook(prompts(1));
                            break;
                    case 3: //system("cls");
                            obj.returnBook();
                            //text here
                            break;
                    case 4: //system("cls");
                            obj.requestBook();
                            //text here
                            break;
                    case 5: //system("cls");
                            viewViolation();
                            //text here
                            break;
                    case 6: //system("cls");
                            exitMessage();
                            logs("OUT", Vars.fill);
                            break;
                    default: //system("cls");
                             System.out.println("INVALID INPUT.");
                             //system("pause");
                             //system("cls");
                             break;
                }
            }
        }else if (Vars.currentSKey == 0 && Vars.currentLKey == 1){ //librarian
            while (true){
                //system("cls");
                switch (menu(2)){
                    case 1: //system("cls");
                            displayBooks();
                            userNum = prompts(2);
                            if (userNum == 1){
                                insertExistingBook();
                            }else if(userNum == 2){
                                obj.insertNewBook();
                            }else{
                                System.out.printf("%n%nCANCELLED%n");
                                //system("pause");
                            }
                            break;
                    case 2: //system("cls");
                            displayBooks();
                            editBooks();
                            break;
                    case 3: //system("cls");
                            displayBooks();
                            userNum = prompts(3);
                            if (userNum == 1){
                                removeAcopy();
                            }else if (userNum == 2){
                                removeBooks();
                            }else{
                                System.out.printf("%n%nCANCELLED%n");
                                //system("pause");
                            }
                            break;
                    case 4: //system("cls");
                            obj.bookRequests();
                            break;
                    case 5: //system("cls");
                            displayBooks();
                            break;
                    case 6: //system("cls");
                            displayLogs();
                            break;
                    case 7: //system("cls");
                            exitMessage();
                            logs("OUT", Vars.fill);
                            break;
                    default: //system("cls");
                            System.out.println("INVALID INPUT.");
                            //system("pause");
                            //system("cls");
                            break;
                }
            }
        }
    }

    //----------------------[LinkedList Structures]----------------------

    class AccountDetails {
        private  int skey, lkey, violation;
        private String studentID, studentName;
        private String librarianID, librarianName;

        //public method to add accounts
        public AccountDetails (String studentID, String librarianID,
                               String studentName, String librarianName,
                              int skey, int lkey, int violation){
            this.studentID = studentID;
            this.librarianID = librarianID;
            this.studentName = studentName;
            this.librarianName = librarianName;
            this.skey = skey;
            this.lkey = lkey;
            this.violation = violation;
        }

        //public methods to access and display the accounts
        public String getStudentID() {
            return studentID;
        }
        public String getLibrarianID() {
            return librarianID;
        }
        public String getStudentName() {
            return studentName;
        }
        public String getLibrarianName() {
            return librarianName;
        }
        public int getSkey() {
            return skey;
        }
        public int getLkey() {
            return lkey;
        }
        public int getViolation() {
            return violation;
        }

        //public methods to edit the violation of the account
        public void setViolation(int violation){
            this.violation = violation;
        }
    }

    class BookDetails {
        private int bookNum, publicationYear, bookQuantity;
        private String bookTitle, bookAuthor, ISBN;

        //public method to add books
        public BookDetails (int bookNum, String ISBN, String bookTitle,
                            String bookAuthor, int publicationYear, int bookQuantity){
            this.bookNum = bookNum;
            this.ISBN = ISBN;
            this.bookTitle = bookTitle;
            this.bookAuthor = bookAuthor;
            this.publicationYear = publicationYear;
            this.bookQuantity = bookQuantity;
        }

        //public methods to access and display the books
        public int getBookNum() {
            return bookNum;
        }
        public String getISBN() {
            return ISBN;
        }
        public String getBookTitle() {
            return bookTitle;
        }
        public String getBookAuthor() {
            return bookAuthor;
        }
        public int getPublicationYear() {
            return publicationYear;
        }
        public int getBookQuantity() {
            return bookQuantity;
        }

        //public methods to edit the details of the book
        public void setBookNum(int bookNum) {
            this.bookNum = bookNum;
        }
        public void setBookISBN(String ISBN) {
            this.ISBN = ISBN;
        }
        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }
        public void setBookAuthor(String bookAuthor) {
            this.bookAuthor = bookAuthor;
        }
        public void setBookPubYear(int pubyear) {
            this.publicationYear = pubyear;
        }
        public void setBookQuantity(int bookQuantity) {
            this.bookQuantity = bookQuantity;
        }
    }

    class LogDetails {
        private String date, time, name, STATUS, ITEM;

        //public method to add Logs
        public LogDetails (String date, String time, String name, String STATUS, String ITEM){
            this.date = date;
            this.time = time;
            this.name = name;
            this.STATUS = STATUS;
            this.ITEM = ITEM;
        }

        //public methods to access and display the logs
        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
        public String getName() {
            return name;
        }
        public String getSTATUS() {
            return STATUS;
        }
        public String getITEM() {
            return ITEM;
        }
    }

    //----------------------[LOG IN]----------------------
    public int insertcard() {
        int userNum;
        boolean isValid = false;

        //check if card is inserted
        File fp = new File("D:\\LMS_Check.txt");
        while (!fp.exists()) {
            try {
                fp.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loading();
        }
        File directoryFD = new File(Vars.LMSFlashDrive);
        directoryFD.mkdir();
        front();

        fp = new File(Vars.LMSFlashDrive + "\\userDetails.csv");
        if (fp.exists()) {
            retrieveAccounts();
            scanScreen(1);
            return 4;
        } else {
            retrieveAccounts();
            scanScreen(2);
            while (!isValid) {
                System.out.println("NEW USER");
                System.out.println("CHOOSE CATEGORY: ");
                System.out.printf("%n[1] STUDENT");
                System.out.printf("%n[2] LIBRARIAN");
                System.out.printf("%n[3] CANCEL");
                System.out.printf("%n%n-> ");
                try {
                    userNum = scan.nextInt();
                    if (userNum > 0 && userNum <= 3) {
                        isValid = true;
                        return userNum;
                    } else {
                        System.out.println("INVALID INPUT.");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                } catch (InputMismatchException e) {
                    e.printStackTrace();
                    System.out.println("INVALID INPUT.");
                    System.out.println("PLEASE TRY AGAIN");
                }
            }
            return 0;
        }
    }

    public void retrieveAccounts() {
        //retrieve accounts in database
        File fp = new File(Vars.LMSFolderDB + "\\Accounts.csv");
        if(!fp.exists()){
            try{
                fp.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            try{
                FileReader f2p = new FileReader(fp);
                BufferedReader bf2p = new BufferedReader(f2p);
                String str;

                while((str = bf2p.readLine()) != null){
                    String[] tokens = str.split(",");

                    String studentID = tokens[0];
                    String librarianID = tokens[1];
                    String studentName = tokens[2];
                    String librarianName = tokens[3];
                    int skey = Integer.parseInt(tokens[4]);
                    int lkey = Integer.parseInt(tokens[5]);
                    int violation = Integer.parseInt(tokens[6]);

                    /*decrypt(1, studentID, null);
                    decrypt(1, librarianID, null);
                    decrypt(1, studentName, null);
                    decrypt(1, librarianName, null);
                    decrypt(2, null, skey);
                    decrypt(2, null, lkey);*/

                    AccountDetails accounts = new AccountDetails(studentID, librarianID, studentName, librarianName, skey, lkey, violation);
                    accountList.add(accounts);
                }
                bf2p.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        //retrieve account details from flashdrive
        fp = new File(Vars.LMSFlashDrive + "\\userDetails.csv");
        if(!fp.exists()){
            System.out.println("OH! NEW USER!");
            try {
                fp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try{
                FileReader f2p = new FileReader(fp);
                BufferedReader bf2p = new BufferedReader(f2p);
                String str;

                while((str = bf2p.readLine()) != null){
                    String[] tokens = str.split(",");

                    Vars.currentStudentID = tokens[0];
                    Vars.currentLibrarianID = tokens[1];
                    Vars.currentStudentName = tokens[2];
                    Vars.currentLibrarianName = tokens[3];
                    Vars.currentSKey = Integer.parseInt(tokens[4]);
                    Vars.currentLKey = Integer.parseInt(tokens[5]);
                    Vars.currentViolation = Integer.parseInt(tokens[6]);

                    /*decrypt(1, Vars.currentStudentID, null);
                    decrypt(1, Vars.currentStudentName, null);
                    decrypt(1, Vars.currentLibrarianID, null);
                    decrypt(1, Vars.currentLibrarianName, null);
                    decrypt(2, null, Vars.currentSKey);
                    decrypt(2, null, Vars.currentLKey);*/
                }
                bf2p.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void retrieveBooks() {
        //retrieve books from Database
        File fp = new File(Vars.LMSFolderDB + "\\Books.txt");
        if(!fp.exists()){
            if(Vars.currentSKey==1 && Vars.currentLKey==0){
                System.out.println("LIBRARY IS CURRENTLY EMPTY");
                System.out.println("NO BOOKS TO BE FOUND :(");
                System.out.println("PLEASE COME BACK LATER. SORRY FOR INCONVENIENCE.");
                //system("pause");
                logs("OUT", Vars.fill);
                //exit(0);
            }else if(Vars.currentSKey==0 && Vars.currentLKey==1){
                System.out.println("LIBRARY IS CURRENTLY EMPTY");
                System.out.println("NO BOOKS TO BE FOUND :(");
                System.out.println("PLEASE ADD BOOKS.");
                //system("pause");
            }else{
                System.out.println("SYSTEM ERROR.");
                //system("pause");
            }
        }else{
            try{
                FileReader f2p = new FileReader(Vars.LMSFolderDB + "\\Books.txt");
                BufferedReader bf2p = new BufferedReader(f2p);
                String str;

                while((str = bf2p.readLine()) != null){
                    String[] tokens = str.split(",");

                    int bookNum = Integer.parseInt(tokens[0]);
                    String ISBN = tokens[1];
                    String bookTitle = tokens[2];
                    String bookAuthor = tokens[3];
                    int publicationYear = Integer.parseInt(tokens[4]);
                    int bookQuant = Integer.parseInt(tokens[5]);

                    /*decrypt(2,"",bLib.bookNum);
                    decrypt(1,bLib.ISBN,0);
                    decrypt(1,bLib.bookTitle,0);
                    decrypt(1,bLib.bookAuthor,0);
                    decrypt(2,"",bLib.publicationYear);
                    decrypt(2,"",bLib.bookQuantity);*/

                    BookDetails book = new BookDetails(bookNum, ISBN, bookTitle, bookAuthor, publicationYear, bookQuant);
                    bookList.add(book);
                }
                bf2p.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void registerStudent() {
        int count = 0;
        boolean isValid = false;

        while(!isValid){
            if(count<3){
                //system("cls");

                System.out.printf("%nAPPLICATION FORM%n%n");
                System.out.println("Enter Student Name: ");
                String studentName = scan.nextLine();
                System.out.println("Enter Student ID: ");
                String studentID = scan.nextLine();

                if(checkAccount(1,studentID)==1){
                    int skey = Vars.currentSKey = 1;
                    int lkey = Vars.currentLKey = 0;
                    String librarianID = new String(Vars.fill);
                    String librarianName = new String(Vars.fill);
                    Vars.currentStudentID = new String(studentID);
                    Vars.currentStudentName = new String(studentName);
                    int violation = Vars.currentViolation = 0;

                    AccountDetails account = new AccountDetails(studentID, studentName, librarianID, librarianName, skey, lkey, violation);
                    accountList.add(account);
                    System.out.println("REGISTRATION SUCCESSFUL");
                    //system("pause");
                    isValid = true;
                }else {
                    System.out.println("INVALID STUDENT ID");
                    System.out.println("ID ALREADY EXIST");
                    System.out.println("PLEASE TRY AGAIN.");
                    //system("pause");
                    count++;
                }
            }else{
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                System.exit(0);
            }
        }
        //system("cls");
    }

    public void registerLibrarian() {
        int count = 0;
        boolean isValid = false;
        while(!isValid){
            if(count<3){
                //system("cls");

                System.out.printf("%nAPPLICATION FORM%n%n");
                System.out.println("ADMINISTRATORS HAVE DEFAULT NAME AND ID");
                System.out.println("PLEASE ENTER THE FOLLOWING INFORMATION TO CONFIRM");
                System.out.println("Enter Librarian Name: ");
                String librarianName = scan.nextLine();
                System.out.println("Enter Librarian ID: ");
                String librarianID = scan.nextLine();

                if(librarianName.compareTo(Vars.adminFillName)==0 &&
                    librarianID.compareTo(Vars.adminFillID)==0){

                    int skey = Vars.currentSKey = 0;
                    int lkey = Vars.currentLKey = 1;
                    String studentID = new String(Vars.fill);
                    String studentName = new String(Vars.fill);
                    Vars.currentLibrarianName = new String(librarianName);
                    Vars.currentLibrarianID = new String(librarianID);
                    int violation = Vars.currentViolation = 0;

                    AccountDetails account = new AccountDetails(studentID, studentName, librarianID, librarianName, skey, lkey, violation);

                    if (checkAccount(2,librarianID) == 1){
                        accountList.add(account);
                    }

                    System.out.println("REGISTRATION SUCCESSFUL");
                    //system("pause");
                    isValid = true;
                }else{
                    System.out.println("INVALID INPUT");
                    System.out.println("CREDENTIALS DO NOT MATCH");
                    System.out.println("PLEASE TRY AGAIN.");
                    //system("pause");
                    count++;
                }
            }else{
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                System.exit(0);
            }
        }
        //system("cls");
    }

    public static void userLOGIN() {
        int count;
        boolean isValid = false;

        if(Vars.currentSKey==1 && Vars.currentLKey==0){
            count = 0;
            while(!isValid){
                if(count<3){
                    //system("cls");
                    System.out.printf("%nLOG IN%n%n");
                    System.out.println("Enter Student ID: ");
                    String studentID = scan.nextLine();

                    if(studentID.compareTo(Vars.currentStudentID) == 0){
                        logs("IN",Vars.fill);
                        isValid = true;
                    }else{
                        System.out.printf("%n%nERROR.%n");
                        System.out.println("WRONG STUDENT ID");
                        System.out.println("PLEASE TRY AGAIN.");
                        //system("pause");
                        count++;
                    }
                }else{
                    System.out.println("ERROR.");
                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                    System.out.println("PLEASE TRY AGAIN LATER");
                    //system("pause");
                    System.exit(0);
                }
            }
        }else if(Vars.currentSKey==0 && Vars.currentLKey==1){
            count = 0;
            while(!isValid){
                if(count<3){
                    //system("cls");
                    System.out.printf("%nLOG IN%n%n");
                    System.out.println("Enter Librarian ID: ");
                    String librarianID = scan.nextLine();

                    if(librarianID.compareTo(Vars.adminFillID) == 0){
                        logs("IN",Vars.fill);
                        isValid = true;
                    }else{
                        System.out.printf("%n%nERROR.%n");
                        System.out.println("WRONG LIBRARIAN ID");
                        System.out.println("PLEASE TRY AGAIN.");
                        //system("pause");
                        count++;
                    }
                }else{
                    System.out.println("ERROR.");
                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                    System.out.println("PLEASE TRY AGAIN LATER");
                    //system("pause");
                    System.exit(0);
                }
            }
        }else{
            //system("cls");
            System.out.println("SYSTEM ERROR.");
            //system("pause");
            //system("cls");
        }
    }



    //----------------------[STUDENT methods]----------------------
    public static void borrowBook(int x) {
        int count, userNum;
        boolean isValid = false;
        boolean isFound = false;

        if (Vars.currentViolation == 0){
            //search the book
            for (BookDetails book : bookList){
                if (book.getBookNum() == x){
                    isFound = true;

                    //get current date and time
                    LocalDate currentDate = LocalDate.now();
                    int currYear = currentDate.getYear();
                    int currMonth = currentDate.getMonthValue();
                    int currDay = currentDate.getDayOfMonth();

                    displayCurrentBook(x);

                    if (verifyAccount(1)){
                        String temp = new String(book.getBookTitle());
                        count = 0;
                        while (!isValid){
                            if (count<3){
                                System.out.println("BOOK COPIES TO BE BORROWED: ");
                                try {
                                    userNum = scan.nextInt();
                                    if (userNum > 0 && userNum < book.bookQuantity){
                                        book.bookQuantity -= userNum;

                                        //save the borrowed book to the flashdrive
                                        File fp = new File(Vars.LMSFlashDrive + "\\borrowedBooks.txt");
                                        do{
                                            try {
                                                fp.createNewFile();
                                            }catch (IOException e){
                                                e.printStackTrace();
                                            }
                                        }while(!fp.exists());

                                        try (BufferedWriter fprint = new BufferedWriter(new FileWriter(fp, true))) {
                                            String borrowedBookDetails = String.format("%d, %d, %d, %d, %s, %s, %s, %d, %d%n",
                                                    currYear, currMonth, currDay, book.getBookNum(), book.getISBN(), book.getBookTitle(),
                                                    book.getBookAuthor(), book.getPublicationYear(), userNum);
                                            fprint.write(borrowedBookDetails);
                                            System.out.println("THE BOOK ( " + temp + " ) SUCCESSFULLY BORROWED.");
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                    }else{
                                        System.out.println("INVALID INPUT");
                                        System.out.println("PLEASE TRY AGAIN");
                                        //system("cls");
                                    }
                                }catch (InputMismatchException e) {
                                    System.out.println("INVALID INPUT.");
                                    System.out.println("PLEASE ENTER A VALID NUMBER.");
                                    scan.nextLine(); // Clear the input buffer
                                    count++;
                                }
                            }else{
                                System.out.println("ERROR.");
                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                System.out.println("PLEASE TRY AGAIN LATER");
                                //system("pause");
                                isValid = true;
                            }
                        }
                    }else{
                        System.out.println("TRANSACTION CANCELLED.");
                        System.out.println("INPUT DO NOT MATCH TO YOUR CREDENTIALS.");
                        System.out.println("PLEASE TRY AGAIN LATER");
                        //system("pause");
                    }
                    break;
                }
            }
            if (!isFound){
                System.out.println("BOOK NUMBER: ( " + x + " ) NOT FOUND.");
            }
        }else {
            System.out.println("TRANSACTION CANCELLED.");
            System.out.println("YOU HAVE A VIOLATION");
            System.out.println("PLEASE SETTLE YOUR VIOLATION FIRST AND TRY AGAIN LATER.");
        }
    }

    public void returnBook() {
        boolean isFound = false;
        boolean isAccFound = false;

        if (verifyAccount(1)){
            File fp = new File(Vars.LMSFlashDrive + "\\borrowedBooks.txt");
            if (fp.exists()){
                //get current time
                LocalDate currentDate = LocalDate.now();
                int currYear = currentDate.getYear();
                int currMonth = currentDate.getMonthValue();
                int currDay = currentDate.getDayOfMonth();

                //read the file
                try {
                    FileReader f2p = new FileReader(fp);
                    BufferedReader bf2p = new BufferedReader(f2p);
                    String str;

                    while ((str = bf2p.readLine()) != null){
                        String[] tokens = str.split(",");

                        int borrowedYear = Integer.parseInt(tokens[0]);
                        int borrowedMonth = Integer.parseInt(tokens[1]);
                        int borrowedDay = Integer.parseInt(tokens[2]);
                        int borrowedBookNum = Integer.parseInt(tokens[3]);
                        String borrowedISBN = tokens[4];
                        String borrowedBookTitle = tokens[5];
                        String borrowedBookAuthor = tokens[6];
                        int borrowedPubYear = Integer.parseInt(tokens[7]);
                        int borrowedBookQuantity = Integer.parseInt(tokens[8]);

                        //check penalty
                        for (AccountDetails account : accountList){
                            if (account.getStudentID().compareTo(Vars.currentStudentID) == 0){
                                isAccFound = true;
                                if (borrowedYear > currYear){
                                    //penalty here
                                    account.setViolation(3);
                                } else if (borrowedMonth > currMonth) {
                                    //penalty here
                                    account.setViolation(2);
                                }else if ((currDay - borrowedDay) >= 3){
                                    //penalty here
                                    account.setViolation(1);
                                }else{
                                    //no penalty
                                    account.setViolation(0);
                                }
                                break;
                            }
                        }
                        if (isAccFound){
                            saveAccounts();
                            saveAccountFD();
                        }else {
                            System.out.println("SYSTEM ERROR");
                            System.out.println("ACCOUNT NOT FOUND IN LIST");
                        }

                        //return the book to the database
                        for (BookDetails book : bookList){
                            if (book.getBookNum() == borrowedBookNum){
                                isFound = true;
                                book.bookQuantity += borrowedBookQuantity;
                                System.out.println("BOOK ( " + borrowedBookTitle + " ) SUCCESSFULLY RETURNED.");
                                break;
                            }
                        }
                        if (!isFound){
                            BookDetails books = new BookDetails(borrowedBookNum, borrowedISBN, borrowedBookTitle,
                                    borrowedBookAuthor, borrowedPubYear, borrowedBookQuantity);
                            bookList.add(books);
                        }
                    }
                    bf2p.close();
                    saveBooks();

                    //remove the borrowedBooks.txt
                    try {
                        Path path = Paths.get(Vars.LMSFlashDrive + "\\borrowedBooks.txt");
                        Files.delete(path);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("NO BOOKS TO BE RETURN.");
                //system("pause");
            }
        }else{
            System.out.println("TRANSACTION CANCELLED.");
            System.out.println("INPUT DO NOT MATCH TO YOUR CREDENTIALS.");
            System.out.println("PLEASE TRY AGAIN LATER");
            //system("pause");
        }
    }

    public void requestBook() {
        //system("cls");
        int count = 0, count2 = 0;
        String validRandomISBN = "";
        boolean isValid = false;
        boolean isCorrect = false;

        if (Vars.currentViolation == 0){
            while(!isValid){
                if (count<3){
                    //system("cls");
    
                    int bookNum = getBookNumber();
                    System.out.println("BOOK ID: " + bookNum);
    
                    System.out.println("INPUT BOOK TITLE: ");
                    String bookTitle = scan.nextLine();
    
                    System.out.println("INPUT BOOK AUTHOR: ");
                    String bookAuthor = scan.nextLine();
    
                    System.out.println("INPUT PUBLICATION YEAR: ");
                    int pubYear = scan.nextInt();
    
                    System.out.println("INPUT BOOK QUANTITY: ");
                    int bookQuantity = scan.nextInt();
    
                    scan.nextLine(); // Consume the newline character
    
                    int userNum = prompts(4);
                    switch (userNum){
                        case 1: //student know the books' isbn
                                if (pubYear > 0 && pubYear <= 2006 && bookQuantity > 0){
                                    System.out.println("\n\nENTER VALID 10-DIGIT ISBN: ");
                                    String userISBN = scan.nextLine();
                                    int len = userISBN.length();
                                    if (len == 10){
                                        if (checkISBN(userISBN,10)){
                                            validRandomISBN = userISBN;
                                            isValid = true;
                                            break;
                                        }else {
                                            System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                            System.out.println("INVALID ISBN.");
                                            //system("pause");
                                            count++;
                                        }
                                    }else {
                                        System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                        System.out.println("INVALID ISBN.");
                                        //system("pause");
                                        count++;
                                    }
                                }else if (pubYear > 0 && pubYear > 2006 && bookQuantity > 0){
                                    System.out.println("\n\nENTER VALID 13-DIGIT ISBN: ");
                                    String userISBN = scan.nextLine();
                                    int len = userISBN.length();
                                    if (len == 13){
                                        if (checkISBN(userISBN,13)){
                                            validRandomISBN = userISBN;
                                            isValid = true;
                                            break;
                                        }else {
                                            System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                            System.out.println("INVALID ISBN.");
                                            //system("pause");
                                            count++;
                                        }
                                    }else {
                                        System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                        System.out.println("INVALID ISBN.");
                                        //system("pause");
                                        count++;
                                    }
                                }else {
                                    System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                    System.out.println("INVALID PUBLICATION YEAR.");
                                    //system("pause");
                                    count++;
                                }
                                break;
                        case 2: //student dont know the books' isbn
                                //will generate valid isbn base on pub year
                                if (pubYear > 0 && pubYear <= 2006 && bookQuantity > 0){
                                    validRandomISBN = getISBN(10);
                                    isValid = true;
                                }else if (pubYear > 0 && pubYear > 2006 && bookQuantity > 0){
                                    validRandomISBN = getISBN(13);
                                    isValid = true;
                                }else{
                                    System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                    System.out.println("INVALID PUBLICATION YEAR.");
                                    //system("pause");
                                    count++;
                                }
                                break;
                        case 3: //cancelled
                                System.out.println("\nMODIFICATION CANCELLED.");
                                isValid = true;
                                break;
                        default: System.out.println("WRONG RETURN VALUE");
                                 break;
                    }
    
                    if (isValid){
                        if (checkBooks(1, bookTitle, bookAuthor, 0)){
                            while (!isCorrect){
                                if (count2<3){
                                    if (verifyAccount(1)){
                                        isCorrect = true;
                                        BookDetails book = new BookDetails(bookNum, validRandomISBN, bookTitle, bookAuthor, pubYear, bookQuantity);
                                        bookRquestList.add(book);
                                        saveBookRequests();
    
                                        System.out.println("\nBOOK ( " + bookTitle + " ) SUCCESSFULLY REQUESTED.");
                                        //system("pause");
                                    }else {
                                        System.out.println("ERROR.");
                                        System.out.println("INPUT DO NOT MATCH TO YOU CREDENTIALS.");
                                        System.out.println("PLEASE TRY AGAIN");
                                        count2++;
                                    }
                                }else{
                                    System.out.println("ERROR.");
                                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                    System.out.println("PLEASE TRY AGAIN LATER");
                                    //system("pause");
                                    isCorrect = true;
                                }
                            }
                        }else {
                            userNum = prompts(5);
                            if (userNum == 1){
                                while (!isCorrect){
                                    if (count2<3){
                                        if (verifyAccount(2)){
                                            count = 0;
                                            count2 = 0;
                                            isCorrect = true;
                                            isValid = false;
                                        }else {
                                            System.out.println("ERROR.");
                                            System.out.println("INPUT DO NOT MATCH TO YOU CREDENTIALS.");
                                            System.out.println("PLEASE TRY AGAIN");
                                            count2++;
                                        }
                                    }else{
                                        System.out.println("ERROR.");
                                        System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                        System.out.println("GRABE KA NA");
                                        System.out.println("PLEASE TRY AGAIN LATER");
                                        //system("pause");
                                        isCorrect = true;
                                    }
                                }
                            }else {
                                System.out.println("PLEASE TRY AGAIN LATER");
                                //system("pause");
                                isCorrect = true;
                            }
                        }
                    }
                }else {
                    System.out.println("ERROR.");
                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                    System.out.println("PLEASE TRY AGAIN LATER");
                    //system("pause");
                    isValid = true;
                }
            }
        }else {
            System.out.println("TRANSACTION CANCELLED.");
            System.out.println("YOU HAVE A VIOLATION");
            System.out.println("PLEASE SETTLE YOUR VIOLATION FIRST AND TRY AGAIN LATER.");
        }
    }

    public static void viewViolation() {
        boolean isFound = false;
        //locate the account
        for (AccountDetails account : accountList){
            if (account.getStudentID().compareTo(Vars.currentStudentID) == 0){
                isFound = true;
                if (account.getViolation() != 0){
                    System.out.println("YOU HAVE VIOLATION OF: " + account.getViolation());
                    int userNum = prompts(8);
                    if (userNum == 1){
                        account.setViolation(0);
                        saveAccounts();
                        saveAccountFD();
                        System.out.println("CONGRATULATION YOU SETTLE YOUR VIOLATION.");
                    }else {
                        System.out.println("BRUH WTF SETTLE YOUR VIOLATION.");
                    }
                }else {
                    System.out.println("CONGRATULATION YOU DON'T HAVE ANY VIOLATION.");
                }
            }
        }
        if (!isFound){
            System.out.println("SYSTEM ERROR");
            System.out.println("ACCOUNT NOT FOUND IN LIST");
        }
    }


    //----------------------[LIBRARIAN methods]----------------------

    public static void insertExistingBook() {
        int count = 0, count2 = 0;
        boolean isValid = false;
        boolean isNum = false;
        //system("cls");
        displayBooks();

        while (!isValid){
            if (count<3){
                try {
                    System.out.println("ENTER BOOK NUMBER: ");
                    int userNum = scan.nextInt();
    
                    //check if book exist
                    for (BookDetails book : bookList){
                        if (book.getBookNum() == userNum){
                            isValid = true;

                            System.out.println("BOOK SELECTED: ");
                            displayCurrentBook(book.bookNum);

                            while (!isNum){
                                if (count2<3){
                                    System.out.printf("%n%nENTER BOOK COPIES TO BE INSERTED: ");
                                    try {
                                        userNum = scan.nextInt();
                                        if (userNum > 0){
                                            if (verifyAccount(2)){
                                                isNum = true;
                                                book.bookQuantity += userNum;
                                                System.out.printf("%nMODIFICATION SUCCESSFUL.%n%n");
                                                System.out.println("BOOK LIST:");
                                                displayBooks();
                                                System.out.println("\n\nBOOK SELECTED:");
                                                displayCurrentBook(book.bookNum);
                                            }else {
                                                System.out.println("INVALID INPUT");
                                                System.out.println("CREDENTIALS DO NOT MATCH");
                                                System.out.println("PLEASE TRY AGAIN.");
                                                //system("pause");
                                                count2++;
                                            }
                                        }else {
                                            System.out.println("\n\nINVALID INPUT.");
                                            System.out.println("PLEASE TRY AGAIN");
                                            count2++;
                                        }
                                    }catch (InputMismatchException e){
                                        e.printStackTrace();
                                        System.out.println("\n\nINVALID INPUT.");
                                        System.out.println("PLEASE TRY AGAIN");
                                        count2++;
                                    }
                                }else {
                                    System.out.println("ERROR.");
                                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                    System.out.println("PLEASE TRY AGAIN LATER");
                                    //system("pause");
                                    isNum = true;
                                }
                            }

                            break;
                        }
                    }
                    if (!isValid){
                        System.out.println("ERROR");
                        System.out.println("WRONG BOOK NUMBER OR BOOK NUMBER DOES NOT EXIST");
                        System.out.println("PLEASE TRY AGAIN");
                        count++;
                    }
                }catch (InputMismatchException e){
                    e.printStackTrace();
                    System.out.println("INVALID INPUT.");
                    System.out.println("PLEASE TRY AGAIN");
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isValid = true;
            }
        }
    }

    public  void insertNewBook() {
        int count = 0, count2 = 0;
        String validRandomISBN = "";
        boolean isValid = false;
        boolean isCorrect = false;

        while(!isValid){
            if (count<3){
                //system("cls");

                int bookNum = getBookNumber();
                System.out.println("BOOK ID: " + bookNum);

                System.out.println("INPUT BOOK TITLE: ");
                String bookTitle = scan.nextLine();

                System.out.println("INPUT BOOK AUTHOR: ");
                String bookAuthor = scan.nextLine();

                System.out.println("INPUT PUBLICATION YEAR: ");
                int pubYear = scan.nextInt();

                System.out.println("INPUT BOOK QUANTITY: ");
                int bookQuantity = scan.nextInt();

                scan.nextLine(); // Consume the newline character

                int userNum = prompts(4);
                switch (userNum){
                    case 1: //librarian know the books' isbn
                            if (pubYear > 0 && pubYear <= 2006 && bookQuantity > 0){
                                System.out.println("\n\nENTER VALID 10-DIGIT ISBN: ");
                                String userISBN = scan.nextLine();
                                int len = userISBN.length();
                                if (len == 10){
                                    if (checkISBN(userISBN,10)){
                                        validRandomISBN = userISBN;
                                        isValid = true;
                                        break;
                                    }else {
                                        System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                        System.out.println("INVALID ISBN.");
                                        //system("pause");
                                        count++;
                                    }
                                }else {
                                    System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                    System.out.println("INVALID ISBN.");
                                    //system("pause");
                                    count++;
                                }
                            }else if (pubYear > 0 && pubYear > 2006 && bookQuantity > 0){
                                System.out.println("\n\nENTER VALID 13-DIGIT ISBN: ");
                                String userISBN = scan.nextLine();
                                int len = userISBN.length();
                                if (len == 13){
                                    if (checkISBN(userISBN,13)){
                                        validRandomISBN = userISBN;
                                        isValid = true;
                                        break;
                                    }else {
                                        System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                        System.out.println("INVALID ISBN.");
                                        //system("pause");
                                        count++;
                                    }
                                }else {
                                    System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                    System.out.println("INVALID ISBN.");
                                    //system("pause");
                                    count++;
                                }
                            }else {
                                System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                System.out.println("INVALID PUBLICATION YEAR.");
                                //system("pause");
                                count++;
                            }
                            break;
                    case 2: //librarian dont know the books' isbn
                            //will generate valid isbn base on pub year
                            if (pubYear > 0 && pubYear <= 2006 && bookQuantity > 0){
                                validRandomISBN = getISBN(10);
                                isValid = true;
                            }else if (pubYear > 0 && pubYear > 2006 && bookQuantity > 0){
                                validRandomISBN = getISBN(13);
                                isValid = true;
                            }else{
                                System.out.println("\nMODIFICATION UNSUCCESSFUL.");
                                System.out.println("INVALID PUBLICATION YEAR.");
                                //system("pause");
                                count++;
                            }
                            break;
                    case 3: //cancelled
                            System.out.println("\nMODIFICATION CANCELLED.");
                            isValid = true;
                            break;
                    default: System.out.println("WRONG RETURN VALUE");
                             break;
                }

                if (isValid){
                    if (checkBooks(1, bookTitle, bookAuthor, 0)){
                        while (!isCorrect){
                            if (count2<3){
                                if (verifyAccount(2)){
                                    isCorrect = true;
                                    BookDetails book = new BookDetails(bookNum, validRandomISBN, bookTitle, bookAuthor, pubYear, bookQuantity);
                                    bookList.add(book);
                                    saveBooks();

                                    System.out.println("\nBOOK ( " + bookTitle + " ) SUCCESSFULLY ADDED.");
                                    //system("pause");
                                }else {
                                    System.out.println("ERROR.");
                                    System.out.println("INPUT DO NOT MATCH TO YOU CREDENTIALS.");
                                    System.out.println("PLEASE TRY AGAIN");
                                    count2++;
                                }
                            }else{
                                System.out.println("ERROR.");
                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                System.out.println("PLEASE TRY AGAIN LATER");
                                //system("pause");
                                isCorrect = true;
                            }
                        }
                    }else {
                        userNum = prompts(5);
                        if (userNum == 1){
                            while (!isCorrect){
                                if (count2<3){
                                    if (verifyAccount(2)){
                                        count = 0;
                                        count2 = 0;
                                        isCorrect = true;
                                        isValid = false;
                                    }else {
                                        System.out.println("ERROR.");
                                        System.out.println("INPUT DO NOT MATCH TO YOU CREDENTIALS.");
                                        System.out.println("PLEASE TRY AGAIN");
                                        count2++;
                                    }
                                }else{
                                    System.out.println("ERROR.");
                                    System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                    System.out.println("GRABE KA NA");
                                    System.out.println("PLEASE TRY AGAIN LATER");
                                    //system("pause");
                                    isCorrect = true;
                                }
                            }
                        }else {
                            System.out.println("PLEASE TRY AGAIN LATER");
                            //system("pause");
                            isCorrect = true;
                        }
                    }
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isValid = true;
            }
        }
    }

    public static void editBooks() {
        int ch = 0, count = 0;
        int newBookNum, newBookYear, newBookQuant;
        String newISBN, newBookTitle, newBookAuthor;
        boolean isCorrect = false;
        boolean isFound = false;

        while (!isCorrect){
            if (count<3){
                if (verifyAccount(2)){
                    isCorrect = true;

                    System.out.println("\n\nENTER BOOK NUMBER TO BE EDITED: ");
                    int userBookNum = scan.nextInt();

                    for (BookDetails book : bookList){
                        if (book.getBookNum() == userBookNum){
                            isFound = true;
                            while (ch != 6){
                                //system("cls");
                                System.out.println("\n\n\nBOOK NUMBER: " + book.bookNum);
                                System.out.println("ISBN: " + book.ISBN);
                                System.out.println("BOOK TITLE: " + book.bookTitle);
                                System.out.println("BOOK AUTHOR: " + book.bookAuthor);
                                System.out.println("BOOK PUBLICATION YEAR: " + book.publicationYear);
                                System.out.println("BOOK QUANTITY: " + book.bookQuantity);
                                ch = prompts(6);
                                switch (ch){
                                    case 1: System.out.println("INPUT NEW VALID BOOK NUMBER: ");
                                            newBookNum = scan.nextInt();
                                            if (checkBooks(2, null, null, newBookNum)){
                                                book.setBookNum(newBookNum);
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("BOOK NUMBER ALREADY EXIST.");
                                            }
                                            break;
                                    case 2: System.out.println("INPUT NEW VALID BOOK TITLE: ");
                                            newBookTitle = scan.nextLine();
                                            if (checkBooks(1, newBookTitle, book.getBookAuthor(), 0)){
                                                book.setBookTitle(newBookTitle);
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("BOOK DETAILS ALREADY EXIST.");
                                            }
                                            break;
                                    case 3: System.out.println("INPUT NEW VALID BOOK AUTHOR: ");
                                            newBookAuthor = scan.nextLine();
                                            if (checkBooks(1, book.getBookTitle(), newBookAuthor, 0)){
                                                book.setBookTitle(newBookAuthor);
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("BOOK DETAILS ALREADY EXIST.");
                                            }
                                            break;
                                    case 4: System.out.println("INPUT NEW VALID BOOK PUBLICATION YEAR: ");
                                            newBookYear = scan.nextInt();
                                            LocalDate currDate = LocalDate.now();
                                            if (newBookYear > 0 && newBookYear < currDate.getYear()){
                                                book.setBookPubYear(newBookYear);

                                                //update the ISBN base on the new pub date
                                                if (newBookYear <= 2006){
                                                    newISBN = getISBN(10);
                                                }else {
                                                    newISBN = getISBN(13);
                                                }
                                                book.setBookISBN(newISBN);
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("INVALID BOOK PUBLICATION YEAR.");
                                            }
                                            break;
                                    case 5: System.out.println("INPUT NEW VALID BOOK QUANTITY: ");
                                            newBookQuant = scan.nextInt();
                                            if (newBookQuant > 0){
                                                book.setBookQuantity(newBookQuant);
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("INVALID BOOK QUANTITY.");
                                            }
                                            break;
                                    case 6: System.out.println("\n\nBOOK SUCCESSFULLY EDITED.");
                                            //system("pause");
                                            break;
                                    default: System.out.println("\n\nSELECT 1 - 6 ONLY.");
                                            break;
                                }
                            } 
                            saveBooks();
                            logs("EDIT BOOK", book.getBookTitle());
                            break;
                        }
                    }
                    if (!isFound){
                        System.out.println("ERROR NOT FOUND.");
                        System.out.println("WRONG BOOK NUMBER OR IT DOES NOT EXIST");
                        System.out.println("PLEASE TRY AGAIN LATER");
                        //system("pause");
                        isCorrect = false;
                        count++;
                    }
                }else{
                    System.out.println("INVALID INPUT");
                    System.out.println("DOES NOT MATCH TO YOUR CREDENTIALS");
                    System.out.println("PLEASE TRY AGAIN");
                    count++;
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isCorrect = true;
            }
        }
    }

    public static void removeAcopy() {
        int count = 0, count2 = 0;
        boolean isValid = false;
        boolean isCorrect = false;

        while (!isValid){
            if (count<3){
                System.out.println("\n\nENTER BOOK NUMBER TO REMOVE A COPY: ");
                int userBookNum = scan.nextInt();

                for(BookDetails book : bookList){
                    if (book.getBookNum() == userBookNum){
                        isValid = true;
                        while (!isCorrect){
                            if (count2<3){
                                System.out.println("\n\nBOOK SELECTED: ");
                                displayCurrentBook(userBookNum);

                                System.out.println("ENTER NUMBER OF COPIES TO REMOVE: ");
                                int userQuantity = scan.nextInt();
                                if (userQuantity > 0 && userQuantity < book.getBookQuantity()){
                                    if (verifyAccount(2)){
                                        isCorrect = true;
                                        int newQuant = book.getBookQuantity() - userQuantity;
                                        book.setBookQuantity(newQuant);
                                        displayBooks();
                                        System.out.println("\n\nMODIFICATION SUCCESSFUL.");
                                        displayCurrentBook(userBookNum);

                                        saveBooks();
                                        logs("REMOVE A COPY", book.getBookTitle());
                                    }else {
                                        System.out.println("INVALID INPUT");
                                        System.out.println("DOES NOT MATCH TO YOUR CREDENTIALS");
                                        System.out.println("PLEASE TRY AGAIN");
                                        count2++;
                                    }
                                }else{
                                    System.out.println("ERROR.");
                                    System.out.println("iNVALID QUANTITY.");
                                    count2++;
                                }
                            }
                        }
                        break;
                    }
                }
                if (!isValid){
                    System.out.println("ERROR NOT FOUND.");
                    System.out.println("WRONG BOOK NUMBER OR IT DOES NOT EXIST");
                    System.out.println("PLEASE TRY AGAIN LATER");
                    count++;
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isValid = true;
            }
        }
    }

    public static void removeBooks() {
        int count = 0, count2 = 0;
        boolean isValid = false;
        boolean isCorrect = false;

        while (!isValid){
            if (count<3){
                System.out.println("\n\nENTER BOOK NUMBER TO REMOVE A COPY: ");
                int userBookNum = scan.nextInt();

                for(BookDetails book : bookList){
                    if (book.getBookNum() == userBookNum){
                        isValid = true;
                        while (!isCorrect){
                            if (count2<3){
                                System.out.println("\n\nBOOK SELECTED: ");
                                displayCurrentBook(userBookNum);

                                if (verifyAccount(2)){
                                    isCorrect = true;
                                    bookList.remove(book);
                                    displayBooks();
                                    System.out.println("\n\nMODIFICATION SUCCESSFUL.");

                                    saveBooks();
                                    logs("REMOVE A COPY", book.getBookTitle());
                                }else {
                                    System.out.println("INVALID INPUT");
                                    System.out.println("DOES NOT MATCH TO YOUR CREDENTIALS");
                                    System.out.println("PLEASE TRY AGAIN");
                                    count2++;
                                }
                            }
                        }
                        break;
                    }
                }
                if (!isValid){
                    System.out.println("ERROR NOT FOUND.");
                    System.out.println("WRONG BOOK NUMBER OR IT DOES NOT EXIST");
                    System.out.println("PLEASE TRY AGAIN LATER");
                    count++;
                }
            }else {
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isValid = true;
            }
        }
    }

    public void bookRequests() {
        int count = 0, count2 = 0;
        boolean isFound = false;
        boolean isCorrect = false;

        File fp = new File(Vars.LMSFolderDB + "\\BookRequests.tx");
        if (fp.exists()){
            try {
                //read the file 
                FileReader f2p = new FileReader(fp);
                BufferedReader bf2p = new BufferedReader(f2p);
                String str;
                
                while ((str = bf2p.readLine()) != null){
                    String[] tokens = str.split(",");

                    int reqBookNum = Integer.parseInt(tokens[0]);
                    String reqISBN = tokens[1];
                    String reqBookTitle = tokens[2];
                    String reqBookAuthor = tokens[3];
                    int reqPublicationYear = Integer.parseInt(tokens[4]);
                    int reqBookQuant = Integer.parseInt(tokens[5]);

                    BookDetails reqBook = new BookDetails(reqBookNum, reqISBN, reqBookTitle, reqBookAuthor, reqPublicationYear, reqBookQuant);
                    bookRquestList.add(reqBook);
                }
                bf2p.close();

                //display contents
                System.out.println("\nBOOK REQUESTS");
                displayBookRequests();

                //approve or not
                int userNum = prompts(7);
                switch (userNum){
                    case 1: //approve book request
                        while (!isFound){
                            if (count<3){
                                System.out.println("ENTER BOOK NUMBER: ");
                                userNum = scan.nextInt();

                                //search book
                                for (BookDetails reqBook: bookRquestList){
                                    if (reqBook.getBookNum() == userNum){
                                        isFound = true;
                                        while (!isCorrect){
                                            if (count2<3){
                                                if (verifyAccount(2)){
                                                    isCorrect = true;
                                                    bookList.add(reqBook);
                                                    bookRquestList.remove(reqBook);
                                                    saveBooks();
                                                    saveBookRequests();
                                                    logs("APPROVED BOOK", reqBook.getBookTitle());

                                                    System.out.println("BOOK ( " + reqBook.getBookTitle() + " ) APPROVED.");
                                                    //system("pause");
                                                }else {
                                                    System.out.println("INVALID INPUT");
                                                    System.out.println("DOES NOT MATCH TO YOUR CREDENTIALS");
                                                    System.out.println("PLEASE TRY AGAIN");
                                                    count2++;
                                                }
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                                System.out.println("PLEASE TRY AGAIN LATER");
                                                //system("pause");
                                                isCorrect = true;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (!isFound){
                                    System.out.println("ERROR");
                                    System.out.println("WRONG BOOK NUMBER OR BOOK NUMBER DOES NOT EXIST");
                                    System.out.println("PLEASE TRY AGAIN");
                                    count++;
                                }
                            }else {
                                System.out.println("ERROR.");
                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                System.out.println("PLEASE TRY AGAIN LATER");
                                //system("pause");
                                isFound = true;
                            }
                        }
                        break;
                    case 2: //disapprove book
                        while (!isFound){
                            if (count<3){
                                System.out.println("ENTER BOOK NUMBER: ");
                                userNum = scan.nextInt();

                                //search book
                                for (BookDetails reqBook: bookRquestList){
                                    if (reqBook.getBookNum() == userNum){
                                        isFound = true;
                                        while (!isCorrect){
                                            if (count2<3){
                                                if (verifyAccount(2)){
                                                    isCorrect = true;
                                                    bookRquestList.remove(reqBook);
                                                    saveBooks();
                                                    saveBookRequests();
                                                    logs("DISAPPROVED BOOK", reqBook.getBookTitle());

                                                    System.out.println("BOOK ( " + reqBook.getBookTitle() + " ) DISAPPROVED.");
                                                    //system("pause");
                                                }else {
                                                    System.out.println("INVALID INPUT");
                                                    System.out.println("DOES NOT MATCH TO YOUR CREDENTIALS");
                                                    System.out.println("PLEASE TRY AGAIN");
                                                    count2++;
                                                }
                                            }else {
                                                System.out.println("ERROR.");
                                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                                System.out.println("PLEASE TRY AGAIN LATER");
                                                //system("pause");
                                                isCorrect = true;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (!isFound){
                                    System.out.println("ERROR");
                                    System.out.println("WRONG BOOK NUMBER OR BOOK NUMBER DOES NOT EXIST");
                                    System.out.println("PLEASE TRY AGAIN");
                                    count++;
                                }
                            }else {
                                System.out.println("ERROR.");
                                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                                System.out.println("PLEASE TRY AGAIN LATER");
                                //system("pause");
                                isFound = true;
                            }
                        }
                        break;
                    case 3: //save and exit
                            saveBooks();
                            saveBookRequests();
                        break;
                    default: System.out.println("INVALID INPUT.");
                            break;
                            
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
                System.out.println("\nERROR.");
                System.out.println("NO BOOK REQUESTS YET.");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("ERROR.");
            System.out.println("NO BOOK REQUESTS YET.");
            //system("pause");
        }
    }

    public static void displayLogs() {
        int count = 0, userNum;
        boolean isValid = false;
        boolean isValid2 = false;

        while(!isValid){
            if(count<3){
                //system("cls");
                displayDirectoryContent();
                System.out.printf("%n%n!DO NOT INCLUDE THE .csv EXTENSION!%n");
                System.out.printf("%n%nENTER DATE (YYYY-MM-DD): ");
                String userStr = scan.nextLine();

                File fp = new File(Vars.LMSLogs + "\\" + userStr + ".csv");
                if(fp.exists()){
                    try {
                        FileReader f2p = new FileReader(fp);
                        BufferedReader bf2p = new BufferedReader(f2p);
                        String str;

                        System.out.printf("%n| %-11s | %-11s | %-50s | %-10s | %-10s |%n",
                                "DATE", "TIME", "NAME", "STATUS", "ITEM");

                        while((str = bf2p.readLine()) != null){
                            String[] tokens = str.split(",");

                            System.out.printf("| %-11s | %-11s | %-50s | %-10s | %-10s |%n",
                                    tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
                        }
                        while (!isValid2){
                            System.out.printf("%n%n");
                            System.out.println("DO YOU WANT TO VIEW OTHER DATES? ");
                            System.out.println("[1] YES");
                            System.out.println("[2] NO");
                            System.out.println("-> ");
                            try{
                                userNum = scan.nextInt();
                                if (userNum > 0 && userNum <= 2){
                                    if (userNum == 1){
                                        isValid2 = true;
                                    }else{
                                        isValid = true;
                                        isValid2 = true;
                                    }
                                }else{
                                    System.out.println("INVALID INPUT");
                                    System.out.println("PLEASE TRY AGAIN.");
                                }
                            }catch (InputMismatchException e){
                                e.printStackTrace();
                                System.out.println("INVALID INPUT.");
                                System.out.println("PLEASE TRY AGAIN");
                            }
                        }
                        bf2p.close();
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    //system("pause");
                }else{
                    System.out.println("ERROR.");
                    System.out.println("FILE ( " + userStr + ".csv ) NOT FOUND.");
                    System.out.println("PLEASE TRY AGAIN.");
                    //system("pause");
                    count++;
                }
            }else{
                System.out.println("ERROR.");
                System.out.println("TOO MANY UNSUCCESSFUL ATTEMPTS.");
                System.out.println("PLEASE TRY AGAIN LATER");
                //system("pause");
                isValid = true;
            }
        }
        //system("cls");
    }

    public static void displayDirectoryContent() {
        File directory = new File(Vars.LMSLogs);

        //Get the contents of the directory
        File[] files = directory.listFiles();

        //Display the contents of the directory
        if (files != null){
            for (File file : files){
                System.out.println(file.getName());
            }
        }
        System.out.printf("%n%n");
    }




    //----------------------[UTILITY]----------------------

    public static int menu(int x) {
        int userNum=0;
        boolean isValid = false;
        //system("cls");
        while (!isValid){
            System.out.printf("%n%n%nMENU%n%n");

            if (x==1){ //student
                System.out.println("[1] DISPLAY BOOKS");
                System.out.println("[2] BORROW BOOKS");
                System.out.println("[3] RETURN BOOKS");
                System.out.println("[4] REQUEST BOOKS");
                System.out.println("[5] VIEW VIOLATIONS");
                System.out.println("[6] SAVE AND EXIT");
                System.out.printf("%n->");

                try {
                    userNum = scan.nextInt();
                    if (userNum > 0 && userNum <= 6){
                        isValid = true;
                    }else{
                        System.out.println("INVALID INPUT.");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }catch (InputMismatchException e) {
                    e.printStackTrace();
                    System.out.printf("%nINVALID INPUT.%n");
                    System.out.println("PLEASE TRY AGAIN");
                }
            }else{ //librarian
                System.out.println("[1] ADD BOOKS");
                System.out.println("[2] EDIT BOOKS");
                System.out.println("[3] REMOVE BOOKS");
                System.out.println("[4] BOOK REQUESTS");
                System.out.println("[5] DISPLAY BOOKS");
                System.out.println("[6] DISPLAY LOGS");
                System.out.println("[7] SAVE AND EXIT");
                System.out.printf("%n->");

                try {
                    userNum = scan.nextInt();
                    if (userNum > 0 && userNum <= 7){
                        isValid = true;
                    }else{
                        System.out.println("INVALID INPUT.");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }catch (InputMismatchException e) {
                    e.printStackTrace();
                    System.out.printf("%nINVALID INPUT.%n");
                    System.out.println("PLEASE TRY AGAIN");
                }
            }
        }
        return userNum;
    }

    public static int prompts(int x) {
        int userNum = 0;
        boolean isValid = false;
        switch (x){
            case 1: // main under case 2 [borrowBooks]
                System.out.printf("%n%nINPUT BOOK NUMBER: ");
                userNum = scan.nextInt();
                //check if existing?
                break;
            case 2: // main under case 1 [addBooks]
                while (!isValid){
                    System.out.printf("%n%nINSERT EXISTING BOOK?%n");
                    System.out.println("[1] YES");
                    System.out.println("[2] NO");
                    System.out.println("[3] CANCEL");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 3){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 3: // main under case 3 [removeBooks]
                while (!isValid){
                    System.out.printf("%n%nREMOVE BOOK%n");
                    System.out.println("[1] REMOVE 'X' NUMBER OF COPY");
                    System.out.println("[2] REMOVE ALL BOOK COPIES");
                    System.out.println("[3] CANCEL");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 3){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 4: //insertNewBook method
                while (!isValid){
                    System.out.println("DO YOU KNOW THE BOOKS' ISBN? ");
                    System.out.println("[1] YES");
                    System.out.println("[2] NO");
                    System.out.println("[3] CANCEL");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 3){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 5: //insertNewBook method bandang huli
                while (!isValid){
                    System.out.println("BOOK ALREADY EXIST.");
                    System.out.println("DO YOU WANT TO TRY AGAIN?");
                    System.out.println("[1] YES");
                    System.out.println("[2] NO");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 2){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 6: //editbooks menu/choices
                while (!isValid){
                    System.out.println("CHANGE BOOK DETAILS");
                    System.out.println("[1] BOOK NUMBER");
                    System.out.println("[2] BOOK TITLE");
                    System.out.println("[3] BOOK AUTHOR");
                    System.out.println("[4] BOOK PUBLICATION YEAR");
                    System.out.println("[5] BOOK QUANTITY");
                    System.out.println("[6] SAVE & EXIT");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 6){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 7: //book request options
                while (!isValid){
                    System.out.println("OPTIONS");
                    System.out.println("[1] APPROVE BOOK REQUEST");
                    System.out.println("[2] DISAPPROVE BOOK REQUEST");
                    System.out.println("[3] SAVE & EXIT");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 3){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;
            case 8: //VIEWvIOLATION METHOD
                while (!isValid){
                    System.out.println("DO YOU WANT TO SETTLE YOUR VIOLATION?");
                    System.out.println("[1] YES");
                    System.out.println("[2] NO");
                    System.out.print("-> ");

                    try {
                        userNum = scan.nextInt();
                        if (userNum > 0 && userNum <= 2){
                            isValid = true;
                            return userNum;
                        }else{
                            System.out.println("INVALID INPUT.");
                            System.out.println("PLEASE TRY AGAIN");
                        }
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.out.printf("%nINVALID INPUT.%n");
                        System.out.println("PLEASE TRY AGAIN");
                    }
                }
                break;

            default: //system("cls");
                System.out.printf("%n%nINVALID PARAMETER.%n");
                //system("pause");
                //system("cls");
                break;
        }
        return userNum;
    }

    public static void displayBooks() {
        //system("cls");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-8s | %-15s | %-74s | %-82s | %-9s | %-8s |%n",
                "BOOK NUM", "ISBN", "BOOK TITLE", "BOOK AUTHOR", "PUB. YEAR", "QUANTITY");
        System.out.println("-------------------------------------------------------------");

        for (BookDetails book : bookList){
            System.out.printf("| %-8s | %-15s | %-74s | %-82s | %-9s | %-8s |%n",
                    book.getBookNum(), book.getISBN(), book.getBookTitle(),
                    book.getBookAuthor(), book.getPublicationYear(), book.getBookQuantity());
        }
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%n%n");
    }

    public static void displayCurrentBook(int x){
        System.out.printf("%n-------------------------------------------------------------");
        System.out.printf("%n%nBook Details:%n");
        System.out.printf("| %-8s | %-15s | %-74s | %-82s | %-9s | %-8s |%n",
                "BOOK NUM", "ISBN", "BOOK TITLE", "BOOK AUTHOR", "PUB. YEAR", "QUANTITY");

        //search for the book
        for (BookDetails book : bookList){
            if (book.getBookNum() == x){
                System.out.printf("| %-8d | %-15s | %-74s | %-82s | %-9d | %-8d |%n",
                        book.getBookNum(), book.getISBN(), book.getBookTitle(),
                        book.getBookAuthor(), book.getPublicationYear(), book.getBookQuantity());
            }
        }
    }

    public static void displayBookRequests() {
        //system("cls");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("| %-8s | %-15s | %-74s | %-82s | %-9s | %-8s |%n",
                "BOOK NUM", "ISBN", "BOOK TITLE", "BOOK AUTHOR", "PUB. YEAR", "QUANTITY");
        System.out.println("-------------------------------------------------------------");

        for (BookDetails book : bookRquestList){
            System.out.printf("| %-8s | %-15s | %-74s | %-82s | %-9s | %-8s |%n",
                    book.getBookNum(), book.getISBN(), book.getBookTitle(),
                    book.getBookAuthor(), book.getPublicationYear(), book.getBookQuantity());
        }
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%n%n");
    }

    public static int checkAccount(int x, String ID) {
        boolean isFound = false;
        if (x == 1){ // student
            for (AccountDetails account : accountList){
                if (account.getStudentID().compareTo(ID) == 0){
                    isFound = true;
                    return 1;
                }
            }
            if (!isFound){
                return 2;
            }
        }else { //librarian
            if (Vars.adminFillID.compareTo(ID) == 0){
                return 1;
            }else{
                return 2;
            }
        }
        return 0;
    }

    public static boolean verifyAccount(int x) {
        if (x==1){ // student
            System.out.println("ENTER STUDENT ID TO CONFIRM: ");
            String userStr = scan.nextLine();
            if (userStr.compareTo(Vars.currentStudentID) == 0){
                return true;
            }else{
                return false;
            }
        }else{ //librarian
            System.out.println("ENTER LIBRARIAN ID TO CONFIRM: ");
            String userStr = scan.nextLine();
            if (userStr.compareTo(Vars.adminFillID) == 0){
                return true;
            }else{
                return false;
            }
        }
    }

    public static int getBookNumber() {
        int newBookNum = 0;
        boolean isValid = false;
        Random srand = new Random();
        while (!isValid){
            do{
                newBookNum = srand.nextInt();
            }while (newBookNum > 0);

            for (BookDetails book : bookList){
                if (book.getBookNum() != newBookNum){
                    isValid = true;
                    break;
                }
            }
        }
        return newBookNum;
    }

    public static String getISBN(int x) {
        long randomISBN;
        String randomISBNstring = "";
        boolean isValid = false;
        Random srand = new Random();
        if (x == 10){
            while (!isValid){
                do{
                    randomISBN = srand.nextInt(999999999) + 1000000000;
                }while (randomISBN > 0);
    
                randomISBNstring = String.valueOf(randomISBN);
                int len = randomISBNstring.length();
    
                //check if valid
                if (len == x){
                    if (checkISBN(randomISBNstring, x)){
                        isValid = true;
                    }
                }
            }
        }else if (x == 13){
            while (!isValid){
                do{
                    randomISBN = srand.nextLong(999999999999L) + 1000000000000L;
                }while (randomISBN > 0);
    
                randomISBNstring = String.valueOf(randomISBN);
                int len = randomISBNstring.length();
    
                //check if valid
                if (len == x){
                    if (checkISBN(randomISBNstring, x)){
                        isValid = true;
                    }
                }
            }
        }else {
            System.out.println("\n\nWRONGPARAMETER.");
            System.out.println("PLEASE CHECK THE CODE.");
        }
        
        return randomISBNstring;
    }

    public static boolean checkISBN(String str, int x) {
        int sum = 0;
        if (x == 10){
            for (int i=0; i<9; i++){
                char c = str.charAt(i);

                if (!Character.isDigit(c)){
                    return false;
                }

                int digit = Character.getNumericValue(c);
                sum += (digit * (x - i));
            }

            //Check if last char is a digit or 'X'
            char checkLastChar = str.charAt(9);
            if (!Character.isDigit(checkLastChar) && checkLastChar != 'X'){
                return false;
            }

            //else if last char is 'X'
            int lastDigit = (checkLastChar == 'X') ? 10 : Character.getNumericValue(checkLastChar);
            sum += lastDigit;

            //check if isbn is existing or not
            for (BookDetails book : bookList){
                if (book.getISBN().compareTo(str) == 0){
                    return false;
                }
            }

            //return true if sum is divisible by 11
            return sum % 11 == 0;
        }else if (x==13){
            for (int i=0; i<12; i++){
                char c = str.charAt(i);

                if (!Character.isDigit(c)){
                    return false;
                }

                //multiply the digit by 1 if the index is even else 
                //multiply the digit by 3 if the index is odd
                //why? ewan ko ganon daw e HAHAHAHA
                int digit = Character.getNumericValue(c);
                sum += (i % 2 == 0) ? digit : digit * 3;
            }

            //Check if last char is a digit or 'X'
            char checkLastChar = str.charAt(12);
            if (!Character.isDigit(checkLastChar) && checkLastChar != 'X'){
                return false;
            }

            //else if last char is 'X'
            int lastDigit = (checkLastChar == 'X') ? 10 : Character.getNumericValue(checkLastChar);
            sum += lastDigit;

            //check if isbn is existing or not
            for (BookDetails book : bookList){
                if (book.getISBN().compareTo(str) == 0){
                    return false;
                }
            }

            //return true if sum is divisible by 10
            //why 10? ewan ko hindi raw same sa 10 digit na divisble by 11 dapat
            return sum % 10 == 0;
        }else {
            System.out.println("\n\nWRONG PARAMETERS.");
            System.out.println("PLEASE CHECK THE CODE.");
            return false;
        }
    }

    public static boolean checkBooks(int x, String title, String author, int bNum) {
        if (x == 1){
            for (BookDetails book : bookList){
                if (book.getBookTitle().equalsIgnoreCase(title) && book.getBookAuthor().equalsIgnoreCase(author)){
                    displayCurrentBook(book.getBookNum());
                    return false;
                }
            }
        }else if (x == 2){
            for (BookDetails book : bookList){
                if (book.getBookNum() == bNum){
                    return false;
                }
            }
        }else {
            System.out.println("\n\nWRONG PARAMETERS.");
            System.out.println("PLEASE CHECK THE CODE.");
            return false;
        }
        
        return true;
    }

    public static void saveAccounts() {
        File fp = new File(Vars.LMSFolderDB + "\\Accounts.csv");
        if (!fp.exists()){
            try {
                fp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp))) {
            for (AccountDetails account : accountList){
                //-------------------encrypting-------------------


                //-------------------saving-------------------
                fwrite.write(account.getStudentID() + "," + account.getLibrarianID() + "," +
                            account.getStudentName() + "," + account.getLibrarianName() + "," +
                            account.getSkey() + "," + account.getLkey() + "," + account.getViolation());
                fwrite.newLine();
            }
        }catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void saveAccountFD() {
        File fp = new File(Vars.LMSFlashDrive + "\\userDetails.csv");
        if (!fp.exists()){
            try {
                fp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Vars.currentSKey == 1 && Vars.currentLKey == 0){ 
            //save student info in flashdrive
            try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp))) {
                for (AccountDetails account : accountList){
                    if (Vars.currentStudentID.equals(account.getStudentID())){
                        //-------------------encrypting-------------------


                        //-------------------saving-------------------
                        fwrite.write(account.getStudentID() + "," + account.getLibrarianID() + "," +
                                    account.getStudentName() + "," + account.getLibrarianName() + "," +
                                    account.getSkey() + "," + account.getLkey() + "," + account.getViolation());
                        fwrite.newLine();
                    }
                }
            }catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        }else if (Vars.currentSKey == 0 && Vars.currentLKey == 1){ 
            //save librarian info in file
            try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp))) {
                for (AccountDetails account : accountList){
                    if (Vars.currentLibrarianID.equals(account.getLibrarianID())){
                        //-------------------encrypting-------------------


                        //-------------------saving-------------------
                        fwrite.write(account.getStudentID() + "," + account.getLibrarianID() + "," +
                                    account.getStudentName() + "," + account.getLibrarianName() + "," +
                                    account.getSkey() + "," + account.getLkey() + "," + account.getViolation());
                        fwrite.newLine();
                    }
                }
            }catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }else {
            System.out.println("ERROR.");
            System.out.println("UNABLE TO SAVE IN FILE.");
        }

        //decrypt data again to use properly in the program
        //-------------------decrypting-------------------
    }

    public static void saveBooks() {
        File fp = new File(Vars.LMSFolderDB + "\\Books.txt");
        if (!fp.exists()){
            try {
                fp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp))) {
            for (BookDetails book : bookList){
                //-------------------encrypting-------------------


                //-------------------saving-------------------
                fwrite.write(book.getBookNum() + "," + book.getISBN() + "," +
                            book.getBookTitle() + "," + book.getBookAuthor() + "," +
                            book.getPublicationYear() + "," + book.getBookQuantity());
                fwrite.newLine();
            }
        }catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void saveBookRequests() {
        File fp = new File(Vars.LMSFolderDB + "\\BookRequests.txt");
        if (!fp.exists()){
            try {
                fp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp))) {
            for (BookDetails book : bookRquestList){
                //-------------------encrypting-------------------


                //-------------------saving-------------------
                fwrite.write(book.getBookNum() + "," + book.getISBN() + "," +
                            book.getBookTitle() + "," + book.getBookAuthor() + "," +
                            book.getPublicationYear() + "," + book.getBookQuantity());
                fwrite.newLine();
            }
        }catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    public static void logs(String STATUS, String ITEM) {
        //get the current date as a filename
        LocalDate currDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedCurrDate = currDate.format(dateFormatter);

        LocalTime currTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedCurrTime = currTime.format(timeFormatter);


        //save to the file path (append)
        File fp = new File(Vars.LMSLogs + "\\" + formattedCurrDate + ".csv");

        if (Vars.currentSKey == 1 && Vars.currentLKey == 0){
            //-------------------student-------------------
            try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp, true))) {
                fwrite.write(formattedCurrDate + "," + formattedCurrTime + "," + 
                            Vars.currentStudentName + "," + STATUS + "," + ITEM);  
                fwrite.newLine(); 
            }catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        }else if (Vars.currentSKey == 0 && Vars.currentLKey == 1){
            //-------------------librarian-------------------
            try (BufferedWriter fwrite = new BufferedWriter(new FileWriter(fp, true))) {
                fwrite.write(formattedCurrDate + "," + formattedCurrTime + "," + 
                            Vars.currentStudentName + "," + STATUS + "," + ITEM);  
                fwrite.newLine(); 
            }catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }


    //----------------------[UI]----------------------

    public static void front()  {
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
    }

    public static void loading() {
        System.out.println("LOADING......");
    }

    public static void scanScreen(int x) {
        if(x==1){
            System.out.println("WELCOME USER!");
        }else{
            System.out.println("NEW USER!");
        }
    }

    public static void exitMessage() {
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println("HOPE TO SEE YOU AGAIN!");
    }

}