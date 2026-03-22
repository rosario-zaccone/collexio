package org.collexio.presentationtesting;

import com.formdev.flatlaf.FlatLightLaf;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.model.ItemType;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.ItemSpecPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ItemSpecPanelTest {
    private static void createAndShowGUI() {
        FlatLightLaf.setup();
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        List<ItemSpec> data = new ArrayList<>();
        data.add(new ItemSpec(1L, ItemType.TECHITEM, "Nintendo DS", "nintendo console"));

        ItemSpecPanel newContentPane = new ItemSpecPanel(new ItemSpecTableModel(data));
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
