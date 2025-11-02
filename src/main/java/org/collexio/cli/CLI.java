package org.collexio.cli;

import org.checkerframework.checker.units.qual.C;
import org.collexio.domain.*;
import org.collexio.persistence.ConnectionFactory;
import org.collexio.persistence.DBItemCollectionDAO;
import org.collexio.persistence.DBItemDAO;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

public class CLI {
    private static final Scanner sc = new Scanner(System.in);
    private DBItemCollectionDAO<Plant> plantCollectionDAO;
    private DBItemCollectionDAO<Book> bookCollectionDAO;
    private DBItemCollectionDAO<TechItem> techCollectionDAO;
    private DBItemDAO itemDAO;
    private DateTimeFormatter formatter;

    public CLI (Connection connection) throws SQLException, IOException {
        plantCollectionDAO = new DBItemCollectionDAO<>(connection);
        bookCollectionDAO = new DBItemCollectionDAO<>(connection);
        techCollectionDAO = new DBItemCollectionDAO<>(connection);
        itemDAO = new DBItemDAO(connection);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public void showCollections(String type) {
        // type indicates the collection type, it can be set on all type for show all collections
        System.out.println("All collections");
    }

    public void addCollection() throws SQLException {
        System.out.println("Enter the type of collection (0 for plant, 1 for tech item, 2 for books)");
        int type = Integer.parseInt(sc.nextLine());
        System.out.println("Enter the collection name");
        String name = sc.nextLine().trim();
        try {
            switch (type) {
                case 0 -> {
                    plantCollectionDAO.add(new ItemCollection<Plant>(name));
                }
                case 1 -> {
                    techCollectionDAO.add(new ItemCollection<TechItem>(name));
                }
                case 2 -> {
                    bookCollectionDAO.add(new ItemCollection<Book>(name));
                }
                default -> {
                    System.out.println("Collection not added: Invalid type");
                    return;
                }
            }
            System.out.println("Collection added!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error while inserting the collection:" + e.getMessage());
        }
    }

    public void insertItem() throws SQLException {
        try {
            System.out.print("Enter the collection id: ");
            long collectionId = Long.parseLong(sc.nextLine());
            System.out.print("Enter the item type (0-Plant,1-TechItem,2-Book): ");
            int type = Integer.parseInt(sc.nextLine());
            System.out.print("Enter the item name: ");
            String name = sc.nextLine().trim();
            System.out.print("Enter the item quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());
            System.out.print("Enter the item description: ");
            String description = sc.nextLine().trim();
            System.out.print("Enter the item scientific_name (not needed for tech items and books): ");
            String second_name = sc.nextLine().trim();

            System.out.print("Enter the photo path: ");
            String path = sc.nextLine().trim();
            System.out.print("Enter date and hour (ex. 2025-11-02 14:30): ");
            String input = sc.nextLine().trim();
            LocalDateTime date = LocalDateTime.parse(input, formatter);

            ItemPhoto photo = new ItemPhoto(Paths.get(path), date);
            Item item = switch (type) {
                case 0 -> new Plant(name, quantity, photo, description, second_name);
                case 1 -> new TechItem(name, quantity, photo, description);
                case 2 -> new Book(name, quantity, photo, description);
                default -> throw new IllegalArgumentException("Invalid item type");
            };
            switch (type) {
                case 0 -> {
                    Optional<ItemCollection<Plant>> collection = plantCollectionDAO.get(collectionId);
                    if (collection.isPresent()) {
                        itemDAO.add(item, collectionId);
                    } else {
                        throw new IllegalArgumentException("Collection with this id does not exist");
                    }
                }
                case 1 -> {
                    Optional<ItemCollection<TechItem>> collection = techCollectionDAO.get(collectionId);
                    if (collection.isPresent()) {
                        itemDAO.add(item, collectionId);
                    } else {
                        throw new IllegalArgumentException("Collection with this id does not exist");
                    }
                }
                case 3 -> {
                    Optional<ItemCollection<Book>> collection = bookCollectionDAO.get(collectionId);
                    if (collection.isPresent()) {
                        itemDAO.add(item, collectionId);
                    } else {
                        throw new IllegalArgumentException("Collection with this id does not exist");
                    }
                }
                default -> throw new IllegalArgumentException("Invalid item type");
            }
        } catch (IllegalArgumentException e ) {
            System.out.println("Item not inserted: " + e.getMessage());
        }

    }

    public void showCollectionItems(String collectionId) {
        System.out.println("All items");
    }

    public void showItemInfo(String itemId) {
        System.out.println("Item info");
    }

    public void printMainMenu() {
        boolean flag = true;
        while (flag) {
            System.out.println("""
                    Select an option:
                    1: Show all collections
                    2: Show all item of a collection
                    3: Item info (also photos for plant, price for tech...)
                    4: Add a new collection
                    5: Remove a collection
                    6: Insert item in a collection
                    7: Update item of a collection
                    8: Remove item of a collection
                    9: Show all transaction
                    10: Show collection wallet amount
                    11: Save and exit
                    TODO update collection name, insert transaction, some business..., collection photo
                    """);
            String response = sc.nextLine().trim(); //se ci fosse un controller, andrebbe passata la risposta al controller, che come classe gestisce l'interpretazione dell'input utente, dell'aggiornamento del modello (se ad esempio c'è da afre un inserimento e dell'aggiornamwnto della view (se c'è da fare ad esempio uno show di tutti gli item
            switch (response) {
                case "1" -> showCollections("");
                case "2" -> {
                    System.out.println("Enter the collection id");
                    response = sc.nextLine().trim();
                    showCollectionItems(response);
                }
                case "3" -> {
                    System.out.println("Enter the item id");
                    response = sc.nextLine().trim();
                    showItemInfo(response);
                }
                case "4" -> {
                    try {
                        addCollection();
                    } catch (SQLException e) {
                        System.out.println("DB error");
                    }
                }
                case "6" -> {
                    try {
                        insertItem();
                    } catch (SQLException e) {
                        System.out.println("DB error");
                    }
                }
                case "11" -> {
                    System.out.println("Bye bye");
                    // save state
                    flag = false;
                }
            }
        }
    }

    public void printWelcomeMenu() {
        System.out.println("Welcome to Collexio! Enter the storage modality");
        // manage the json and db mode
        // load the db or json file, load the state
        printMainMenu();
    }

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = ConnectionFactory.getConnection();
        CLI cli = new CLI(connection);
        cli.printWelcomeMenu();
    }
}
