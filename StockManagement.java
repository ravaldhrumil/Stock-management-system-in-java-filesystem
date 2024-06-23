import java.util.*;
import java.io.*;

class Stock {
    String name;
    double price;
    int quantity;

    Stock(String name, double price, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        String s = this.name + "," + this.price + "," + this.quantity;
        return s;
    }
}

public class StockManagement {

    static HashMap<String, Stock> stocks = new HashMap<>();
    static HashMap<String, Stock> portfolio = new HashMap<>();
    static double finalbalance = 1000000000.00;
    static String stock_fp = "stocks.txt";
    static String watchlist_fp = "watchlist.txt";
    static String history_fp = "history.txt";

    public static void loadWatchlist() {
        try {
            File f = new File(watchlist_fp);
            FileReader watchFileReader = new FileReader(f);
            BufferedReader watBufferedReader = new BufferedReader(watchFileReader);

            String line = watBufferedReader.readLine();

            while (line != null) {
                String[] arr = line.split(",");
                stocks.put(arr[0], new Stock(arr[0], Double.parseDouble(arr[1]), Integer.parseInt(arr[2])));
                line = watBufferedReader.readLine();
            }
            watBufferedReader.close();

        } catch (Exception e) {
            System.out.println("Error loading watchlist data, " + e);
        }
    }

    public static void loadStocks() {
        try {
            File f = new File(stock_fp);
            FileReader watchFileReader = new FileReader(f);
            BufferedReader watBufferedReader = new BufferedReader(watchFileReader);

            String line = watBufferedReader.readLine();

            while (line != null) {
                String[] arr = line.split(",");
                stocks.put(arr[0], new Stock(arr[0], Double.parseDouble(arr[1]), Integer.parseInt(arr[2])));
                line = watBufferedReader.readLine();
            }
            watBufferedReader.close();

        } catch (Exception e) {
            System.out.println("Error loading stocks data, " + e);
        }
    }

    @SuppressWarnings("rawtypes")
    public static void displayStocks() {
        loadWatchlist();
        Set set = stocks.keySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    @SuppressWarnings("rawtypes")
    public static void buyStock(String name, int quantity) {
        loadStocks();
        Set set = stocks.keySet();
        Iterator itr = set.iterator();
        boolean flag = false;
        while (itr.hasNext()) {
            Stock s = stocks.get(itr.next());
            if (s.name.equals(name)) {
                double totalPrice = s.price * quantity;
                if (totalPrice > finalbalance) {
                    System.out.println("Insufficien fund balance to but " + quantity + " of " + name);
                }

                else {
                    finalbalance -= totalPrice;
                    portfolio.put(name, new Stock(name, s.price, quantity));
                    saveOrderHistory(name, quantity, s.price, "BUY");
                }
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("Doesnot exist");
        }
    }

    public static void saveOrderHistory(String name, int quantity, double price, String type) {
        File f = new File(history_fp);
        try {
            FileWriter histFileWriter = new FileWriter(f, true);
            BufferedWriter hisBufferedWriter = new BufferedWriter(histFileWriter);
            hisBufferedWriter.write("\n" + type + ": " + name + ", Quantity: " + quantity + ", Price: " + price);
            hisBufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error writing history " + e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public static void displayPortfolio() {
        Iterator itr = portfolio.keySet().iterator();
        double portfolioValue = 0.0;
        System.out.println("\n\n\t\tQuantity\t\tName\t\tTotal value");
        System.out.println("---------------------------------------------------------------------------");
        while (itr.hasNext()) {
            Stock s = portfolio.get(itr.next());
            double currentStockValue = s.quantity * s.price;
            System.out.println("\t\t" + s.quantity + "\t\t\t" + s.name + "\t\t" + currentStockValue);
            portfolioValue += currentStockValue;
        }
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("\t\tYour total portfolio value is: " + portfolioValue);
    }

    @SuppressWarnings("rawtypes")
    public static void sellStocks(String name, int quantity) {
        Iterator itr = portfolio.keySet().iterator();
        boolean flag = false;
        while (itr.hasNext()) {
            Stock s = portfolio.get(itr.next());
            if (s.name.equals(name)) {
                flag = true;
                if (s.quantity < quantity) {
                    System.out.println("You tried to sell " + quantity + " stocks of " + name + " but you only have "
                            + s.quantity + " shares");
                } else {
                    finalbalance += (quantity * s.price);
                    saveOrderHistory(name, quantity, s.price, "Sell");
                    if (s.quantity - quantity == 0) {
                        itr.remove();
                    } else {
                        s.quantity -= quantity;
                    }
                }
            }
        }

        if (!flag) {
            System.out.println("This stock doesnot exist in you portfolio!!!");
        }
    }

    public static void displayOrderHistory() {
        try {
            File f = new File(history_fp);
            FileReader histFileReader = new FileReader(f);
            BufferedReader hisBufferedReader = new BufferedReader(histFileReader);
            String line = hisBufferedReader.readLine();
            while (line != null) {
                System.out.println(line);
                line = hisBufferedReader.readLine();
            }
            hisBufferedReader.close();
        } catch (Exception e) {
            System.out.println("Error loading History " + e);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(
                    "\n\nPress 1 to display stocks\nPress 2 to buy stocks\nPress 3 to display portfolio\nPress 4 to sell stocks\nPress 5 to see history\nPress 6 to exit the system\n");
            try {

                System.out.print("Please enter choice: ");
                int ch = sc.nextInt();

                switch (ch) {
                    case 1:
                        displayStocks();
                        break;

                    case 2:
                        sc.nextLine();
                        System.out.print("\nPlease enter stock name: ");
                        String name = sc.nextLine();
                        try {
                            System.out.print("\nPlease enter stock quantity: ");
                            int quantity = sc.nextInt();
                            buyStock(name, quantity);
                        } catch (Exception e) {
                            System.out.println("\nYou cannot enter anythin except integer for quantity!!!");
                            sc.nextLine();
                        }
                        break;

                    case 3:
                        displayPortfolio();
                        break;

                    case 4:
                        sc.nextLine();
                        System.out.print("\nPlease enter stock name: ");
                        String sellName = sc.nextLine();
                        try {
                            System.out.print("\nPlease enter stock quantity: ");
                            int sellQuantity = sc.nextInt();
                            sellStocks(sellName, sellQuantity);
                        } catch (Exception e) {
                            System.out.println("\nYou cannot enter anythin except integer for quantity!!!");
                            sc.nextLine();
                        }
                        break;

                    case 5:
                        displayOrderHistory();
                        break;

                    case 6:
                        sc.close();
                        System.out.println("\n\n\n\t\tThank you for using our system!!!!!\n\n");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("\nPlease enter correct choice!!!");
                        break;
                }
            } catch (Exception e) {
                System.out.println("\nYou can only enter integer value in choice!!");
                sc.nextLine();
            }
        }
    }
}