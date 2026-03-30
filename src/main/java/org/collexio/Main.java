package org.collexio;

import org.collexio.persistence.dao.*;
import org.collexio.business.service.*;
import org.collexio.presentation.view.AppFrame;
import org.collexio.presentation.controller.*;
import javax.swing.*;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = ConnectionFactory.getConnection();

                ItemCollectionDAO collectionDAO = new DBItemCollectionDAO(connection);
                ItemDAO itemDAO = new DBItemDAO(connection);
                ItemSpecDAO specDAO = new DBItemSpecDAO(connection);
                ItemPhotoDAO photoDAO = new DBItemPhotoDAO(connection);
                TransactionDAO transactionDAO = new DBTransactionDAO(connection);

                ItemCollectionService collectionService = new ItemCollectionService(collectionDAO);
                ItemService itemService = new ItemService(itemDAO);
                ItemSpecService specService = new ItemSpecService(specDAO);
                ItemPhotoService photoService = new ItemPhotoService(photoDAO);
                TransactionService transactionService = new TransactionService(transactionDAO);

                ItemCollectionOrchestrator collectionOrchestrator =
                        new ItemCollectionOrchestrator(itemService, collectionService);

                ItemOrchestrator itemOrchestrator =
                        new ItemOrchestrator(itemService, photoService, transactionService, specService);

                new AppFrame(
                        "Collexio",
                        connection,
                        collectionService,
                        collectionOrchestrator,
                        itemService,
                        itemOrchestrator,
                        photoService,
                        specService,
                        transactionService
                );

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}