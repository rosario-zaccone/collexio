package org.collexio.presentation.view;

import org.collexio.presentation.model.*;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

public class TabbedPanel extends JPanel {
    private final JTabbedPane tabbedPanel;
    private final ItemCollectionPanel itemCollectionPanel;
    private final ItemPanel itemPanel;
    private final ItemSpecPanel itemSpecPanel;

    public TabbedPanel(
            ItemCollectionTableModel itemCollectionModel,
            ItemSpecTableModel itemSpecModel,
            ItemTableModel itemModel
    ) throws SQLException, IOException {
        super(new GridLayout(1, 1));

        tabbedPanel = new JTabbedPane();

        itemCollectionPanel = new ItemCollectionPanel(itemCollectionModel);
        tabbedPanel.addTab("Item collection", null, itemCollectionPanel, "Item collection");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_1);

        // Item Spec tab
        itemSpecPanel = new ItemSpecPanel(itemSpecModel);
        tabbedPanel.addTab("Item specification", null, itemSpecPanel, "Item specification");
        tabbedPanel.setMnemonicAt(1, KeyEvent.VK_2);


        // Item tab
        itemPanel = new ItemPanel(itemModel, itemCollectionModel);
        tabbedPanel.addTab("Item", null, itemPanel, "Item");
        tabbedPanel.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPanel.addChangeListener(e -> {
            int index = tabbedPanel.getSelectedIndex();
            try {
                switch (index) {
                    case 0 -> itemCollectionModel.refresh();
                    case 1 -> itemSpecModel.refresh();
                    case 2 -> itemModel.refresh();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        tabbedPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabbedPanel);
    }

    public ItemCollectionPanel getItemCollectionPanel() {
        return itemCollectionPanel;
    }

    public ItemPanel getItemPanel() {
        return itemPanel;
    }

    public ItemSpecPanel getItemSpecPanel() {
        return itemSpecPanel;
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }
}