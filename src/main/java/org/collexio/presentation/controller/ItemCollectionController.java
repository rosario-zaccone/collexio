package org.collexio.presentation.controller;

import org.collexio.business.domain.ItemCollection;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.view.TabbedPanel;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemcollection.InsertItemCollectionForm;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;
import org.collexio.presentation.view.itemcollection.UpdateItemCollectionForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ItemCollectionController {
    private ItemCollectionTableModel model;
    private ItemCollectionPanel view;
    private ItemPanel itemView;
    private TabSwitchListener tabSwitchListener;
    private final InsertItemCollectionForm insertForm;
    private final UpdateItemCollectionForm updateForm;


    public ItemCollectionController(ItemCollectionTableModel model, ItemCollectionPanel view, ItemPanel itemView, TabSwitchListener tabSwitchListener) {
        this.model = model;
        this.view = view;
        this.itemView = itemView;
        this.tabSwitchListener = tabSwitchListener;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        this.view.getUpdateButton().setAction(new UpdateAction());
        this.view.getItemsButton().setAction(new ItemsAction());

        insertForm = this.view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new ItemCollectionController.InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> insertForm.dispose());

        updateForm = this.view.getUpdateForm();
        updateForm.getSubmitButton().addActionListener(new ItemCollectionController.UpdateFormListener());
        updateForm.getCancelButton().addActionListener(e -> {
            updateForm.dispose();
            updateForm.clearForm();
        });
    }

    class InsertButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            insertForm.setVisible(true);
        }
    }


    class InsertFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String name = insertForm.getName();
                ItemCollection collection = new ItemCollection(name);
                model.addRow(collection);
                insertForm.setMessageLabel("Item Collection inserted");
                itemView.refreshFilter();

            } catch (IllegalArgumentException ex) {
                insertForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                insertForm.setMessageLabel("Database Error: " + ex.getMessage());
            } catch (IOException ex) {
                insertForm.setMessageLabel("IO Error: " + ex.getMessage());
            }

        }
    }

    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long id = Long.parseLong(updateForm.getId());
                String name = updateForm.getName();
                model.updateRow(new ItemCollection(id, name));
                updateForm.setMessageLabel("Item Collection updated");
            } catch (IllegalArgumentException ex) {
                updateForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                updateForm.setMessageLabel("Database Error: " + ex.getMessage());
            } catch (IOException ex) {
                updateForm.setMessageLabel("IO Error: " + ex.getMessage());
            }

        }
    }


    public class DeleteAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);

            int confirm = JOptionPane.showConfirmDialog(
                    view.getTable(),
                    "Are you sure?",
                    "Delete confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    model.removeRow(modelRow);
                    itemView.refreshFilter();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            view.getTable(),
                            "Error",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        }
    }


    public class UpdateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateForm.setVisible(true);
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);
            ItemCollection collection = model.getRow(modelRow);
            updateForm.setId(collection.getId().toString());
            updateForm.setName(collection.getName());
        }
    }

    public class ItemsAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabSwitchListener != null) {
                tabSwitchListener.switchToItemTab();
                int viewRow = Integer.parseInt(e.getActionCommand());
                int modelRow = view.getTable().convertRowIndexToModel(viewRow);
                ItemCollection collection = model.getRow(modelRow);
                Long collectionId = collection.getId(); // set the filter to this id
                itemView.setCollectionIdForFilter(collectionId.toString());

            }
        }
    }

}
