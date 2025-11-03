package org.collexio.cli;

import org.checkerframework.checker.units.qual.C;
import org.collexio.domain.*;
import org.collexio.persistence.*;
import org.collexio.utilities.Utilities;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class CLI {
    private static final Scanner sc = new Scanner(System.in);
    private final DBItemCollectionDAO collectionDAO;
    private final DBTransactionDAO transactionDAO;
    private final DBItemDAO itemDAO;
    private final DBItemPhotoDAO itemPhotoDAO;
    private final DateTimeFormatter formatter;
    private  final Connection connection;

    public CLI (Connection connection) throws SQLException, IOException {
        collectionDAO = new DBItemCollectionDAO(connection);
        itemDAO = new DBItemDAO(connection);
        transactionDAO = new DBTransactionDAO(connection);
        itemPhotoDAO = new DBItemPhotoDAO(connection);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.connection = connection;
    }

    public void showCollections() {
        try {
            List<ItemCollection> collections = collectionDAO.getAll();
            if (collections.isEmpty())
                System.out.println("There are no collections");
            else
                collections.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
        System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void addCollection() {
        try {
            System.out.println("Enter the collection name");
            String name = sc.nextLine().trim();
            collectionDAO.add(new ItemCollection(name));
            System.out.println("Collection added!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void insertItem() { //TODO UPLOAD PHOTO
        try {
            System.out.print("Enter the collection id: ");
            long collectionId = Long.parseLong(sc.nextLine().trim());
            Optional<ItemCollection> collection = collectionDAO.get(collectionId);
            if (collection.isEmpty())
                throw new IllegalArgumentException("Collection with this id doesn't exist");
            System.out.print("Enter the item type (0-Plant,1-TechItem,2-Book): ");
            int type = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter the item name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter the item quantity: ");
            int quantity = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter the item description: ");
            String description = sc.nextLine().trim();
            System.out.print("Enter the item scientific_name (not needed for tech items and books): ");
            String second_name = sc.nextLine().trim();

            System.out.print("Enter the photo path: ");
            String path = sc.nextLine().trim();
            System.out.print("Enter date and hour (ex. 2025-11-02): ");
            String input = sc.nextLine().trim();
            LocalDate date = LocalDate.parse(input, formatter);

            ItemPhoto photo = new ItemPhoto(Paths.get(path), date);
            Item item = switch (type) {
                case 0 -> new Plant(name, quantity, photo, description, second_name);
                case 1 -> new TechItem(name, quantity, photo, description);
                case 2 -> new Book(name, quantity, photo, description);
                default -> throw new IllegalArgumentException("Invalid item type");
            };
            itemDAO.add(item, collectionId);
            photo = itemPhotoDAO.getLast().get();
            Utilities.uploadPhoto(photo, connection);
            System.out.println("Item added!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        } catch (IOException e) {
        System.out.println("IO error: " + e.getMessage());
    }
    }

    public void showCollectionItems() {
        try {
            System.out.print("Enter the collection id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            ItemCollection collection = collectionDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Collection with this id doesn't exist"));
            List<Item> items = collection.getData();
            System.out.println("Collection " + id + ", name: " + collection.getName());
            System.out.println("Total item: " + collection.getTotalQuantity());
            System.out.println("Items:");
            items.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void showItemInfo() {
        try {
            System.out.print("Enter the item id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            Item item = itemDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Item with this id doesn't exist"));
            System.out.println("Item info:");
            System.out.println(item);
            if (item instanceof Priceable) {
                Priceable o = (Priceable)item;
                System.out.println(o.getPrice());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void deleteCollection() {
        try {
            System.out.print("Enter the collection id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            ItemCollection collection = collectionDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Collection with this id doesn't exist"));
            collectionDAO.delete(id);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void deleteItem() {
        try {
            System.out.print("Enter the item id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            Item item = itemDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Collection with this id doesn't exist"));
            itemDAO.delete(id);
            (item.getPhoto().getPath().toFile()).delete(); // if this operation fails, reset also the db TODO
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void updateItem() {
    }

    public void descriptionAI() {
        try {
            System.out.print("Enter the item id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            Item item = itemDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Collection with this id doesn't exist"));
            item.generateDescription();
            System.out.println("Description generated!");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void showTransactions() {
        try {
            System.out.println("All transactions:");
            Set<Transaction> set = transactionDAO.getAll();
            set.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void showWallet() {
        try {
            Set<Transaction> set = transactionDAO.getAll();
            double amount = 0.0;
            for (Transaction transaction: set) {
                if (transaction.isIncome())
                    amount += transaction.getAmount();
                else
                    amount -= transaction.getAmount();
            }
            System.out.println("Wallet amount: " + amount + "euro");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void insertTransaction() {
        try {
            System.out.print("Enter the item id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            Optional<Item> item = itemDAO.get(id);
            if (item.isEmpty())
                throw new IllegalArgumentException("Item with this id doesn't exist");
            System.out.print("Enter the amount: ");
            double amount = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Enter the date (ex. 2025-11-02) : ");
            String input = sc.nextLine().trim();
            LocalDate date = LocalDate.parse(input, formatter);
            System.out.print("Is an income? (1 for yes, 0 for no): ");
            int num = Integer.parseInt(sc.nextLine().trim());
            boolean income = (num == 1);
            Transaction transaction = new Transaction(amount, income, date);
            transactionDAO.add(transaction, id);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public void generateAlbum() {
        try {
            System.out.print("Enter the collection id: ");
            long id = Long.parseLong(sc.nextLine().trim());
            ItemCollection collection = collectionDAO.get(id).orElseThrow(() -> new IllegalArgumentException("Collection with this id doesn't exist"));
            Desktop desktop = Desktop.getDesktop();
            desktop.open(Paths.get(collection.buildPhoto()).toFile());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printMainMenu() {
        boolean flag = true;
        while (flag) {
            System.out.println("""
                    Select an option:
                    1: Show all collections
                    2: Show all item of a collection
                    3: Item info (also price for tech...)
                    4: Add a new collection
                    5: Remove a collection
                    6: Insert item in a collection
                    7: Update item of a collection
                    8: Remove item
                    9: Generate item description with AI
                    10: Show all transactions
                    11: Show collection wallet amount
                    12: Add a transaction
                    13: Generate collection photo album
                    14: Exit
                    TODO update collection name, insert transaction, some business..., collection photo
                    """);
            String response = sc.nextLine().trim(); //se ci fosse un controller, andrebbe passata la risposta al controller, che come classe gestisce l'interpretazione dell'input utente, dell'aggiornamento del modello (se ad esempio c'è da afre un inserimento e dell'aggiornamwnto della view (se c'è da fare ad esempio uno show di tutti gli item
            switch (response) {
                case "1" -> showCollections();
                case "2" -> {
                    showCollectionItems();
                }
                case "3" -> {
                    showItemInfo();
                }
                case "4" -> {
                    addCollection();
                }
                case "5" -> {
                    deleteCollection();
                }
                case "6" -> {
                    insertItem();
                }
                case "7" -> {
                    //TODO
                }
                case "8" -> {
                    deleteItem();
                }
                case "9" -> {
                    descriptionAI();
                }
                case "10" -> {
                    showTransactions();
                }
                case "11" -> {
                    showWallet();
                }
                case "12" -> {
                    insertTransaction();
                }
                case "13" -> {
                    generateAlbum();
                }
                case "14" -> {
                    System.out.println("Bye bye");
                    flag = false;
                }
            }
        }
    }

    public void printWelcomeMenu() {
        System.out.println("Welcome to Collexio! Enter the storage modality");
        printMainMenu();
    }

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = ConnectionFactory.getConnection();
        CLI cli = new CLI(connection);
        cli.printWelcomeMenu();
    }

    // upload photo item
    // get price
}
