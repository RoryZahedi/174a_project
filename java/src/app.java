package src;

import java.util.Scanner;
import java.sql.Connection;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.text.DecimalFormat;

public class app {
    
    public static String user = new String();


    public static void main(String[] args) {
        Connection conn = null;
        try {
            File f = new File("../db/chinook.db");
            int filelength = (int) f.length();
            // db parameters
            String url = "jdbc:sqlite:../db/chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");

            Statement statement = conn.createStatement();
                
                if(filelength == 0){
                    statement.executeUpdate(readSQLFile("../db/schema.sql"));
                }

            //execute update and execute query for SQL commands
           // statement.executeUpdate("CREATE TABLE Poopoo(length INTEGER, did INTEGER)");

        System.out.println("----------------------------------------");
        System.out.println("------- 174A Final Project -------------");
        System.out.println("------- By Rick Han and Rory Zahedi ----");
        System.out.println("-----------------------------------------\n\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type one of the following commands to get started\nlogin - log/register in for customers admins and managers \nquit - quits the program\n");
        String currentPage = scanner.nextLine();
        while(!currentPage.equals("quit") && !currentPage.equals("login")){
            System.out.println("Invalid Command!\nPlease type one of the following commands to get started\nlogin - log in for customers admins and managers \nquit - quits the program\n");
            currentPage = scanner.nextLine();
        }

        while(!currentPage.equals("quit")){
            if(currentPage.equals("login")){ //has login option or register new account
                currentPage = loginScreen(currentPage, statement);
            }
            else if(currentPage.equals("customer_page")){ //home page for customers to deposit,withdawl,buy,sell, 
                                                            //show balance for market account, list current price of 
                                                            //stock and actor profile and list movie info
                // customerScreen();
                currentPage = customerScreen(currentPage, statement);
            }
            else if(currentPage.equals("manager_page")){ //home page for manager
                currentPage = managerScreen(currentPage, statement);
            }
            else if(currentPage.equals("admin_page")){ //home page for admin to open/close marketm set new price for stock
                                                         //and set new date
                break;
            }
        }

        System.out.println("Program quiting\nHave a nice day!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } 
    }

    public static String loginScreen(String currentPage, Statement statement){
       try{ 
        System.out.println("----------------------------------------");
        System.out.println("------- Login/Register Account ---------");
        System.out.println("----------------------------------------\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("To login type \"login\" to register a new account please type \"register\" \nto quit please type \"quit\"\n");
        String action = scanner.nextLine(); 

        if(action.equals("quit")){
            return "quit";
        }
        if(action.equals("login")){
                System.out.print("Enter Username: ");
                String u = scanner.nextLine();
                user = u;
              
                String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
                ResultSet rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("User Not Found");
                    return currentPage;
                }

                System.out.print("Enter Password: ");
                String password = scanner.nextLine();
                if(u.equals("admin") && password.equals("secret")){
                    System.out.print("\nAdministrator Login Sucessful! \n\n\n ");
                    currentPage = "manager_page";
                    return currentPage;
                }
                query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\' AND C.password = \'%s\';", user, password);
                rs = statement.executeQuery(query);
                if(rs.isBeforeFirst()){
                    System.out.println("\nLogin Sucecssful!: ");
                    currentPage = "customer_page";
                    return currentPage;
                }
                else{
                    System.out.println("\n Error: Invalid Username and Password. Please Try Again ");
                    return currentPage;
                }
        }
        else if(action.equals("register")){
                System.out.println("Enter desired username: ");
                String user = scanner.nextLine();  
                String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
                ResultSet rs = statement.executeQuery(query);
                if(rs.isBeforeFirst()){
                    System.out.println("Invalid Username! User already exists");
                    return currentPage;
                }
                else{
                    System.out.println("Enter desired password: ");
                    String password = scanner.nextLine();  
                    System.out.println("Enter your name: ");
                    String name = scanner.nextLine();  
                    System.out.println("Enter your address: ");
                    String address = scanner.nextLine(); 
                    System.out.println("Enter your state: ");
                    String st = scanner.nextLine(); 
                    System.out.println("Enter your phone: ");
                    String phone = scanner.nextLine(); 
                    System.out.println("Enter your email: ");
                    String email = scanner.nextLine(); 
                    System.out.println("Enter your taxid: ");
                    String taxid = scanner.nextLine(); 
                    System.out.println("Enter your ssn: ");
                    String ssn = scanner.nextLine(); 
                    String insert = String.format("INSERT INTO Customers (name, username, password, address, state, phone, email, taxid, ssn) VALUES (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');", name, user, password, address, st, phone, email, taxid, ssn);
                    statement.executeUpdate(insert);
                    insert = String.format("INSERT INTO Market_Account (taxid, balance, initial) VALUES (\'%s\', 1000.00, 1000.00);", taxid);
                    statement.executeUpdate(insert);
                    System.out.println("Account Successfully Registered!");
                    return currentPage;
                 }
            }
        else{
            System.out.println("Invalid Command Issued \n To login type \"login\" to register a new account please type \"register\" \n to quit please type \"quit\"\n");
        }
        return currentPage;
         } catch (SQLException ex) {
                System.out.println(ex.getMessage());
        }
        return currentPage;
    }
    public static String customerScreen(String currentPage, Statement statement){
       try{ 
        System.out.println("\n\n------------------Home------------------");
        System.out.println("Type \"help\" for an issue of commands!");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();
        String id = "";
        String query = "";
        query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        id = rs.getString("taxid");

        if(action.equals("help")){
            System.out.println("Commands\n--------\ndeposit- add money account balance\nwithdrawl- subtract money from account balance\nbuy- purchase shares of a stock\nsell- sell your shares of a stock\nbalance- show your current balance \nhistory- show your transaction history\nstock- list current price of a stock and the actor profile\nmovie- get information about a movie\ntop- get a list of movies with 5 star rating during a specific time\nlogout- logout from your account \nquit- quit the program\nsecret-list of project specific commands");
        }
        else if(action.equals("secret")){
            System.out.println("Secret Commands\n--------\nopen- open the market for the day\nclose- close the market for the day\nset_price- set the price for a given stock\nset_date- set the date");
        }
        else if(action.equals("deposit")){
            System.out.print("Amount to deposit: ");
            String a = scanner.nextLine();
            float amount = Float.parseFloat(a);
            String update = String.format("UPDATE Market_Account SET balance = balance + %f WHERE taxid = \'%s\';", amount, id);
            statement.executeUpdate(update);
            query = String.format("SELECT date, month FROM Current_Date;");
            rs = statement.executeQuery(query);
            rs.next();
            String date = rs.getString("date");
            int month = rs.getInt("month");
            String insert = String.format("INSERT INTO Market_Transaction (transtype, taxid, amount, date, month) VALUES ('deposit', \'%s\', %f, \'%s\', %d);", id, amount, date, month);
            statement.executeUpdate(insert);
            System.out.println("Successfully depositied " + amount  +" dollar(s)");
            return currentPage;
        }
        else if(action.equals("withdrawl")){
            System.out.print("Enter amount to withdaw: ");
            String a = scanner.nextLine();
            float amount = Float.parseFloat(a);
            query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
            rs = statement.executeQuery(query);
            rs.next();
            float balance = rs.getFloat("balance");
            if( amount > balance){
                System.out.println("Error. Amount requested more than current balance.");
                return currentPage;
            }
            String update = String.format("UPDATE Market_Account SET balance = balance - %f WHERE taxid = \'%s\';", amount, id);
            statement.executeUpdate(update);
            query = String.format("SELECT date, month FROM Current_Date;");
            rs = statement.executeQuery(query);
            rs.next();
            String date = rs.getString("date");
            int month = rs.getInt("month");
            String insert = String.format("INSERT INTO Market_Transaction (transtype, taxid, amount, date, month) VALUES ('withdrawl', \'%s\', %f, \'%s\', %d);", id, amount, date, month);
            statement.executeUpdate(insert);
            System.out.println("Successfully withdrew " + amount  +" dollars");
            return currentPage;
        }
        else if(action.equals("buy")){
                System.out.print("Please enter the name of the stock you would like to purchase: ");
                String stock_name = scanner.nextLine();
                query = String.format("SELECT current FROM Actor_Director_Stock A WHERE A.stockid = \'%s\';", stock_name);
                rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("Invalid stock name.");
                    return currentPage;
                }
                rs.next();
                float stock_price = rs.getFloat("current");
                System.out.println("\nAmount: ");
                String a = scanner.nextLine();
                float amount = Float.parseFloat(a);
                query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
                rs = statement.executeQuery(query);
                float balance = rs.getFloat("balance");
                if(balance >= stock_price * amount + 20){
                    query = String.format("SELECT shares FROM Stock_Account S WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", id, stock_name, stock_price);
                    rs = statement.executeQuery(query);
                    if(!rs.isBeforeFirst()){
                        String insert = String.format("INSERT INTO Stock_Account (taxid, shares, stockid, buyprice) VALUES (\'%s\', %f, \'%s\', %f);", id, amount, stock_name, stock_price);
                        statement.executeUpdate(insert);
                    }
                    else{
                        String update = String.format("UPDATE Stock_Account SET shares = shares + %f WHERE taxid = \'%s\' AND stockid = \'%s\' AND buyprice = %f;", amount, id, stock_name, stock_price);
                        statement.executeUpdate(update);
                    }
                    float cost = stock_price * amount + 20;
                    String update = String.format("UPDATE Market_Account SET balance = balance - %f WHERE taxid = \'%s\';", cost, id);
                    statement.executeUpdate(update);
                    query = String.format("SELECT date, month FROM Current_Date;");
                    rs = statement.executeQuery(query);
                    rs.next();
                    String date = rs.getString("date");
                    int month = rs.getInt("month");
                    String insert = String.format("INSERT INTO Stock_Transaction (transtype, taxid, stockid, shares, buyprice, sellprice, date, month) VALUES ('buy', \'%s\', \'%s\', %f, %f, NULL, \'%s\', %d);", id, stock_name, amount, stock_price, date, month);
                    statement.executeUpdate(insert);
                    System.out.println("Stock successfully purchased");
                }
                else{
                    System.out.println("Insufficient Funds!");     
                }
        }
        else if(action.equals("sell")){
                System.out.print("Please enter the name of the stock you would like to sell: ");
                String stock_name = scanner.nextLine();
                System.out.print("Please enter original buying price: ");
                String o = scanner.nextLine();
                float original = Float.parseFloat(o);
                query = String.format("SELECT current FROM Actor_Director_Stock A WHERE A.stockid = \'%s\';", stock_name);
                rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("Invalid stock name.");
                    return currentPage;
                }
                rs.next();
                float stock_price = rs.getFloat("current");
                query = String.format("SELECT shares FROM Stock_Account S WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", id, stock_name, original);
                rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("No such stock owned at that buying price.");
                    return currentPage;
                }
                System.out.println("\nAmount: ");
                String a = scanner.nextLine();
                float amount = Float.parseFloat(a);
                rs.next();
                float shares = rs.getFloat("shares");
                if(shares > amount){
                    String update = String.format("UPDATE Stock_Account SET shares = shares - %f WHERE taxid = \'%s\' AND stockid = \'%s\' AND buyprice = %f;", amount, id, stock_name, original);
                    statement.executeUpdate(update);
                }
                else if (shares == amount){
                    String update = String.format("DELETE FROM Stock_Account S WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", id, stock_name, original);
                }
                else{
                    System.out.println("Insufficient Number of stock");
                    return currentPage;
                }
                float profit = stock_price * amount - 20;
                String update = String.format("UPDATE Market_Account SET balance = balance + %f WHERE taxid = \'%s\';", profit, id);
                statement.executeUpdate(update);
                query = String.format("SELECT date, month FROM Current_Date;");
                rs = statement.executeQuery(query);
                rs.next();
                String date = rs.getString("date");
                int month = rs.getInt("month");
                String insert = String.format("INSERT INTO Stock_Transaction (transtype, taxid, stockid, shares, buyprice, sellprice, date, month) VALUES ('sell', \'%s\', \'%s\', %f, %f, %f, \'%s\', %d);", id, stock_name, amount, original, stock_price, date, month);
                statement.executeUpdate(insert);
                System.out.println("Stock successfully sold");
        }
        else if(action.equals("balance")){
            query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
            rs = statement.executeQuery(query);
            rs.next();
            float balance = rs.getFloat("balance");
            System.out.println("Balance: " + balance);   
        }
        else if(action.equals("history")){
            query = String.format("SELECT transactionid, transtype, stockid, shares, buyprice, sellprice, date FROM Stock_Transaction S WHERE S.taxid = \'%s\';", id);
            rs = statement.executeQuery(query);
            List<Integer> transactionid = new ArrayList<Integer>();
            List<String> transtype = new ArrayList<String>();
            List<String> stockid = new ArrayList<String>();
            List<Float> shares = new ArrayList<Float>();
            List<Float> buyprice = new ArrayList<Float>();
            List<Float> sellprice = new ArrayList<Float>();
            List<String> date = new ArrayList<String>();
            while(rs.next()){
                transactionid.add(rs.getInt("transactionid"));
                transtype.add(rs.getString("transtype"));
                stockid.add(rs.getString("stockid"));
                shares.add(rs.getFloat("shares"));
                buyprice.add(rs.getFloat("buyprice")); //never null
                sellprice.add(rs.getFloat("sellprice")); // sometimes sellprice is NULL when the transaction is a buy. 
                date.add(rs.getString("date"));
            }
            String table = "\nTransaction ID\tStock ID\tShares\tBuy Price\tSell Price\tDate\t\tTransaction Type";
            System.out.println(table);
            String rows = "";
            for(int i =0; i<transactionid.size(); i++){
                rows += transactionid.get(i) + "\t\t";
                rows += stockid.get(i) + "\t\t";
                rows += shares.get(i) + "\t";
                rows += buyprice.get(i) + "\t\t";
                //turn fake zero sell prices into NULL
                if(transtype.get(i).equals("buy")){
                    rows += "null\t\t";
                }
                else{
                    rows += sellprice.get(i) + "\t\t";
                }
                rows += date.get(i) + "\t";
                rows += transtype.get(i);
                rows += "\n";
            }
            System.out.println(rows);
          
            // FORMAT THESE LISTS TO DISPLAY TO USER - table is a 6 x N
        }
        else if(action.equals("stock")){
            System.out.print("Please enter the name of the stock you would like to view: ");
            String stock_name = scanner.nextLine();
            String row = "";
            query = String.format("SELECT name, dob FROM Actor_Director A WHERE A.stockid = \'%s\';", stock_name);
            rs = statement.executeQuery(query);
            rs.next();
            String name = rs.getString("name");
            row += name + "\t\t";
            String dob = rs.getString("dob");
            row += dob + "\t";
            query = String.format("SELECT role, year, value, title FROM Contracted C WHERE C.stockid = \'%s\';", stock_name);
            rs = statement.executeQuery(query);
            rs.next();
            String role = rs.getString("role");
            row += role + "\t";
            String year = rs.getString("year");
            row += year + "\t";
            int value = rs.getInt("value");
            row += value + "\t";
            String title = rs.getString("title");
            row += title + "\t";
            String table = "\nName \t\t\tDOB\t\t\tRole\tYear\tValue\t\tTitle";
            System.out.println(table);
            System.out.println(row);
            // FORMAT TO PRINT TO USER   
        }
        else if(action.equals("movie")){
            System.out.print("Movie title: ");
            String movie_title = scanner.nextLine();
            query = String.format("SELECT * FROM Movies M WHERE M.title = \'%s\';", movie_title);
            rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("No such stock movie");
                return currentPage;
            } 
            rs.next();
            String row = "";
            String movieid = rs.getString("movieid");
            row += movieid + "\t";
            String year = rs.getString("year");
            row += year + "\t";
            String genre = rs.getString("genre");
            row += genre + "\t";
            float rating = rs.getFloat("rating");
            row += rating + "\t";
            int revenue = rs.getInt("revenue");
            row += revenue + "\t";
            String table = "\nID\tYear\tGenre\tRating\tRevenue";
            System.out.println(table);
            System.out.println(row);
            // FORMAT INFO AND PRINT TO USER
        }
        else if(action.equals("top")){
            System.out.print("Starting year range: ");
            String s = scanner.nextLine();
            int start = Integer.parseInt(s);
            System.out.print("Ending year range: ");
            String e = scanner.nextLine();
            int end = Integer.parseInt(e);
            query = String.format("SELECT title FROM Movies M WHERE M.year > %d AND M.year < %d AND M.rating > 5.0;", start, end);
            rs = statement.executeQuery(query);
            String mList = "";
            List<String> titles = new ArrayList<String>();
            while(rs.next()){
                titles.add(rs.getString("title"));
                mList += rs.getString("title") + "\t";
            }
            System.out.println(mList);

            // FORMAT LIST TO PRINT TO USER 
        }
        else if(action.equals("quit")){
            return "quit";
        }
        else if(action.equals("logout")){
            System.out.println("\n\nSucessfully logged out!");
            return "login";
        }
        else if(action.equals("open")){
             try{
                query = String.format("SELECT month, date FROM Current_Date");
                rs = statement.executeQuery(query);
                int month = rs.getInt("month");
                String date = rs.getString("date");
                int day = Integer.parseInt(date.substring(3,5));
                int year = Integer.parseInt(date.substring(6,10));
                int new_day = day++;
                int new_month = month;
                int new_year = year;
                String new_month_str = "";
                String new_day_str = "";
                String new_year_str = "";
                if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                    if(day == 31){
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else if(month == 4 || month == 6 | month == 9 || month == 11){
                    if(day == 30)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else if(month == 2 && year % 4 == 0){
                    if(day == 29)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account M SET M.initial = M.balance WHERE M.taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else{
                    if(day == 28)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account M SET M.initial = M.balance WHERE M.taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                if(new_month < 10)
                    new_month_str = "0" + String.valueOf(new_month);
                else new_month_str = String.valueOf(new_month);
                if(new_day < 10)
                    new_day_str = "0" + String.valueOf(new_day);
                else new_day_str = String.valueOf(new_day);
                new_year_str = String.valueOf(new_year);
                String new_date = new_month_str + "-" + new_day_str + "-" + new_year_str;
                String update = String.format("UPDATE Current_Date SET date = \'%s\', month = %d;", new_date, new_month);
                statement.executeUpdate(update);
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }   
        }
        else if(action.equals("close")){
            try{
                query = String.format("SELECT date, month FROM Current_Date");
                rs = statement.executeQuery(query);
                String date = rs.getString("date");
                int month = rs.getInt("month");
                String update = String.format("UPDATE Actor_Director_Stock SET closing = current;");
                statement.executeUpdate(update);
                query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                rs = statement.executeQuery(query);
                List<String> taxid = new ArrayList<String>();
                while(rs.next()){
                    taxid.add(rs.getString("taxid"));
                }
                for(int i = 0; i < taxid.size(); i++){
                    query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", taxid.get(i));
                    rs = statement.executeQuery(query);
                    rs.next();
                    float balance = rs.getFloat("balance");
                    update = String.format("INSERT INTO Daily_Report (taxid, balance, date, month) VALUES (\'%s\', %f, \'%s\', %d);", taxid.get(i), balance, date, month);
                    statement.executeUpdate(update);
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }  
        }
        else if(action.equals("set_price")){
            try{
                System.out.println("What stock would you like to set?");
                String stock = scanner.nextLine();
                System.out.println("What price would you like to set " + stock + " to?");
                String n = scanner.nextLine();
                float new_price = Float.parseFloat(n);
                String update = String.format("UPDATE Actor_Director_Stock SET current = %f WHERE stockid = \'%s\';", new_price, stock);
                statement.executeUpdate(update);
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else if(action.equals("set_price")){
            System.out.println("What stock would you like to set?");
            String stock = scanner.nextLine();
            System.out.println("What price would you like to set " + stock + " to?");
            String n = scanner.nextLine();
            float new_price = Float.parseFloat(n);
            String update = String.format("UPDATE Actor_Director_Stock SET current = %f WHERE stockid = \'%s\';", new_price, stock);
            statement.executeUpdate(update);
        }
        else if(action.equals("set_date")){
         try{ 
            System.out.println("What would you like to set the date to? (mo-day-yr)");
            String new_date = scanner.nextLine();
            int month = Integer.parseInt(new_date.substring(0,2));
            String update = String.format("UPDATE Current_Date SET date = \'%s\', month = %d;", new_date, month);
            statement.executeUpdate(update);
            query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
            rs = statement.executeQuery(query);
            List<String> taxid = new ArrayList<String>();
            while(rs.next()){
                taxid.add(rs.getString("taxid"));
            }
            for(int i = 0; i < taxid.size(); i++){
                update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                statement.executeUpdate(update);
            }
            System.out.println("Date set to " + new_date);
           }
           catch (SQLException ex) {
            System.out.println(ex.getMessage());
           }
        }
        else{
            System.out.println("Invalid Command issued");
        }
        return currentPage;
        } catch (SQLException ex) {
                System.out.println(ex.getMessage());
        }
        return currentPage;
    }
    public static String managerScreen(String currentPage, Statement statement){
        System.out.println("\n\n------Manager Home------------------");
        System.out.println("Type \"help\" for an issue of commands!");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine(); 

        if(action.equals("help")){
            System.out.println("\nCommands\n--------\nadd interest- add some monthly interested to all market accounts  \ngen statement- Generate monthly statement for a given customer \nactive- Generate a list of all customers who traded at least 1,000 shares this month\nDTER - Generates a list of all customers who have made more than 10,000 in the last month \nreport- generate a list of all account associated with a particular customer and the current balance\ndelete- delete the list of transactions from each of the accounts\nquit- quit the program\nlogout-logout from manager account\nsecret- list of secret commands \n");
        }
        else if(action.equals("secret")){
            System.out.println("Secret Commands\n--------\nopen- open the market for the day\nclose- close the market for the day\nset_price- set the price for a given stock\nset_date- set the date");
        }
        else if(action.equals("add interest")){
            try{
                String query = String.format("SELECT taxid FROM Customers C;");
                ResultSet rs = statement.executeQuery(query);
                List<String> taxid = new ArrayList<String>();
                while(rs.next()){
                    taxid.add(rs.getString("taxid"));
                }
                for(int i = 0; i < taxid.size(); i++){
                    query = String.format("SELECT balance FROM Daily_Report D WHERE D.taxid = \'%s\';", taxid.get(i));
                    rs = statement.executeQuery(query);
                    List<Float> balance = new ArrayList<Float>();
                    while(rs.next()){
                        balance.add(rs.getFloat("balance"));
                    }
                    float total = 0;
                    for(int j = 0; j < balance.size(); j++){
                        total += balance.get(j);
                    }
                    float average = total / balance.size();
                    float interest = (0.02f/12.0f) * average;
                    int help = (int)(interest*100);
                    interest = help/100f;
                    String update = String.format("UPDATE Market_Account SET balance = balance + %f WHERE taxid = \'%s\';", interest, taxid.get(i));
                    statement.executeUpdate(update);
                    query = String.format("SELECT date, month FROM Current_Date;");
                    rs = statement.executeQuery(query);
                    rs.next();
                    String date = rs.getString("date");
                    int month = rs.getInt("month");
                    String insert = String.format("INSERT INTO Market_Transaction (transtype, taxid, amount, date, month) VALUES ('interest', \'%s\', %f, \'%s\', %d);", taxid.get(i), interest, date, month);
                    statement.executeUpdate(insert);
                }
                System.out.println("Interest added!");
            } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            }
        }
        else if(action.equals("gen statement")){
            try{
            
                System.out.print("Enter customer username: ");
                user = scanner.nextLine();
                String query = String.format("SELECT taxid, name, email FROM Customers C WHERE C.username = \'%s\';", user);
                ResultSet rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("Invalid customer.");
                    return currentPage;
                }
                else rs.next();
                String name = rs.getString("name");
                String email = rs.getString("email");
                String id = rs.getString("taxid");
                query = String.format("SELECT month, date FROM Current_Date");
                rs = statement.executeQuery(query);
                rs.next();
                int month = rs.getInt("month");
                String currentDate = rs.getString("date");
                query = String.format("SELECT transactionid, transtype, amount, date FROM Market_Transaction M WHERE M.taxid = \'%s\' AND M.month = %d;", id, month);
                rs = statement.executeQuery(query);
                List<Integer> transactionid = new ArrayList<Integer>();
                List<String> transtype = new ArrayList<String>();
                List<Float> amount = new ArrayList<Float>();
                List<String> date = new ArrayList<String>();
                if(rs.isBeforeFirst()){
                    while(rs.next()){
                        transactionid.add(rs.getInt("transactionid"));
                        transtype.add(rs.getString("transtype"));
                        amount.add(rs.getFloat("amount"));
                        date.add(rs.getString("date"));
                    }
                }
                query = String.format("SELECT transactionid, transtype, stockid, shares, buyprice, sellprice, date FROM Stock_Transaction S WHERE S.taxid = \'%s\' AND S.month = %d;", id, month);
                rs = statement.executeQuery(query);
                List<Integer> transactionid1 = new ArrayList<Integer>();
                List<String> transtype1 = new ArrayList<String>();
                List<String> stockid = new ArrayList<String>();
                List<Float> shares = new ArrayList<Float>();
                List<Float> buyprice = new ArrayList<Float>();
                List<Float> sellprice = new ArrayList<Float>();
                List<String> date1 = new ArrayList<String>();
                if(rs.isBeforeFirst()){
                    while(rs.next()){
                        transactionid1.add(rs.getInt("transactionid"));
                        transtype1.add(rs.getString("transtype"));
                        stockid.add(rs.getString("stockid"));
                        shares.add(rs.getFloat("shares"));
                        buyprice.add(rs.getFloat("buyprice")); //never null
                        sellprice.add(rs.getFloat("sellprice")); // sometimes sellprice is NULL when the transaction is a buy. 
                        date1.add(rs.getString("date"));
                    }
                }
                query = String.format("SELECT initial FROM Market_Account M WHERE M.taxid = \'%s\';", id);
                rs = statement.executeQuery(query);
                rs.next();
                float initial_balance = rs.getFloat("initial");
                query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
                rs = statement.executeQuery(query);
                rs.next();
                float final_balance = rs.getFloat("balance");
                float earnings = 0;
                for(int i = 0; i < buyprice.size(); i++){
                    if(transtype1.get(i).equals("sell")){
                        float diff = sellprice.get(i) - buyprice.get(i);
                        earnings += diff * shares.get(i);
                    }
                }
                int commissions = (transactionid.size() + transactionid1.size()) * 20;
                String table = "Name: " + name+ "\temail: " + email + "\tInital Balance: "+ initial_balance + "\tFinal Balance: " + final_balance + "\tEarnings: " + earnings + "\tComissions: " + commissions;
                System.out.println(table);
                
                System.out.println("\nMarket Transactions\n-------------------\n");

                table = "Transaction ID\t\tTransaction Type\tAmount\t\tDate";
                String row = "";
                System.out.println(table);
                for(int i = 0; i<transactionid.size();i++){
                    row += transactionid.get(i) + "\t\t\t";
                    row += transtype.get(i) + "\t\t";
                    row += amount.get(i) + "\t\t";
                    row += date.get(i) + "\n\n";
                }
                System.out.println(row);
                
                row = "";
                System.out.println("Stock Transactions\n----------------\n");
                table = "Transaction ID\tTransaction Type\tStock ID\tShares\tBuy Price\tSell Price\tDate";
                System.out.println(table);
                for(int i = 0; i<transactionid1.size();i++){
                    row += transactionid1.get(i) + "\t\t";
                    row += transtype1.get(i) + "\t\t\t";
                    row += stockid.get(i) + "\t\t";
                    row += shares.get(i) + "\t";
                    row += buyprice.get(i) + "\t\t";
                    row += sellprice.get(i) + "\t\t";
                    row += date1.get(i) + "\n";
                }
                System.out.println(row);

                
                    // FORMAT AND DISPLAY ALL LISTS, name, email, initial_balance, final_balance, earnings, commissions
                 } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                }
        }
         else if(action.equals("active")){
             try{
                String query = String.format("SELECT month FROM Current_Date;");
                ResultSet rs = statement.executeQuery(query);
                int month = rs.getInt("month");
                query = String.format("SELECT taxid, SUM(shares) as total_shares FROM Stock_Transaction S WHERE S.month = %d GROUP BY taxid HAVING total_shares >= 1000;", month);
                rs = statement.executeQuery(query);
                List<String> taxid = new ArrayList<String>();
                while(rs.next()){
                    taxid.add(rs.getString("taxid"));
                }
                for(int i = 0; i < taxid.size(); i++){
                    query = String.format("SELECT name FROM Customers C WHERE C.taxid = \'%s\';", taxid.get(i));
                    rs = statement.executeQuery(query);
                    rs.next();
                    String name = rs.getString("name"); 
                    System.out.println("Name: " + name +"\t Tax ID: " + taxid.get(i));
                    // FORMAT AND DISPLAY: taxid.get(i), name
                }
            } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            }    
        }
         else if(action.equals("DTER")){
             try{
                String query = String.format("SELECT month FROM Current_Date;");
                ResultSet rs = statement.executeQuery(query);
                int month = rs.getInt("month");
                int last_month = month - 1;
                if(month == 1)
                    last_month = 12;
                query = String.format("SELECT taxid, name, state FROM Customers C WHERE C.username <> 'admin';");
                rs = statement.executeQuery(query);
                List<String> taxid = new ArrayList<String>();
                List<String> name = new ArrayList<String>();
                List<String> state = new ArrayList<String>();
                while(rs.next()){
                    taxid.add(rs.getString("taxid"));
                    name.add(rs.getString("name"));
                    state.add(rs.getString("state"));
                }
                for(int i = 0; i < taxid.size(); i++){
                    query = String.format("SELECT transtype, shares, buyprice, sellprice FROM Stock_Transaction S WHERE S.taxid = \'%s\' AND S.month = %d;", taxid.get(i), last_month);
                    ResultSet rs1 = statement.executeQuery(query);
                    List<String> transtype = new ArrayList<String>();
                    List<Float> shares = new ArrayList<Float>();
                    List<Float> buyprice = new ArrayList<Float>();
                    List<Float> sellprice = new ArrayList<Float>();
                    while(rs1.next()){
                        transtype.add(rs1.getString("transtype"));
                        shares.add(rs1.getFloat("shares"));
                        buyprice.add(rs1.getFloat("buyprice"));
                        sellprice.add(rs1.getFloat("sellprice"));
                    }
                    float earnings = 0;
                    for(int j = 0; j < buyprice.size(); j++){
                        if(transtype.get(j).equals("sell")){
                            float diff = sellprice.get(j) - buyprice.get(j);
                            earnings += diff * shares.get(j);
                        }
                    }
                    query = String.format("SELECT amount FROM Market_Transaction M WHERE M.taxid = \'%s\' AND M.month = %d AND M.transtype = 'interest';", taxid.get(i), last_month);
                    rs1 = statement.executeQuery(query);
                    float amount = 0;
                    if(rs1.isBeforeFirst())
                        amount = rs1.getFloat("amount");
                    if(earnings + amount > 10000){
                        System.out.println("Name: " + name.get(i) +"\tID: " + taxid.get(i) + "\t State: " + state.get(i));
                    }
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } 
        }
        else if(action.equals("report")){
            try{
                System.out.print("Enter customer username: \n");
                user = scanner.nextLine();
                String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
                ResultSet rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    System.out.println("Invalid customer.");
                    return currentPage;
                }
                rs.next();
                String id = rs.getString("taxid");
                query = String.format("SELECT marketid, balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
                rs = statement.executeQuery(query);
                int marketid = rs.getInt("marketid");
                float balance = rs.getFloat("balance");
                query = String.format("SELECT shares, stockid, buyprice FROM Stock_Account S WHERE S.taxid = \'%s\';", id);
                rs = statement.executeQuery(query);
                List<Float> shares = new ArrayList<Float>();
                List<String> stockid = new ArrayList<String>();
                List<Float> buyprice = new ArrayList<Float>();
                //FORMAT AND DISPLAY: ALL LISTS as stock accounts. marketid, balance as market accounts
                System.out.println("\n"+user +"\n-------");
                System.out.println("\nMarket ID: " + id + "\tBalance: " + balance+ "\n");

                //maket account - market id and balance
                String table = "Shares\tStockID\tBuy Price";
                String row = "";
                while(rs.next()){
                    shares.add(rs.getFloat("shares"));
                    stockid.add(rs.getString("stockid"));
                    buyprice.add(rs.getFloat("buyprice"));
                }
                for(int i=0;i<shares.size();i++){
                    row += shares.get(i) + "\t";
                    row += stockid.get(i) + "\t";
                    row += buyprice.get(i) + "\n";
                }
                System.out.println(table);
                System.out.println(row);
                } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                }
            
        }
        else if(action.equals("delete")){
            try{
                System.out.print("Enter month to delete: ");
                String m = scanner.nextLine();
                int month = Integer.parseInt(m);
                String update = String.format("DELETE FROM Market_Transaction WHERE month = %d;", month);
                statement.executeUpdate(update);
                update = String.format("DELETE FROM Stock_Transaction WHERE month = %d;", month);
                statement.executeUpdate(update);
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
        else if(action.equals("open")){
            try{
                String query = String.format("SELECT month, date FROM Current_Date");
                ResultSet rs = statement.executeQuery(query);
                int month = rs.getInt("month");
                String date = rs.getString("date");
                int day = Integer.parseInt(date.substring(3,5));
                int year = Integer.parseInt(date.substring(6,10));
                int new_day = day + 1;
                int new_month = month;
                int new_year = year;
                String new_month_str = "";
                String new_day_str = "";
                String new_year_str = "";
                if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                    if(day == 31){
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        else new_month = month + 1;
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else if(month == 4 || month == 6 | month == 9 || month == 11){
                    if(day == 30)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        else new_month = month + 1;
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else if(month == 2 && year % 4 == 0){
                    if(day == 29)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        else new_month = month + 1;
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                else{
                    if(day == 28)
                    {
                        new_day = 1;
                        if(month == 12){
                            new_month = 1;
                            new_year = year++;
                        }
                        else new_month = month + 1;
                        query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                        rs = statement.executeQuery(query);
                        List<String> taxid = new ArrayList<String>();
                        while(rs.next()){
                            taxid.add(rs.getString("taxid"));
                        }
                        for(int i = 0; i < taxid.size(); i++){
                            String update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                            statement.executeUpdate(update);
                        }
                    }
                }
                if(new_month < 10)
                    new_month_str = "0" + String.valueOf(new_month);
                else new_month_str = String.valueOf(new_month);
                if(new_day < 10)
                    new_day_str = "0" + String.valueOf(new_day);
                else new_day_str = String.valueOf(new_day);
                new_year_str = String.valueOf(new_year);
                String new_date = new_month_str + "-" + new_day_str + "-" + new_year_str;
                String update = String.format("UPDATE Current_Date SET date = \'%s\', month = %d;", new_date, new_month);
                statement.executeUpdate(update);
                System.out.println("Shop open");
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else if(action.equals("close")){
            try{
                String query = String.format("SELECT date, month FROM Current_Date");
                ResultSet rs = statement.executeQuery(query);
                String date = rs.getString("date");
                int month = rs.getInt("month");
                String update = String.format("UPDATE Actor_Director_Stock SET closing = current;");
                statement.executeUpdate(update);
                query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
                rs = statement.executeQuery(query);
                List<String> taxid = new ArrayList<String>();
                while(rs.next()){
                    taxid.add(rs.getString("taxid"));
                }
                for(int i = 0; i < taxid.size(); i++){
                    query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", taxid.get(i));
                    rs = statement.executeQuery(query);
                    rs.next();
                    float balance = rs.getFloat("balance");
                    update = String.format("INSERT INTO Daily_Report (taxid, balance, date, month) VALUES (\'%s\', %f, \'%s\', %d);", taxid.get(i), balance, date, month);
                    statement.executeUpdate(update);
                }
                System.out.println("Shop closed");
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }  
        }
        else if(action.equals("set_price")){
            try{
                System.out.println("What stock would you like to set?");
                String stock = scanner.nextLine();
                System.out.println("What price would you like to set " + stock + " to?");
                String n = scanner.nextLine();
                float new_price = Float.parseFloat(n);
                String update = String.format("UPDATE Actor_Director_Stock SET current = %f WHERE stockid = \'%s\';", new_price, stock);
                statement.executeUpdate(update);
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        else if(action.equals("set_date")){
           try{ 
            System.out.println("What would you like to set the date to? (mo-day-yr)");
            String new_date = scanner.nextLine();
            int month = Integer.parseInt(new_date.substring(0,2));
            String update = String.format("UPDATE Current_Date SET date = \'%s\', month = %d;", new_date, month);
            statement.executeUpdate(update);
            String query = String.format("SELECT taxid FROM Customers C WHERE C.username <> 'admin';");
            ResultSet rs = statement.executeQuery(query);
            List<String> taxid = new ArrayList<String>();
            while(rs.next()){
                taxid.add(rs.getString("taxid"));
            }
            for(int i = 0; i < taxid.size(); i++){
                update = String.format("UPDATE Market_Account SET initial = balance WHERE taxid = \'%s\';", taxid.get(i));
                statement.executeUpdate(update);
            }
            System.out.println("Date set to " + new_date);
           }
           catch (SQLException ex) {
            System.out.println(ex.getMessage());
           }
        }
        
        else if(action.equals("quit")){
            return "quit";
        }
        else if(action.equals("logout")){
            System.out.println("Sucessfully logged out!");
            return "login";
        }
        else{
            System.out.println("Invalid Command! Type \"help\" to see a list of commands");
        }
        return currentPage;
    }
    public static String readSQLFile(String filename) {
        String str = "";
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.length() >= 2 && data.charAt(0) != '-' && data.charAt(1) != '-') {
                    str += data;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred when reading SQL file.");
            e.printStackTrace();
        }
        return str;
    }
    
}



