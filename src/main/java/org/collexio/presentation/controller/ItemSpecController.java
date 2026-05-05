package org.collexio.presentation.controller;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.entity.ItemType;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.itemspec.InsertSpecForm;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;
import org.collexio.presentation.view.itemspec.UpdateSpecForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ItemSpecController {
    private final ItemSpecTableModel model;
    private final ItemSpecPanel view;
    private final InsertSpecForm insertForm;
    private final UpdateSpecForm updateForm;


    public ItemSpecController(ItemSpecTableModel model, ItemSpecPanel view) {
        this.model = model;
        this.view = view;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        this.view.getUpdateButton().setAction(new UpdateAction());
        this.view.getPriceButton().setAction(new PriceAction());

        insertForm = this.view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> insertForm.dispose());

        updateForm = this.view.getUpdateSpecForm();
        updateForm.getSubmitButton().addActionListener(new UpdateFormListener());
        updateForm.getCancelButton().addActionListener(e -> {
            updateForm.dispose();
            updateForm.clearForm();
        });
        updateForm.getGenerateDescButton().addActionListener(new GenerateDescriptionButtonListener());

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
                ItemType type = ItemType.fromInt(insertForm.getItemType());
                String description = insertForm.getDescription();
                ItemSpec spec = new ItemSpec(type, name, description);
                model.addRow(spec, null);
                insertForm.setMessageLabel("Item spec inserted");
            } catch (IllegalArgumentException ex) {
                insertForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                insertForm.setMessageLabel("Database Error: " + ex.getMessage());
            }

        }
    }

    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long id = Long.parseLong(updateForm.getId());
                String name = updateForm.getName();
                ItemType type = ItemType.fromInt(updateForm.getItemType());
                String description = updateForm.getDescription();
                model.updateRow(new ItemSpec(id, type, name, description), null);
                updateForm.setMessageLabel("Item spec updated");
            } catch (IllegalArgumentException ex) {
                updateForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                updateForm.setMessageLabel("Database Error: " + ex.getMessage());
            }

        }
    }

    class GenerateDescriptionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String desc = model.generateDescription(new ItemSpec(Long.parseLong(updateForm.getId()), ItemType.fromInt(updateForm.getItemType()), updateForm.getName(), updateForm.getDescription()));
                updateForm.setDescription(desc);
            } catch (IOException | InterruptedException ex) {
                JOptionPane.showMessageDialog(
                        view.getTable(),
                        "Error during description generation " + ex.getMessage(),
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                );
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
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            view.getTable(),
                            "Error",
                            "Database Error " + ex.getMessage(),
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
            ItemSpec spec = model.getRow(modelRow);
            updateForm.setId(spec.getId().toString());
            updateForm.setDescription(spec.getDescription());
            updateForm.setName(spec.getName());
            updateForm.setItemType(spec.getType().getValue());
        }
    }


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
