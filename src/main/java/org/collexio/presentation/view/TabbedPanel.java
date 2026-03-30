package org.collexio.presentation.view;

import org.collexio.business.domain.Transaction;
import org.collexio.business.service.*;
import org.collexio.persistence.dao.*;
import org.collexio.presentation.controller.ItemCollectionController;
import org.collexio.presentation.controller.ItemController;
import org.collexio.presentation.controller.ItemSpecController;
import org.collexio.presentation.model.*;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class TabbedPanel extends JPanel {

    public TabbedPanel(
            Connection connection,
            ItemCollectionService itemCollectionService,
            ItemCollectionOrchestrator collectionOrchestrator,
            ItemService itemService,
            ItemOrchestrator itemOrchestrator,
            ItemPhotoService photoService,
            ItemSpecService specService,
            TransactionService transactionService
    ) throws SQLException, IOException {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();

        // Item Collection tab
        ItemCollectionTableModel itemCollectionModel = new ItemCollectionTableModel(
                itemCollectionService,
                collectionOrchestrator
        );
        ItemCollectionPanel itemCollectionView = new ItemCollectionPanel(itemCollectionModel);
        tabbedPane.addTab("Item collection", null, itemCollectionView, "Item collection");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        // Item Spec tab
        ItemSpecTableModel itemSpecModel = new ItemSpecTableModel(specService);
        ItemSpecPanel itemSpecView = new ItemSpecPanel(itemSpecModel);
        tabbedPane.addTab("Item specification", null, itemSpecView, "Item specification");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);


        // Item tab
        ItemTableModel itemModel = new ItemTableModel(itemService, itemOrchestrator, specService);
        ItemPanel itemView = new ItemPanel(itemModel);
        tabbedPane.addTab("Item", null, itemView, "Item");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
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

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabbedPane);

        // Controllers
        new ItemSpecController(itemSpecModel, itemSpecView);
        new ItemCollectionController(itemCollectionModel, itemCollectionView);
        new ItemController(itemModel, itemView);
    }
}