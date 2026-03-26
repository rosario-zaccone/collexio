package org.collexio.presentationtesting;

import com.formdev.flatlaf.FlatLightLaf;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.presentation.controller.ItemCollectionController;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ItemCollectionPanelTest {

    private static void createAndShowGUI() throws SQLException, IOException {
        FlatLightLaf.setup();
        JFrame frame = new JFrame("ItemCollection Table Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Connection conn = ConnectionFactory.getConnection();

        ItemCollectionTableModel model = new ItemCollectionTableModel(
                new ItemCollectionService(
                        new DBItemCollectionDAO(conn),
                        new ItemService(
                                new DBItemDAO(conn),
                                new ItemPhotoService(new DBItemPhotoDAO(conn)),
                                new TransactionService(new DBTransactionDAO(conn)),
                                new ItemSpecService(new DBItemSpecDAO(conn))
                        )
                )
        );

        ItemCollectionPanel view = new ItemCollectionPanel(model);
        view.setOpaque(true);
        frame.setContentPane(view);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ItemCollectionController controller = new ItemCollectionController(model, view);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}