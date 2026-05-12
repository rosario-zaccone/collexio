package org.collexio.bootstrap;

import org.collexio.AppConfig;
import org.collexio.ConfigManager;
import org.collexio.ConnectionFactory;
import org.collexio.DatabaseInitializer;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.presentation.controller.*;
import org.collexio.presentation.model.*;
import org.collexio.presentation.view.AppFrame;
import org.collexio.presentation.view.PresentationText;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.factory.BookProviderFactory;
import org.collexio.utilities.factory.PlantProviderFactory;
import org.collexio.utilities.factory.TechItemProviderFactory;
import org.collexio.utilities.infogenerator.GeminiInfoGenerator;
import org.collexio.utilities.pricecraper.SubitoScraper;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Create ~/.collexio/data/ and ~/.collexio/images/ if missing
                AppConfig.initDirs();

                // 2. Load config and ask for API key on first launch
                ConfigManager config = new ConfigManager();
                config.load();
                config.ensureInitialConfig();

                // 3. Connect to SQLite
                Connection connection = ConnectionFactory.getConnection();

                // 4. Run schema if DB is new
                DatabaseInitializer.initIfNeeded(connection);

                // 5. Build factories with API key from config
                String apiKey = config.getApiKey();
                AbstractFactory plantProviderFactory   = new PlantProviderFactory(apiKey);
                AbstractFactory bookProviderFactory    = new BookProviderFactory(apiKey);
                AbstractFactory techItemProviderFactory = new TechItemProviderFactory(apiKey);

                // 6. DAOs
                ItemCollectionDAO collectionDAO = new DBItemCollectionDAO(connection);
                ItemDAO           itemDAO       = new DBItemDAO(connection);
                ItemSpecDAO       specDAO       = new DBItemSpecDAO(connection);
                ItemPhotoDAO      photoDAO      = new DBItemPhotoDAO(connection);
                TransactionDAO    transactionDAO = new DBTransactionDAO(connection);

                // 7. Services
                ItemCollectionService itemCollectionService = new ItemCollectionService(collectionDAO);
                ItemService           itemService          = new ItemService(itemDAO);
                ItemSpecService       itemSpecService      = new ItemSpecService(specDAO);
                ItemPhotoService      itemPhotoService     = new ItemPhotoService(photoDAO);
                TransactionService    transactionService   = new TransactionService(transactionDAO);
                PriceService          priceService         = new PriceService(new SubitoScraper(), plantProviderFactory, bookProviderFactory, techItemProviderFactory);
                InfoGenerationService infoService          = new InfoGenerationService(new GeminiInfoGenerator(apiKey, 50), plantProviderFactory, bookProviderFactory, techItemProviderFactory);

                // 8. Orchestrators
                ItemCollectionOrchestrator collectionOrchestrator = new ItemCollectionOrchestrator(itemService, itemCollectionService);
                ItemOrchestrator           itemOrchestrator       = new ItemOrchestrator(itemService, itemPhotoService, transactionService, itemSpecService);

                // 9. Table models
                ItemCollectionTableModel itemCollectionModel = new ItemCollectionTableModel(itemCollectionService, collectionOrchestrator);
                ItemSpecTableModel       itemSpecModel       = new ItemSpecTableModel(itemSpecService, priceService, infoService);
                ItemTableModel           itemModel           = new ItemTableModel(itemService, itemOrchestrator, itemSpecService, priceService);
                TransactionTableModel    transactionModel    = new TransactionTableModel(transactionService);

                // 10. Main frame
                AppFrame frame = new AppFrame(
                        "Collexio",
                        itemCollectionModel,
                        itemSpecModel,
                        itemModel,
                        transactionModel
                );

                // 11. Close DB connection on exit
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

                // 12. Controllers
                var tab = frame.getTabbedPanel();
                new ItemSpecController(itemSpecModel, tab.getItemSpecPanel());
                new ItemCollectionController(
                        itemCollectionModel,
                        tab.getItemCollectionPanel(),
                        tab.getItemPanel(),
                        tab::selectItemPanel
                );
                new ItemController(
                        itemModel,
                        tab.getItemPanel(),
                        tab.getTransactionPanel(),
                        tab::selectTransactionPanel
                );
                new TransactionController(transactionModel, tab.getTransactionPanel());

            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(
                        null,
                        e.getMessage(),
                        PresentationText.text("Startup Error"),
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(
                        null,
                        PresentationText.text("Startup failed:\n") + e.getMessage(),
                        PresentationText.text("Error"),
                        JOptionPane.ERROR_MESSAGE
                );
                throw new RuntimeException(e);
            }
        });
    }
}
