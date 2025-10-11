package org.collexio.cli;

import org.checkerframework.checker.units.qual.C;

import java.util.Scanner;

public class CLI {
    private static final Scanner sc = new Scanner(System.in);

    public void showCollections(String type) {
        // type indicates the collection type, it can be set on all type for show all collections
        System.out.println("All collections");
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

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.printWelcomeMenu();
    }
}
