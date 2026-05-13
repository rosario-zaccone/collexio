package org.collexio.presentation.controller;

import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.entity.ItemType;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.BusyDialog;
import org.collexio.presentation.view.PresentationText;
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
            insertForm.clearForm();
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
                insertForm.setMessageLabel(PresentationText.text("Item spec inserted"));
            } catch (IllegalArgumentException ex) {
                insertForm.setMessageLabel(PresentationText.text("Input Error: ") + ex.getMessage());
            } catch (SQLException ex) {
                insertForm.setMessageLabel(PresentationText.text("Database Error: ") + ex.getMessage());
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
                ItemType type = ItemType.fromInt(updateForm.getItemType());
                String description = updateForm.getDescription();
                model.updateRow(new ItemSpec(id, type, name, description), null);
                updateForm.setMessageLabel(PresentationText.text("Item spec updated"));
            } catch (IllegalArgumentException ex) {
                updateForm.setMessageLabel(PresentationText.text("Input Error: ") + ex.getMessage());
            } catch (SQLException ex) {
                updateForm.setMessageLabel(PresentationText.text("Database Error: ") + ex.getMessage());
            } catch (RuntimeException ex) {
                updateForm.setMessageLabel(PresentationText.text("Error: ") + ex.getMessage());
            }

        }
    }

    class GenerateDescriptionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            BusyDialog.run(
                    updateForm,
                    "Please wait",
                    "Generating description, please wait...",
                    () -> model.generateDescription(new ItemSpec(
                            Long.parseLong(updateForm.getId()),
                            ItemType.fromInt(updateForm.getItemType()),
                            updateForm.getName(),
                            updateForm.getDescription()
                    )),
                    updateForm::setDescription,
                    ex -> JOptionPane.showMessageDialog(
                            view.getTable(),
                            PresentationText.text("Error during description generation: ") + ex.getMessage(),
                            PresentationText.text("Error"),
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );
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
            BusyDialog.run(
                    view.getTable(),
                    "Please wait",
                    "Scraping price, please wait...",
                    () -> model.price(modelRow),
                    price -> JOptionPane.showMessageDialog(
                            view.getTable(),
                            price + " €",
                            PresentationText.text("Price scraped"),
                            JOptionPane.INFORMATION_MESSAGE
                    ),
                    ex -> JOptionPane.showMessageDialog(
                            view.getTable(),
                            PresentationText.text("Sorry, no price available"),
                            PresentationText.text("Price scraped"),
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );
        }
    }

}
