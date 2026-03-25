package org.collexio.presentation.controller;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.model.ItemType;
import org.collexio.presentation.dto.ItemSpecDTO;
import org.collexio.presentation.model.CrudTableModel;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.InsertSpecForm;
import org.collexio.presentation.view.ItemSpecPanel;
import org.collexio.presentation.view.UpdateSpecForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

public class ItemSpecController {
    private ItemSpecTableModel model;
    private ItemSpecPanel view;
    private final InsertSpecForm insertForm;
    private final UpdateSpecForm updateForm;


    public ItemSpecController(ItemSpecTableModel model, ItemSpecPanel view) {
        this.model = model;
        this.view = view;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        this.view.getUpdateButton().setAction(new UpdateAction());
        this.view.getPriceButton().setAction(new PriceAction());
        this.view.getUpdateButton().setAction(new UpdateAction());

        insertForm = view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> {
            insertForm.dispose();
        });

        updateForm = view.getUpdateSpecForm();
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
                ItemType type = ItemType.fromInt(Integer.parseInt(insertForm.getItemType()));
                String description = insertForm.getDescription();
                ItemSpec spec = new ItemSpec(type, name, description);
                model.addRow(spec);
                insertForm.setMessageLabel("Item spec inserted");
            } catch (IllegalArgumentException ex) { // TODO: change messagges
                insertForm.setMessageLabel("Error: " + ex);
            } catch (RuntimeException ex) {
                insertForm.setMessageLabel("Error: " + ex);
            } catch (SQLException ex) {
                insertForm.setMessageLabel("Error: " + ex);
            }

        }
    }

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

    class GenerateDescriptionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String desc = model.generateDescription(new ItemSpec(Long.parseLong(updateForm.getId()), ItemType.fromString(updateForm.getItemType()), updateForm.getName(), updateForm.getDescription()));
            System.out.println(desc);
            updateForm.setDescription(desc);
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
                    throw new RuntimeException(ex); // TODO: show dialog error messagge (or message on a status bar)
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
            ItemSpecDTO dto = ItemSpecDTO.fromDomain(model.getSpec(modelRow));
            updateForm.setId(dto.getId().toString());
            updateForm.setDescription(dto.getDescription());
            updateForm.setName(dto.getName());
            updateForm.setItemType(dto.getType().toString());
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
