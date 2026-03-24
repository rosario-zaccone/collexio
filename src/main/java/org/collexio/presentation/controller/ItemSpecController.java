package org.collexio.presentation.controller;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.model.ItemType;
import org.collexio.presentation.model.CrudTableModel;
import org.collexio.presentation.view.InsertSpecForm;
import org.collexio.presentation.view.ItemSpecPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ItemSpecController {
    private CrudTableModel model;
    private ItemSpecPanel view;
    private final InsertSpecForm insertForm;


    public ItemSpecController(CrudTableModel model, ItemSpecPanel view) {
        this.model = model;
        this.view = view;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButtonColumn().setAction(new DeleteAction());
        insertForm = view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> {
            insertForm.dispose();
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
}
