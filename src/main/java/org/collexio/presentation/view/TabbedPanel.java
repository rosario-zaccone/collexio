package org.collexio.presentation.view;

import org.collexio.presentation.model.*;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;
import org.collexio.presentation.view.transaction.TransactionPanel;

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
    private final TransactionPanel transactionPanel;

    public TabbedPanel(
            ItemCollectionTableModel itemCollectionModel,
            ItemSpecTableModel itemSpecModel,
            ItemTableModel itemModel,
            TransactionTableModel transactionModel
    ) throws SQLException, IOException {
        super(new GridLayout(1, 1));

        tabbedPanel = new JTabbedPane();

        itemCollectionPanel = new ItemCollectionPanel(itemCollectionModel);
        tabbedPanel.addTab("Item collections", null, itemCollectionPanel, "Item collections");
        tabbedPanel.setMnemonicAt(0, KeyEvent.VK_1);

        // Item Spec tab
        itemSpecPanel = new ItemSpecPanel(itemSpecModel);
        tabbedPanel.addTab("Item specifications", null, itemSpecPanel, "Item specifications");
        tabbedPanel.setMnemonicAt(1, KeyEvent.VK_2);


        // Item tab
        itemPanel = new ItemPanel(itemModel, itemCollectionModel);
        tabbedPanel.addTab("Items", null, itemPanel, "Items");
        tabbedPanel.setMnemonicAt(2, KeyEvent.VK_3);

        // Transaction tab
        transactionPanel = new TransactionPanel(transactionModel, itemModel);
        tabbedPanel.addTab("Transactions", null, transactionPanel, "Transactions");
        tabbedPanel.setMnemonicAt(2, KeyEvent.VK_4);

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

    public TransactionPanel getTransactionPanel() {
        return transactionPanel;
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }
}