//Garrett's Popcorn Vending Machine
//Comp 170 700 Fall 2025
//Window's 10
// J. Molas

import java.util.InputMismatchException;
import java.util.Scanner;
public class GarrettsPopcornVendingMachine {

//Use of constant final variables for data that will not change.
    private static final int NUM_ROWS = 3;
    private static final int NUM_COLS = 3;
    private static final int INITIAL_STOCK = 5;
    private static final String SENTINEL_VALUE = "-1";

//Use of multi-dimensional arrays to structure data.
    // Parallel arrays for product names and prices.
    private static final String[][] PRODUCT_NAMES = {
        {"Garrett Mix", "Pecan Carmel Crisp",   "Plain"},
        {"Caramel Crisp",   "Cashew Carmel Crisp",  "Buttery"},
        {"Cheese Corn", "Almond Carmel Crisp",  "Sweet Corn"}
    };

    private static final double[][] PRODUCT_PRICES = {
        {14.99, 10.99, 6.99},
        {16.99, 9.99, 8.99},
        {12.99, 11.99, 7.99}
    };

//A multi-dimensional array to track the stock level of each product.
    private static int[][] productStock = new int[NUM_ROWS][NUM_COLS];
    
    //Arrays to store restock information, replacing the HashMap.
    private static int[][] restockAmount = new int[NUM_ROWS][NUM_COLS];
    private static double[][] restockCost = new double[NUM_ROWS][NUM_COLS];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Variables properly declared and initialized.
        double totalCost = 0.0;
        int totalItems = 0;
        String inputChoice;

        //Initialize all products with the initial stock level and restock arrays to zero.
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                productStock[i][j] = INITIAL_STOCK;
                restockAmount[i][j] = 0;
                restockCost[i][j] = 0.0;
            }
        }

        //Display the welcome message and product chart.
        System.out.println("Welcome to Garrett's Popcorn Vending Machine!");
        System.out.println("Please select an item by entering the product row (0-2) and column letter ( P,  N,  R).");
        System.out.println("Enter -1 to quit and see your total.");

        //Proper structure for continuous input.
        do {
            displayProductChart();
            System.out.print("\nEnter your selection (e.g., '0P'): ");
            inputChoice = scanner.nextLine().trim().toUpperCase();

            //Check if the user wants to quit.
            if (inputChoice.equals(SENTINEL_VALUE)) {
                break;
            }

            //Requirement 2: Validate the input.
            if (inputChoice.length() != 2) {
                System.out.println("\nInvalid input. Please enter a row number (0-2) and a column letter (P, N, R).\n");
                continue;
            }

            //Parse the input row and column.
            int row;
            char colChar;
            try {
                row = Integer.parseInt(String.valueOf(inputChoice.charAt(0)));
                colChar = inputChoice.charAt(1);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. The first character must be a number.\n");
                continue;
            }

            int colIndex;
            switch (colChar) {
                case 'P':
                    colIndex = 0;
                    break;
                case 'N':
                    colIndex = 1;
                    break;
                case 'R':
                    colIndex = 2;
                    break;
                default:
                    System.out.println("\nInvalid column letter. Please use 'P', 'N', or 'R'.\n");
                    continue;
            }

            //Validate that the row number is within bounds.
            if (row < 0 || row >= NUM_ROWS) {
                System.out.println("\nInvalid row number. Please use 0, 1, or 2.\n");
                continue;
            }

            //Extra Credit: Check stock level before purchase.
            if (productStock[row][colIndex] <= 0) {
                System.out.println("\nSorry, '" + PRODUCT_NAMES[row][colIndex] + "' is currently out of stock. Please make another selection.\n");
                continue;
            }

            //Process the purchase.
            String productName = PRODUCT_NAMES[row][colIndex];
            double productPrice = PRODUCT_PRICES[row][colIndex];
            
            //Decrement the stock.
            productStock[row][colIndex]--;

            totalCost += productPrice;
            totalItems++;

//Display each item and its cost for each iteration.
            System.out.printf("\nYour selection: %s (%s, $%.2f) has been added to your order.\n", productName, inputChoice, productPrice);
            System.out.println("Current order total: " + totalItems + " items for a total of $" + String.format("%.2f", totalCost) + "\n");

            //Check if restock is needed.
            //Check if stock is below 3 and it's not already flagged for restock.
            if (productStock[row][colIndex] < 3 && restockAmount[row][colIndex] == 0) {
                int restockAmountValue = INITIAL_STOCK - productStock[row][colIndex];
                double restockCostValue = restockAmountValue * (productPrice * 0.5);
                restockAmount[row][colIndex] = restockAmountValue;
                restockCost[row][colIndex] = restockCostValue;
            }

        } while (true);

//Provide a final summary.
        System.out.println("\n*********************************");
        System.out.println("Summary of Items Purchased");
        System.out.println("*********************************");
        System.out.println(totalItems + " items purchased for a total cost of $" + String.format("%.2f", totalCost));
        System.out.println("\n*********************************");

        //Display restock information.
        boolean restockFlag = false;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (restockAmount[i][j] > 0) {
                    if (!restockFlag) {
                         System.out.println("\nItem(s) sent for Restock");
                         restockFlag = true;
                    }
                    System.out.printf("   - %d %s (Restock cost: $%.2f)\n", restockAmount[i][j], PRODUCT_NAMES[i][j], restockCost[i][j]);
                }
            }
        }

        if (restockFlag) {
            System.out.println("\n*********************************");
        }

        scanner.close();
    }


//Displays the product chart to the console.
    public static void displayProductChart() {
        System.out.println("\nProduct Row/Column\t\t\tP\t\t\t\tN\t\t\t\tR");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        for (int i = 0; i < NUM_ROWS; i++) {
            System.out.printf("%d\t\t\t%s ($%.2f)\t%s ($%.2f)\t%s ($%.2f)\n",
                i,
                PRODUCT_NAMES[i][0], PRODUCT_PRICES[i][0],
                PRODUCT_NAMES[i][1], PRODUCT_PRICES[i][1],
                PRODUCT_NAMES[i][2], PRODUCT_PRICES[i][2]);
        }
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }
}