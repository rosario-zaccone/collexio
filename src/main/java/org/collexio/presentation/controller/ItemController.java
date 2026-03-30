package org.collexio.presentation.controller;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.model.ItemStatus;
import org.collexio.persistence.model.ItemType;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.view.item.InsertItemForm;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemspec.InsertSpecForm;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;
import org.collexio.presentation.view.itemspec.UpdateSpecForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;

public class ItemController {
    private ItemTableModel model;
    private ItemPanel view;
    private final InsertItemForm insertForm;
    // private final UpdateSpecForm updateForm;


    public ItemController(ItemTableModel model, ItemPanel view) {
        this.model = model;
        this.view = view;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        // this.view.getUpdateButton().setAction(new UpdateAction());

        insertForm = view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> insertForm.dispose());
        this.view.getPriceButton().setAction(new PriceAction());

        /*
        updateForm = view.getUpdateSpecForm();
        updateForm.getSubmitButton().addActionListener(new UpdateFormListener());
        updateForm.getCancelButton().addActionListener(e -> {
            updateForm.dispose();
            updateForm.clearForm();
        });
        updateForm.getGenerateDescButton().addActionListener(new GenerateDescriptionButtonListener());
        */
    }

    class InsertButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            insertForm.setVisible(true);
        }
    }


    class InsertFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long collectionId = null;
                String collectionStr = insertForm.getCollection();
                if (!collectionStr.isEmpty())
                    collectionId = Long.parseLong(collectionStr);
                Long specId = Long.parseLong(insertForm.getSpec());
                ItemStatus status = ItemStatus.fromInt(insertForm.getStatus());
                Path path = Path.of(insertForm.getPhotoPath());
                LocalDate date = LocalDate.parse(insertForm.getPhotoDate());
                ItemPhoto photo = new ItemPhoto(path, date);
                ItemSpec spec = model.getSpec(specId);
                model.addRowWithCollection(new Item(status, photo, spec), collectionId);
                insertForm.setMessageLabel("Item spec inserted");
            } catch (IllegalArgumentException ex) { // TODO: change messagges
                insertForm.setMessageLabel("Error: " + ex);
            } catch (RuntimeException ex) {
                insertForm.setMessageLabel("Error: " + ex);
            } catch (SQLException ex) {
                insertForm.setMessageLabel("Error: " + ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    /*
    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long id = Long.parseLong(updateForm.getId());
                String name = updateForm.getName();
                ItemType type = ItemType.fromString(updateForm.getItemType());
                String description = updateForm.getDescription();
                model.updateRow(new ItemSpec(id, type, name, description));
                updateForm.setMessageLabel("Item spec updated");
            } catch (IllegalArgumentException ex) { // TODO: change messagges
                updateForm.setMessageLabel("Error: " + ex);
            } catch (RuntimeException ex) {
                updateForm.setMessageLabel("Error: " + ex);
                ex.printStackTrace();
            } catch (SQLException ex) {
                updateForm.setMessageLabel("Error: " + ex);
            }

        }
    }
     */


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
                } catch (SQLException ex) {
                    throw new RuntimeException(ex); // TODO: show dialog error messagge (or message on a status bar)
                }
            }
        }
    }


    /*
    public class UpdateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateForm.setVisible(true);
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);
            ItemSpec spec = model.getRow(modelRow);
            updateForm.setId(spec.getId().toString());
            updateForm.setDescription(spec.getDescription());
            updateForm.setName(spec.getName());
            updateForm.setItemType(spec.getType().toString());
        }
    }
     */

    public class PriceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);
            String message = "Sorry, no price available";
            try {
                double price = model.price(modelRow);
                message = price + " €";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(
                    view.getTable(),
                    message,
                    "Price scraped",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

}

