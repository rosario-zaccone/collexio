package org.collexio.presentationtesting;

import com.formdev.flatlaf.FlatLightLaf;
import org.collexio.business.domain.ItemSpec;
import org.collexio.business.service.ItemSpecService;
import org.collexio.persistence.dao.ConnectionFactory;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.persistence.model.ItemType;
import org.collexio.presentation.controller.ItemSpecController;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.ItemSpecPanel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemSpecPanelTest {
    private static void createAndShowGUI() throws SQLException, IOException {
        FlatLightLaf.setup();
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ItemSpecTableModel model = new ItemSpecTableModel(new ItemSpecService(new DBItemSpecDAO(ConnectionFactory.getConnection())));
        ItemSpecPanel view = new ItemSpecPanel(model);
        view.setOpaque(true);
        frame.setContentPane(view);

        frame.pack();
        frame.setVisible(true);

        ItemSpecController controller = new ItemSpecController(model, view);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
