package org.collexio.presentation.controller;

import org.collexio.business.domain.ItemCollection;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.view.PresentationText;
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
    private final ItemCollectionTableModel model;
    private final ItemCollectionPanel view;
    private final ItemPanel itemView;
    private final TabSwitchListener tabSwitchListener;
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
            insertForm.clearForm();
        }
    }


    class InsertFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String name = insertForm.getName();
                ItemCollection collection = new ItemCollection(name);
                model.addRow(collection, null);
                insertForm.setMessageLabel(PresentationText.text("Item Collection inserted"));
                itemView.refreshFilter();

            } catch (IllegalArgumentException ex) {
                insertForm.setMessageLabel(PresentationText.text("Input Error: ") + ex.getMessage());
            } catch (SQLException ex) {
                insertForm.setMessageLabel(PresentationText.text("Database Error: ") + ex.getMessage());
            } catch (IOException ex) {
                insertForm.setMessageLabel(PresentationText.text("IO Error: ") + ex.getMessage());
            } catch (RuntimeException ex) {
                insertForm.setMessageLabel(PresentationText.text("Error: ") + ex.getMessage());
            }

        }
    }

    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long id = Long.parseLong(updateForm.getId());
                String name = updateForm.getName();
                model.updateRow(new ItemCollection(id, name), null);
                updateForm.setMessageLabel(PresentationText.text("Item Collection updated"));
            } catch (IllegalArgumentException ex) {
                updateForm.setMessageLabel(PresentationText.text("Input Error: ") + ex.getMessage());
            } catch (SQLException ex) {
                updateForm.setMessageLabel(PresentationText.text("Database Error: ") + ex.getMessage());
            } catch (IOException ex) {
                updateForm.setMessageLabel(PresentationText.text("IO Error: ") + ex.getMessage());
            } catch (RuntimeException ex) {
                updateForm.setMessageLabel(PresentationText.text("Error: ") + ex.getMessage());
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
                    PresentationText.text("Are you sure?"),
                    PresentationText.text("Delete confirm"),
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    model.removeRow(modelRow);
                    itemView.refreshFilter();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            view.getTable(),
                            PresentationText.text("Error"),
                            PresentationText.text("Database Error: ") + ex.getMessage(),
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
            updateForm.clearForm();
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
                tabSwitchListener.switchToTab();
                int viewRow = Integer.parseInt(e.getActionCommand());
                int modelRow = view.getTable().convertRowIndexToModel(viewRow);
                ItemCollection collection = model.getRow(modelRow);
                Long collectionId = collection.getId(); // set the filter to this id
                itemView.setCollectionIdForFilter(collectionId.toString());

            }
        }
    }

}
