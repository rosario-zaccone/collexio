package org.collexio.presentationtesting;

import org.collexio.business.service.ItemSpecService;
import org.collexio.persistence.dao.ConnectionFactory;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.presentation.controller.ItemSpecController;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class ItemSpecPanelTest {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame("TableDemo");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    ItemSpecTableModel model = new ItemSpecTableModel(new ItemSpecService(new DBItemSpecDAO(ConnectionFactory.getConnection())));
                    ItemSpecPanel view = new ItemSpecPanel(model);
                    view.setOpaque(true);
                    frame.setContentPane(view);

                    frame.pack();
                    frame.setVisible(true);

                    ItemSpecController controller = new ItemSpecController(model, view);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
