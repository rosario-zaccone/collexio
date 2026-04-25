package org.collexio;

import io.github.cdimascio.dotenv.Dotenv;
import org.collexio.persistence.dao.*;
import org.collexio.business.service.*;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.model.TransactionTableModel;
import org.collexio.presentation.view.AppFrame;
import org.collexio.presentation.controller.*;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.factory.BookProviderFactory;
import org.collexio.utilities.factory.PlantProviderFactory;
import org.collexio.utilities.factory.TechItemProviderFactory;
import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.pricecraper.SubitoScraper;

import javax.swing.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Dotenv dotenv = Dotenv.load();
                AbstractFactory plantProviderFactory = new PlantProviderFactory(dotenv);
                AbstractFactory bookProviderFactory = new BookProviderFactory(dotenv);
                AbstractFactory techItemProviderFactory = new TechItemProviderFactory(dotenv);

                Connection connection = ConnectionFactory.getConnection();

                ItemCollectionDAO collectionDAO = new DBItemCollectionDAO(connection);
                ItemDAO itemDAO = new DBItemDAO(connection);
                ItemSpecDAO specDAO = new DBItemSpecDAO(connection);
                ItemPhotoDAO photoDAO = new DBItemPhotoDAO(connection);
                TransactionDAO transactionDAO = new DBTransactionDAO(connection);

                ItemCollectionService itemCollectionService = new ItemCollectionService(collectionDAO);
                ItemService itemService = new ItemService(itemDAO);
                ItemSpecService itemSpecService = new ItemSpecService(specDAO);
                ItemPhotoService itemPhotoService = new ItemPhotoService(photoDAO);
                TransactionService transactionService = new TransactionService(transactionDAO);
                PriceService priceService = new PriceService(new SubitoScraper());
                InfoGenerationService infoService = new InfoGenerationService(new GeminiInfoGenerator("", 50));

                ItemCollectionOrchestrator collectionOrchestrator =
                        new ItemCollectionOrchestrator(itemService, itemCollectionService);

                ItemOrchestrator itemOrchestrator =
                        new ItemOrchestrator(itemService, itemPhotoService, transactionService, itemSpecService);

                ItemCollectionTableModel itemCollectionModel = new ItemCollectionTableModel(itemCollectionService, collectionOrchestrator);
                ItemSpecTableModel itemSpecModel = new ItemSpecTableModel(itemSpecService, priceService, infoService, plantProviderFactory, bookProviderFactory, techItemProviderFactory);
                ItemTableModel itemModel = new ItemTableModel(itemService, itemOrchestrator, itemSpecService, priceService);
                TransactionTableModel transactionModel = new TransactionTableModel(transactionService);

                var frame = new AppFrame(
                        "Collexio",
                        itemCollectionModel,
                        itemSpecModel,
                        itemModel,
                        transactionModel
                );

                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        try {
                            if (connection != null && !connection.isClosed()) {
                                connection.close();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                var tab = frame.getTabbedPanel();
                // Controllers
                new ItemSpecController(itemSpecModel, tab.getItemSpecPanel());
                new ItemCollectionController(
                        itemCollectionModel,
                        tab.getItemCollectionPanel(),
                        tab.getItemPanel(),
                        () -> {
                            // tab.getItemPanel().getModel().refresh();
                            tab.getTabbedPanel().setSelectedComponent(tab.getItemPanel());
                        }
                );
                new ItemController(
                        itemModel,
                        tab.getItemPanel(),
                        tab.getTransactionPanel(),
                        () -> {
                            // tab.getItemPanel().getModel().refresh();
                            tab.getTabbedPanel().setSelectedComponent(tab.getTransactionPanel());
                        }
                        );
                new TransactionController(transactionModel, tab.getTransactionPanel());

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}