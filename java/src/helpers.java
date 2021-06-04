
class Helpers{
    public static void loginScreen(String[] currentPage){
        System.out.println("----------------------------------------");
        System.out.println("------- Login/Register Account -------------");
        System.out.println("-----------------------------------------\n");

        Scanner scanner = new Scanner(System.in);
        System.out.println("To login type \"login\" to register a new account please type \"register\" \n to quit please type \"quit\"\n");
        String action = scanner.nextLine(); 
        
        if(action == "quit"){return;}
        if(action == "login"){
            System.out.print("Enter Username: ");
            String user = scanner.nextLine();
              
            String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("User Not Found");
                return;
            }

            System.out.print("\nEnter Password: ");
            String password = scanner.nextLine();
            if(user == "User" && password = "secret"){
                System.out.print("\n Administrator Login Sucessful!: ");
                currentPage = "admin_page";
                return;
            }

            String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\' AND C.password = \'%s\';", user, password);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("\n Customer Login Sucecssful!: ");
                currentPage = "customer_page";
                return;
            }
            else{
                System.out.println("\n Error: Invalid Username and Password. Please Try Again ");
                return;
            }
        }
        else if(action == "register"){
            System.out.println("Enter desired username: ");
            String user = scanner.nextLine();  
            String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
            ResultSet rs = statement.executeQuery(query);
            if(rs.isBeforeFirst()){
                System.out.println("Invalid Username! User already exists");
                return;
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
                String insert = String.format("INSERT INTO Customers VALUES (\'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', \'%s\');", name, user, password, address, st, phone, email, taxid, ssn);
                statement.executeUpdate(insert);
                String insert = String.format("INSERT INTO Market_Account VALUES (\'%s\', 1000.00);", taxid);
                statement.executeUpdate(insert);
                System.out.println("Account Successfully Registered!");
                return;
            }
        }
        else{
            System.out.println("Invalid Command Issued \n To login type \"login\" to register a new account please type \"register\" \n to quit please type \"quit\"\n");
            return;
        }
    }

    //home page for customers to deposit,withdawl,buy,sell, 
    //show balance for market account, list current price of 
    //stock and actor profile and list movie info
    public static void customerScreen(String[] currentPage, String[] user){
        System.out.println("----------------------------------------");
        System.out.println("------------------Home------------------");
        System.out.println("----------------------------------------\n");
        System.out.println("\n Commands\n--------\n deposit- add money account balance\n withdrawl- subtract money from account balance\n buy_sell- purchase shares of a stock\n sell- sell shares of a stock\n accrue interest- Add money to the market account while gaining interest at the end of each month\n logout- logout from your account \n quit- exit the program");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine(); 

        String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        String id = rs.getString("taxid");

        if(action == "deposit"){
            String amount = scanner.nextLine();
            String update = String.format("UPDATE Market_Account SET balance = balance + %f WHERE taxid = \'%s\';", amount, id);
            statement.executeUpdate(update);
            String query = String.format("SELECT date FROM Current_Date;");
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            String date = rs.getString("date");
            String insert = String.format("INSERT INTO Market_Transaction VALUES ('deposit', \'%s\', %f, \'%s\');", id, amount, date);
            statement.executeUpdate(insert);
            System.out.println("Successfully depositied " + amount  +" dollars");
            return;
        }
        else if(action == "withdrawl"){
            String amount = scanner.nextLine();
            String query = String.format("SELECT taxid FROM Market_Account M WHERE M.taxid = \'%s\';", id);
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            float balance = rs.getFloat("balance");
            if(amount > balance){
                System.out.println("Error. Amount requested more than current balance.");
                return;
            }
            String update = String.format("UPDATE Market_Account SET balance = balance - %f WHERE taxid = \'%s\';", amount, id);
            statement.executeUpdate(update);
            String query = String.format("SELECT date FROM Current_Date;");
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            String date = rs.getString("date");
            String insert = String.format("INSERT INTO Market_Transaction VALUES ('withdrawl', \'%s\', %f, \'%s\');", id, amount, date);
            statement.executeUpdate(insert);
            System.out.println("Successfully withdrew " + amount  +" dollars");
            return;
        }
        else if(action == "buy_sell"){
            currentPage = "buy_sell_page";
            return;
        }
        // else if(action == "accrue interest"){
        //     ?
        //     return;
        // }

        else if(action == "quit"){
            currentPage = "exit";
        }
        else if(action == "logout"){
            System.out.println("Sucessfully logged out!")
            currentPage = "login";
            return;
        }
        else{
            System.out.println("Invalid Command issued");
            return;
        }
        

    }

    public static void buyScreen(String[] currentPage, String[] user){ // NEED TO PASS IN USERNAME
        System.out.println("----------------------------------------");
        System.out.println("-------------Buy/Sell Stocks------------");
        System.out.println("----------------------------------------\n");
        System.out.println("\n Commands\n--------\n list- view list of stocks and their prices\n purchase- purchase a specific stock \n balance- view your account balance\n  \n back- return to homepage logout- logout from your account \n quit- exit the program");
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine(); 

        String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        String id = rs.getString("taxid");

        if(action == "list"){
            System.out.print("Please enter the name of the stock you would like to view: ");
            String stock_name = scanner.nextLine();
            String query = String.format("SELECT name, dob FROM Actor_Director A WHERE A.stockid = \'%s\';", stock_name);
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            String name = rs.getString("name");
            String dob = rs.getString("dob");
            String query = String.format("SELECT role, year, value, title FROM Contracted C WHERE C.stockid = \'%s\';", stock_name);
            ResulSet rs = statement.executeQuery(query);
            rs.next();
            String role = rs.getString("role");
            String year = rs.getString("year");
            int value = rs.getInt("value");
            String title = rs.getString("title");
            // FORMAT TO PRINT TO USER
        }
        else if(action == "purchase"){
            System.out.print("Please enter the name of the stock you would like to purchase: ");
            String stock_name = scanner.nextLine();
            String query = String.format("SELECT current FROM Actor_Director_Stock A WHERE A.stockid = \'%s\';", stock_name);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("Invalid stock name.");
                return;
            }
            rs.next();
            float stock_price = rs.getFloat("current");
            System.out.println("\nAmount: ")
            String a = scanner.nextLine();
            float amount = Float.parseFloat(a);
            if(user.balance >= stock_price * amount + 20){
                String query = String.format("SELECT shares FROM Stock_Account S WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", id, stock_name, current);
                ResultSet rs = statement.executeQuery(query);
                if(!rs.isBeforeFirst()){
                    String insert = String.format("INSERT INTO Stock_Account VALUES (\'%s\', %f, \'%s\', %f);", id, amount, stock_name, stock_price);
                    statement.executeUpdate(insert);
                }
                else{
                    String update = String.format("UPDATE Stock_Account SET shares = shares + %f WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", amount, id, stock_name, current);
                    statement.executeUpdate(update);
                }
                float cost = stock_price * amount + 20;
                String update = String.format("UPDATE Market_Account SET balance = balance - %f WHERE taxid = \'%s\';", cost, id);
                statement.executeUpdate(update);
                String query = String.format("SELECT date FROM Current_Date;");
                ResultSet rs = statement.executeQuery(query);
                rs.next();
                String date = rs.getString("date");
                String insert = String.format("INSERT INTO Stock_Transaction VALUES (\'%s\', \'%s\', %f, %f, NULL, \'%s\');", id, stock_name, amount, stock_price, date);
                statement.executeUpdate(insert);
                System.out.println("Stock successfully purchased");
                return;
            }
            else{
                System.out.println("Insufficient Funds!");
                return;
            }
            return;
        }
        else if(action == "sell"){
            System.out.print("Please enter the name of the stock you would like to sell: ");
            String stock_name = scanner.nextLine();
            String o = scanner.nextLine(); // USER NEEDS TO INPUT ORIGINAL BUYING PRICE OF STOCK HE WANTS TO SELL
            float original = Float.parseFloat(o);
            String query = String.format("SELECT current FROM Actor_Director_Stock A WHERE A.stockid = \'%s\';", stock_name);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("Invalid stock name.");
                return;
            }
            rs.next();
            float stock_price = rs.getFloat("current");
            String query = String.format("SELECT shares FROM Stock_Account S WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", id, stock_name, original);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("No such stock owned at that buying price.");
                return;
            }
            System.out.println("\nAmount: ")
            String a = scanner.nextLine();
            Float amount = Float.parseFloat(a);a
            rs.next();
            float shares = rs.getFloat("shares");
            if(shares > amount){
                String update = String.format("UPDATE Stock_Account SET shares = shares - %f WHERE S.taxid = \'%s\' AND S.stockid = \'%s\' AND S.buyprice = %f;", amount, id, stock_name, original);
                statement.executeUpdate(update);
            else if (shares == amount){
                String update = String.format("DELETE FROM Stock_Account S WHERE taxid = \'%s\' AND stockid = \'%s\' AND buyprice = %f;", id, stock_name, original);
            }
            else{
                System.out.println("Insufficient Number of stock");
                return;
            }
            float profit = stock_price * amount - 20;
            String update = String.format("UPDATE Market_Account SET balance = balance + %f WHERE taxid = \'%s\';", profit, id);
            statement.executeUpdate(update);
            String query = String.format("SELECT date FROM Current_Date;");
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            String date = rs.getString("date");
            String insert = String.format("INSERT INTO Stock_Transaction VALUES (\'%s\', \'%s\', %f, %f, %f, \'%s\');", id, stock_name, amount, original, stock_price, date);
            statement.executeUpdate(insert);
            System.out.println("Stock successfully sold");
            return;
        }
        else if(action == "balance"){
            String query = String.format("SELECT taxid FROM Market_Account M WHERE M.taxid = \'%s\';", id);
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            float balance = rs.getFloat("balance");
            System.out.println("Balance: %f", balance);
        } 
        
        else if(action == "history"){
            String query = String.format("SELECT transactionid, stockid, shares, buyprice, sellprice, date FROM Stock_Transaction S WHERE S.taxid = \'%s\';", id);
            ResultSet rs = statement.executeQuery(query);
            List<String> transactionid = new ArrayList<String>();
            List<String> stockid = new ArrayList<String>();
            List<float> shares = new ArrayList<float>();
            List<float> buyprice = new ArrayList<float>();
            List<float> sellprice = new ArrayList<float>();
            List<String> date = new ArrayList<String>();
            while(rs.next()){
                transactionid.add(rs.getString("transactionid"));
                stockid.add(rs.getString("stockid"));
                shares.add(rs.getFloat("shares"));
                buyprice.add(rs.getFloat("buyprice")); //never null
                sellprice.add(rs.getFloat("sellprice")); // sometimes sellprice is NULL when the transaction is a buy. 
                date.add(rs.getString("date"));
            }
            // FORMAT THESE LISTS TO DISPLAY TO USER - table is a 6 x N
        } // added action to show transaction history

        else if(action == "quit"){
            currentPage = "exit";
        }
        else if(action == "logout"){
            system.out.println("Sucessfully logged out!")
            currentPage = "login";
            return;
        }
        else if(action == "back"){
            currentPage = "customer_page";
            return;
        }

    }
    public static void MovieScreen(String[] currentPage){
        System.out.println("----------------------------------------");
        System.out.println("-----------------Movies-----------------");
        System.out.println("----------------------------------------\n");
        System.out.println("\nCommands\n--------\n  \n info- get information on a specific movie \n  top- view list of top 5 movies from a given genre \nback - return to home_screen logout- logout from your account \n quit- exit the program \n");  
    
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();  // Read user input

        if(action == "info"){
            System.out.print("Movie title: ")
            String movie_title = scanner.nextLine();
            String query = String.format("SELECT * FROM Movies M WHERE M.title = \'%s\';", movie_title);
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            String movieid = rs.getString("movieid");
            String year = rs.getString("year");
            String genre = rs.getString("genre");
            float rating = rs.getFloat("rating");
            int revenue = rs.getInt("revenue");
            return; 
            // FORMAT INFO AND PRINT TO USER
        }
        else if(action == "top"){
            System.out.print("Starting year range: ")
            String s = scanner.nextLine();
            int start = Integer.parseInt(s);
            System.out.print("Ending year range: ")
            String e = scanner.nextLine();
            int end = Integer.parseInt(e);
            String query = String.format("SELECT title FROM Movies M WHERE M.year > %d AND M.year < %d;", start, end);
            ResultSet rs = statement.executeQuery(query);
            List<String> titles = new ArrayList<String>();
            while(rs.next()){
                titles.add(rs.getString("title"));
            }
            // FORMAT LIST TO PRINT TO USER
            return; 
        }
        else if(action == "quit"){
            currentPage = "exit";
        }
        else if(action == "logout"){
            system.out.println("Sucessfully logged out!")
            currentPage = "login";
            return;
        }
        else if(action == "back"){
            currentPage = "customer_page";
            return;
        }
    }
    
    public static void ManagerScreen(String[] currentPage){
        System.out.println("----------------------------------------");
        System.out.println("-----------------Home Page-----------------");
        System.out.println("----------------------------------------\n");
        System.out.println("\nCommands\n--------\n add interest - add some monthly interested to all market accounts  \n gen statement - Generate monthly statement for a given customer \n  active - Generate a list of all customers who traded at least 1,000 shares this month\n DTER - Generates a list of all customers who have made more than 10,000 in the last month \n report- generate a list of all account associated with a particular customer and the current balance\n delete- delete the list of transactions from each of the accounts\n exit- exit the program \n logout-logout from manager account \n");
        
        Scanner scanner = new Scanner(System.in);
        String action = scanner.nextLine();  // Read user input
        if(action == "add interest"){
            String query = String.format("SELECT taxid FROM Customers C;");
            rs = statement.executeQuery(query);
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
                    total += balance;
                }
                float average = total / balance.size();
                float interest = (0.02/12) * average
                DecimalFormat df = new DecimalFormat("##.##");
                df.setRoundingMode(RoundingMode.DOWN);
                interest = df.format(interest);
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
        }
        else if(action == "gen statement"){
            System.out.print("Enter customer username: ");
            user = scanner.nextLine();
            String query = String.format("SELECT taxid, name, email FROM Customers C WHERE C.username = \'%s\';", user);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("Invalid customer.");
            }
            rs.next();
            String name = rs.getString("name");
            String email = rs.getString("email");
            String id = rs.getString("taxid");
            query = String.format("SELECT month, date FROM Current_Date");
            ResultSet rs = statement.executeQuery(query);
            int month = rs.getInt("month");
            String currentDate = rs.getString("date");
            query = String.format("SELECT transactionid, transtype, amount, date FROM Market_Transaction M WHERE M.taxid = \'%s\' AND M.month = %d;", id, month);
            ResultSet rs = statement.executeQuery(query);
            List<Integer> transactionid = new ArrayList<Integer>();
            List<String> transtype = new ArrayList<String>();
            List<Float> amount = new ArrayList<Float>();
            List<String> date = new ArrayList<String>();
            while(rs.next()){
                transactionid.add(rs.getString("transactionid"));
                transtype.add(rs.getString("transtype"));
                amount.add(rs.getFloat("amount"));
                date.add(rs.getString("date"));
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
            while(rs.next()){
                transactionid1.add(rs.getString("transactionid"));
                transtype1.add(rs.getString("transtype"));
                stockid.add(rs.getString("stockid"));
                shares.add(rs.getFloat("shares"));
                buyprice.add(rs.getFloat("buyprice")); //never null
                sellprice.add(rs.getFloat("sellprice")); // sometimes sellprice is NULL when the transaction is a buy. 
                date1.add(rs.getString("date"));
            }
            query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", id);
            rs = statement.executeQuery(query);
            rs.next();
            float final_balance = rs.getFloat("balance");
            String first_day = currentDate.substring(0, 3) + "01" + currentDate.substring(5, 10);
            query = String.format("SELECT balance FROM Daily_Report D WHERE D.taxid = \'%s\' AND D.date = \'%s\';", id, first_day);
            rs = statement.executeQuery(query);
            float initial_balance = final_balance;
            if(rs.isBeforeFirst()){
                rs.next();
                initial_balance = rs.getFloat("balance");
            }
            float earnings = 0;
            for(int i = 0; i < buyprice.size(); i++){
                if(transtype1.get(i).equals('sell')){
                    float diff = sellprice.get(i) - buyprice.get(i);
                    earnings += diff * shares.get(i);
                }
            }
            int commissions = (transactionid.size() + transactionid1.size()) * 20;
            // FORMAT AND DISPLAY ALL LISTS, name, email, initial_balance, final_balance, earnings, commissions
        }
         else if(action == "active"){
            String query = String.format("SELECT month FROM Current_Date");
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
                String name = rs.getString("name"); 
                // FORMAT AND DISPLAY: taxid.get(i), name
            }
        }
         else if(action == "DTER"){
            String query = String.format("SELECT month FROM Current_Date");
            ResultSet rs = statement.executeQuery(query);
            int month = rs.getInt("month");
            int last_month = month - 1;
            if(month == 1)
                last_month = 12;
            query = String.format("SELECT taxid, name, state FROM Customers C");
            rs = statement.executeQuery(query);
            while(rs.next()){
                String id = rs.getString("taxid");
                query = String.format("SELECT shares, buyprice, sellprice FROM Stock_Transaction S WHERE S.taxid = \'%s\' AND S.month = %d;", id, last_month);
                ResultSet rs1 = statement.executeQuery(query);
                List<Float> shares = new ArrayList<Float>();
                List<Float> buyprice = new ArrayList<Float>();
                List<Float> sellprice = new ArrayList<Float>();
                while(rs1.next()){
                    shares.add(rs1.getFloat("shares"));
                    buyprice.add(rs1.getFloat("buyprice"));
                    sellprice.add(rs1.getFloat("sellprice"));
                }
                float earnings = 0;
                for(int i = 0; i < buyprice.size(); i++){
                    if(transtype1.get(i).equals('sell')){
                        float diff = sellprice.get(i) - buyprice.get(i);
                        earnings += diff * shares.get(i);
                    }
                }
                query = String.format("SELECT amount FROM Market_Transaction M WHERE M.taxid = \'%s\' AND M.month = %d AND M.transtype = 'interest';", id, last_month);
                float amount = rs.getFloat("amount");
                if(earnings + amount > 10000){
                    String name = rs.getString("name");
                    String state = rs.getString("state");
                    //FORMAT AND DISPLAY: name, id, state
                }
            }
        }
        else if(action == "report"){
            System.out.print("Enter customer username: ");
            user = scanner.nextLine();
            String query = String.format("SELECT taxid FROM Customers C WHERE C.username = \'%s\';", user);
            ResultSet rs = statement.executeQuery(query);
            if(!rs.isBeforeFirst()){
                System.out.println("Invalid customer.");
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
            List<String> stockid = new ArrayList<Float>();
            List<Float> buyprice = new ArrayList<Float>();
            while(rs.next()){
                shares.add(rs.getFloat("shares"));
                stockid.add(rs.getString("stockid"));
                buyprice.add(rs.getFloat("buyprice"));
            }
            //FORMAT AND DISPLAY: ALL LISTS as stock accounts. marketid, balance as market accounts
        }
        else if(action == "delete"){
            System.out.print("Enter month to delete: ");
            int  scanner.nextLine();
            Stri= String.format("DELETE FROM Market_Transaction M WHERE M.month = %d;", month);
            statement.executeUpdate(update);
            String update = String.format("DELETE FROM Stock_Transaction S WHERE S.month = %d;", month);
            statement.executeUpdate(update);
        }

        else if(action == "open"){
            String query = String.format("SELECT month, date FROM Current_Date");
            ResultSet rs = statement.executeQuery(query);
            int month = rs.getInt("month");
            String date = rs.getString("date");
            int day = Integer.parseInt(date.substring(3,5));
            int year = Integer.parseInt(date.substring(6,10));
            int new_day = day++;
            int new_month = month;
            int new_year = year;
            String nr(F  w b | month == 9 || month == 11){
y                aly_Rp DD.balan,D.dae, D.month = %dbaladae, mnth
                        new_day = 1;
                        if(month == 12){
            }            new_month = 1;
                        new_year = year++;
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
                }
            }
            if(new_month < 10)
                new_month_str = "0" + String.valueof(new_month);
            else new_month_str = String.valueof(new_month);
            if(new_day < 10)
                new_day_str = "0" + String.valueof(new_day);
            else new_day_str = String.valueof(new_day);
            new_year_str = String.valueof(new_year);
            String new_date = new_month_str + "-" + new_day_str + "-" + new_year_str;
            String update = String.format("UPDATE Current_Date SET date = \'%s\', month = %d;", new_date, new_month);
            statement.executeUpdate(update);
        }

        else if(action == "close"){
            String query = String.format("SELECT date, month FROM Current_Date");
            ResultSet rs = statement.executeQuery(query);
            String date = rs.getString("date");
            int month = rs.getInt("month");
            String update = String.format("UPDATE Actor_Director_Stock A SET A.closing = A.current";)
            statement.executeUpdate(update);
            query = String.format("SELECT taxid FROM Customers C";);
            ResultSet rs = statement.executeQuery(query);
            List<String> taxid = new ArrayList<String>();
            while(rs.next()){
                taxid.add(rs.getString("taxid"));
            }
            for(int i = 0; i < taxid.size(); i++){
                query = String.format("SELECT balance FROM Market_Account M WHERE M.taxid = \'%s\';", taxid.get(i));
                rs = statement.executeQuery(query);
                rs.next();
                float balance = rs.getFloat("balance");
                balance.add(rs.getFloat("balance"));
                update = String.format("UPDATE Daily_Report D SET D.balance = %f, D.date = \'%s\', D.month = %d;", balance, date, month);
                statement.executeUpdate(update);
            }
        }

        else if(action == "new price")

        else if(action == "quit"){
            system.out.println("Quiting program")
            currentPage = "exit";
        }
        else if(action == "logout"){
            system.out.println("Sucessfully logged out!")
            currentPage = "login";
        }
        
    }
    