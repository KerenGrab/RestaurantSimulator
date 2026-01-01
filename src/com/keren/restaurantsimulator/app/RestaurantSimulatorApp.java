package com.keren.restaurantsimulator.app;

import com.keren.restaurantsimulator.enums.OrderStatus;
import com.keren.restaurantsimulator.enums.TableStatus;
import com.keren.restaurantsimulator.model.Dish;
import com.keren.restaurantsimulator.model.Menu;
import com.keren.restaurantsimulator.model.Order;
import com.keren.restaurantsimulator.model.Restaurant;
import com.keren.restaurantsimulator.model.Table;
import com.keren.restaurantsimulator.service.BillingService;
import com.keren.restaurantsimulator.service.KitchenService;
import com.keren.restaurantsimulator.service.OrderService;
import com.keren.restaurantsimulator.service.TableService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RestaurantSimulatorApp {

    // For demo: map tableId -> orderId (because Order doesn't store its id)
    private final Map<Integer, Integer> orderIdByTableId = new HashMap<>();

    private final Restaurant restaurant;
    private final TableService tableService;
    private final BillingService billingService;
    private final OrderService orderService;
    private final KitchenService kitchenService;

    public RestaurantSimulatorApp(Restaurant restaurant) {
        if (restaurant == null) throw new IllegalArgumentException("restaurant cannot be null");

        this.restaurant = restaurant;

        this.tableService = new TableService(restaurant);
        this.billingService = new BillingService(restaurant);

        this.orderService = new OrderService();
        this.kitchenService = new KitchenService(orderService);
    }

    public static void main(String[] args) {
        Restaurant restaurant = DemoData.createDemoRestaurant();
        RestaurantSimulatorApp app = new RestaurantSimulatorApp(restaurant);
        app.run();
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        try {
            printWelcome();
            printHelp();

            while (true) {
                System.out.print("\n> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                String cmd = parts[0].toLowerCase();

                try {
                    switch (cmd) {
                        case "help" -> printHelp();
                        case "exit" -> {
                            System.out.println("Bye!");
                            return;
                        }

                        case "test" -> runQuickTests();

                        case "menu" -> printMenu();
                        case "tables" -> printTables();
                        case "table" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            printTableDetails(tableId);
                        }

                        case "seat" -> {
                            requireArgs(parts, 3);
                            int tableId = Integer.parseInt(parts[1]);
                            int customers = Integer.parseInt(parts[2]);
                            seat(tableId, customers);
                        }

                        case "open" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            openOrder(tableId);
                        }

                        case "add" -> {
                            requireArgs(parts, 4);
                            int tableId = Integer.parseInt(parts[1]);
                            int dishId = Integer.parseInt(parts[2]);
                            int qty = Integer.parseInt(parts[3]);
                            addDish(tableId, dishId, qty);
                        }

                        case "submit" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            submitToKitchen(tableId);
                        }

                        case "prep" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            startPrep(tableId);
                        }

                        case "ready" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            markReady(tableId);
                        }

                        case "served" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            markServed(tableId);
                        }

                        case "bill" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            requestBill(tableId);
                        }

                        case "pay" -> {
                            requireArgs(parts, 2);
                            int tableId = Integer.parseInt(parts[1]);
                            pay(tableId);
                        }

                        default -> System.out.println("Unknown command. Type 'help'.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } finally {
            sc.close();
        }
    }

    // ---------------- Commands ----------------

    private void printWelcome() {
        System.out.println("=== Restaurant Simulator ===");
        System.out.println("Type 'help' to see commands.");
    }

    private void printHelp() {
        System.out.println("""
                Commands:
                  help                         - show commands
                  exit                         - quit
                  test                         - run quick sanity tests

                  menu                         - print menu
                  tables                       - list all tables (status)
                  table <tableId>              - table details (status + current order)

                  seat <tableId> <customers>   - seat customers (sets table OCCUPIED)
                  open <tableId>               - open new order for an occupied table
                  add <tableId> <dishId> <qty> - add dish to current order

                  submit <tableId>             - submit table's order to kitchen (creates orderId)
                  prep <tableId>               - start preparing (SUBMITTED -> IN_PREP)
                  ready <tableId>              - mark ready (IN_PREP -> READY)
                  served <tableId>             - mark served (READY -> SERVED)

                  bill <tableId>               - request bill (sets WAITING_FOR_BILL)
                  pay <tableId>                - pay and clear table (sets FREE)
                """);
    }

    private void printMenu() {
        Menu menu = restaurant.getMenu();
        System.out.println("Menu:");
        for (Dish d : menu.listDishes()) {
            System.out.println("  " + d.getId() + ") " + d.getName()
                    + " | ₪" + d.getPrice()
                    + " | prep " + d.getPrepTimeMinutes() + "m");
        }
    }

    private void printTables() {
        System.out.println("Tables:");
        for (Table t : restaurant.listTables()) {
            System.out.println("  Table " + t.getTableNumber()
                    + " | capacity=" + t.getCapacity()
                    + " | status=" + t.getStatus()
                    + (t.hasOrder() ? " | hasOrder" : ""));
        }
    }

    private void printTableDetails(int tableId) {
        Table t = restaurant.getTableById(tableId);
        System.out.println("Table " + t.getTableNumber()
                + " | capacity=" + t.getCapacity()
                + " | status=" + t.getStatus());

        if (!t.hasOrder()) {
            System.out.println("No active order.");
            return;
        }

        Order order = t.getOrder();
        System.out.println(order);

        Integer orderId = orderIdByTableId.get(tableId);
        if (orderId != null) {
            System.out.println("Order ID: " + orderId + " | Status: " + order.getStatus());
        } else {
            System.out.println("Order not submitted yet. Status: " + order.getStatus());
        }
    }

    private void seat(int tableId, int customers) {
        Table table = restaurant.getTableById(tableId);
        table.seat(customers);
        System.out.println("Seated " + customers + " customers at table " + tableId + ".");
    }

    private void openOrder(int tableId) {
        Table table = restaurant.getTableById(tableId);
        table.openNewOrder();
        orderIdByTableId.remove(tableId); // new order, old mapping no longer relevant
        System.out.println("Opened new order for table " + tableId + ".");
    }

    private void addDish(int tableId, int dishId, int qty) {
        Table table = restaurant.getTableById(tableId);
        if (!table.hasOrder()) {
            throw new IllegalStateException("Table " + tableId + " has no active order. Use: open <tableId>");
        }

        Dish dish = restaurant.getMenu().getDishOrThrow(dishId);
        table.getOrder().addDish(dish, qty);

        System.out.println("Added " + qty + " x " + dish.getName() + " to table " + tableId + ".");
    }

    private void submitToKitchen(int tableId) {
        Table table = restaurant.getTableById(tableId);
        if (!table.hasOrder()) {
            throw new IllegalStateException("Table " + tableId + " has no active order");
        }

        Order order = table.getOrder();

        int orderId = orderService.addOrder(order);
        orderIdByTableId.put(tableId, orderId);

        kitchenService.submitOrderToKitchen(orderId);

        System.out.println("Submitted order " + orderId + " to kitchen for table " + tableId + ".");
    }

    private void startPrep(int tableId) {
        int orderId = getOrderIdForTableOrThrow(tableId);
        kitchenService.startPreparing(orderId);
        System.out.println("Order " + orderId + " is now IN_PREP.");
    }

    private void markReady(int tableId) {
        int orderId = getOrderIdForTableOrThrow(tableId);
        kitchenService.orderIsReady(orderId);
        System.out.println("Order " + orderId + " is READY.");
    }

    private void markServed(int tableId) {
        billingService.markServed(tableId);
        System.out.println("Table " + tableId + " marked as SERVED.");
    }

    private void requestBill(int tableId) {
        billingService.requestBill(tableId);
        double total = billingService.calculateBill(tableId);
        System.out.println("Bill requested for table " + tableId + ". Total = ₪" + total);
    }

    private void pay(int tableId) {
        Table table = restaurant.getTableById(tableId);
        if (table.getStatus() != TableStatus.WAITING_FOR_BILL) {
            throw new IllegalStateException("Table " + tableId + " must be WAITING_FOR_BILL before paying (use: bill <tableId>)");
        }

        double total = billingService.pay(tableId);
        orderIdByTableId.remove(tableId);

        System.out.println("Paid ₪" + total + ". Table " + tableId + " is now FREE.");
    }

    // ---------------- Quick Tests ----------------

    /**
     * Small, dependency-free sanity tests.
     * Run from the CLI by typing: test
     */
    private void runQuickTests() {
        System.out.println("Running quick tests...");

        // Choose a table we can "own" during tests.
        int testTableId = 5;
        Table table = restaurant.getTableById(testTableId);

        // Reset table to a known state
        table.clear();
        orderIdByTableId.remove(testTableId);

        // Test 1: menu has dish #1, name not empty
        Dish dish1 = restaurant.getMenu().getDishOrThrow(1);
        assertTrue(dish1.getName() != null && !dish1.getName().isBlank(), "Dish #1 should have a non-empty name");

        // Test 2: seat -> OCCUPIED
        table.seat(2);
        assertEquals(TableStatus.OCCUPIED, table.getStatus(), "Table should be OCCUPIED after seat()");

        // Test 3: open order -> hasOrder + bill 0
        table.openNewOrder();
        assertTrue(table.hasOrder(), "Table should have an order after openNewOrder()");
        assertEquals(0.0, table.getBill(), "New order bill should start at 0");

        // Test 4: add dish -> bill changes
        table.getOrder().addDish(dish1, 2); // demo dish 1 price = 48.0 -> expected 96.0
        assertEquals(96.0, table.getBill(), "Bill after adding 2 of dish #1 should be 96.0 (based on demo data)");

        // Test 5: kitchen status flow
        int orderId = orderService.addOrder(table.getOrder());
        orderIdByTableId.put(testTableId, orderId);

        kitchenService.submitOrderToKitchen(orderId);
        assertEquals(OrderStatus.SUBMITTED, table.getOrder().getStatus(), "After submit, status should be SUBMITTED");

        kitchenService.startPreparing(orderId);
        assertEquals(OrderStatus.IN_PREP, table.getOrder().getStatus(), "After startPreparing, status should be IN_PREP");

        kitchenService.orderIsReady(orderId);
        assertEquals(OrderStatus.READY, table.getOrder().getStatus(), "After orderIsReady, status should be READY");

        // Test 6: served + bill + pay clears table
        billingService.markServed(testTableId);
        assertEquals(OrderStatus.SERVED, table.getOrder().getStatus(), "After markServed, status should be SERVED");

        billingService.requestBill(testTableId);
        assertEquals(TableStatus.WAITING_FOR_BILL, table.getStatus(), "After requestBill, table should be WAITING_FOR_BILL");

        double paid = billingService.pay(testTableId);
        assertEquals(96.0, paid, "Paid amount should equal the bill (96.0)");

        assertEquals(TableStatus.FREE, table.getStatus(), "After pay, table should be FREE");
        assertTrue(!table.hasOrder(), "After pay, table should have no order");

        // Clean up mapping
        orderIdByTableId.remove(testTableId);

        System.out.println("✅ All quick tests passed!");
    }

    // ---------------- Helpers ----------------

    private int getOrderIdForTableOrThrow(int tableId) {
        Integer orderId = orderIdByTableId.get(tableId);
        if (orderId == null) {
            throw new IllegalStateException("Table " + tableId + " has no submitted order. Use: submit <tableId>");
        }
        return orderId;
    }

    private void requireArgs(String[] parts, int n) {
        if (parts.length < n) {
            throw new IllegalArgumentException("Not enough arguments. Type 'help'.");
        }
    }

    // Tiny assertions (no JUnit needed)
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException("TEST FAILED: " + message);
        }
    }

    private void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new IllegalStateException("TEST FAILED: " + message + " (expected=" + expected + ", actual=" + actual + ")");
    }

    private void assertEquals(double expected, double actual, String message) {
        double eps = 0.0001;
        if (Math.abs(expected - actual) <= eps) return;
        throw new IllegalStateException("TEST FAILED: " + message + " (expected=" + expected + ", actual=" + actual + ")");
    }
}
