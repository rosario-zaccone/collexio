package org.collexio.presentation.controller;

import org.collexio.business.domain.Item;
import org.collexio.business.domain.ItemPhoto;
import org.collexio.business.domain.ItemSpec;
import org.collexio.persistence.entity.ItemStatus;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.view.item.InsertItemForm;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.item.UpdateItemForm;
import org.collexio.presentation.view.transaction.TransactionPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ItemController {
    private final ItemTableModel model;
    private final ItemPanel view;
    private final TransactionPanel transactionView;
    private final TabSwitchListener tabSwitchListener;
    private final InsertItemForm insertForm;
    private final UpdateItemForm updateForm;


    public ItemController(ItemTableModel model, ItemPanel view, TransactionPanel transactionView, TabSwitchListener tabSwitchListener) {
        this.model = model;
        this.view = view;
        this.tabSwitchListener = tabSwitchListener;
        this.transactionView = transactionView;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        this.view.getUpdateButton().setAction(new UpdateAction());
        this.view.getFilterField().addActionListener(new FilterListener());
        this.view.getPrintButton().addActionListener(new PrintListener());
        this.view.getTransactionsButton().setAction(new TransactionsAction());

        insertForm = this.view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> insertForm.dispose());
        this.view.getPriceButton().setAction(new PriceAction());


        updateForm = this.view.getUpdateForm();
        updateForm.getSubmitButton().addActionListener(new UpdateFormListener());
        updateForm.getCancelButton().addActionListener(e -> {
            updateForm.dispose();
            updateForm.clearForm();
        });
    }

    class PrintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.getTable().print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(
                        view.getTable(),
                        "Error",
                        "Printer Error " + ex.getMessage(),
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }

    class InsertButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            insertForm.setVisible(true);
            insertForm.clearForm();
        }
    }

    class FilterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedId = view.getFilterField().getSelectedItem().toString();
            if (selectedId.isEmpty()) {
                view.setFilterField(null);
            } else {

                view.setFilterField(new RowFilter<ItemTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ItemTableModel, ? extends Integer> entry) {
                        Long itemId = (Long) entry.getValue(0);
                        try {
                            Optional<Long> collectionId = model.getCollectionId(itemId);
                            if (collectionId.isEmpty())
                                return false;
                            else
                                return collectionId.get().equals(Long.valueOf(selectedId));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

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
                model.addRow(new Item(status, photo, spec), collectionId);
                insertForm.setMessageLabel("Item spec inserted");
            } catch (IllegalArgumentException ex) {
                insertForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                insertForm.setMessageLabel("Database Error: " + ex.getMessage());
            } catch (IOException ex) {
                insertForm.setMessageLabel("IO Error: " + ex.getMessage());
            } catch (RuntimeException ex) {
                insertForm.setMessageLabel("Error: " + ex.getMessage());
            }

        }
    }

    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long id = Long.parseLong(updateForm.getId());
                Long collectionId = Long.parseLong(updateForm.getCollection());
                Long specId = Long.parseLong(updateForm.getSpec());
                ItemStatus status = ItemStatus.fromInt(updateForm.getStatus());
                Long photoId = Long.parseLong(updateForm.getPhotoId());
                Path path = Path.of(updateForm.getPhotoPath());
                LocalDate date = LocalDate.parse(updateForm.getPhotoDate());
                ItemSpec spec = model.getSpec(specId);
                model.updateRow(new Item(id, status, new ItemPhoto(photoId, path, date), spec), collectionId);
                updateForm.setMessageLabel("Item spec updated");
            } catch (IllegalArgumentException ex) {
                updateForm.setMessageLabel("Input Error: " + ex.getMessage());
            } catch (SQLException ex) {
                updateForm.setMessageLabel("Database Error: " + ex.getMessage());
            } catch (IOException ex) {
                updateForm.setMessageLabel("IO Error: " + ex.getMessage());
            } catch (RuntimeException ex) {
                updateForm.setMessageLabel("Error: " + ex.getMessage());
            }

        }
    }


    public class TransactionsAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (tabSwitchListener != null) {
                tabSwitchListener.switchToTab();
                int viewRow = Integer.parseInt(e.getActionCommand());
                int modelRow = view.getTable().convertRowIndexToModel(viewRow);
                Item item = model.getRow(modelRow);
                Long itemId = item.getId(); // set the filter to this id
                transactionView.setItemIdForFilter(itemId.toString()); // ,MPDIFU
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
            updateForm.clearForm();
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);
            Item item = model.getRow(modelRow);
            updateForm.setId(item.getId().toString());
            Optional<Long> collection;
            try {
                collection = model.getCollectionId(item.getId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (collection.isEmpty())
                updateForm.setCollection("");
            else
                updateForm.setCollection(collection.get().toString());
            updateForm.setSpec(item.getSpec().getId().toString());
            updateForm.setStatus(item.getStatus().getValue());
            updateForm.setPhotoDate(item.getPhoto().getDate().toString());
            updateForm.setPhotoPath(item.getPhoto().getPath().toString());
            updateForm.setPhotoId(item.getPhoto().getId().toString());
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

