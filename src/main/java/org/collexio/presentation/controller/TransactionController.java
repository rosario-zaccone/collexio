package org.collexio.presentation.controller;

import org.collexio.business.domain.Transaction;
import org.collexio.presentation.model.TransactionTableModel;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.transaction.InsertTransactionForm;
import org.collexio.presentation.view.transaction.TransactionPanel;
import org.collexio.presentation.view.transaction.UpdateTransactionForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransactionController {
    private final TransactionTableModel model;
    private final TransactionPanel view;
    private final InsertTransactionForm insertForm;
    private final UpdateTransactionForm updateForm;


    public TransactionController(TransactionTableModel model, TransactionPanel view) {
        this.model = model;
        this.view = view;
        this.view.getAddButton().addActionListener(new InsertButtonListener());
        this.view.getDeleteButton().setAction(new DeleteAction());
        this.view.getFilterField().addActionListener(new FilterListener());
        this.view.getUpdateButton().setAction(new UpdateAction());

        insertForm = this.view.getInsertForm();
        insertForm.getSubmitButton().addActionListener(new InsertFormListener());
        insertForm.getCancelButton().addActionListener(e -> insertForm.dispose());

        updateForm = this.view.getUpdateForm();
        updateForm.getSubmitButton().addActionListener(new UpdateFormListener());
        updateForm.getCancelButton().addActionListener(e -> {
            updateForm.dispose();
            updateForm.clearForm();
        });
    }

    class FilterListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedId = view.getFilterField().getSelectedItem().toString();
            if (selectedId.isEmpty()) {
                view.setFilterField(null);
            } else {

                view.setFilterField(new RowFilter<TransactionTableModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends TransactionTableModel, ? extends Integer> entry) {
                        Long transactionId = (Long) entry.getValue(0);
                        try {
                            Long itemId = model.getItemId(transactionId);
                            return itemId.equals(Long.valueOf(selectedId));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

        }
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
                Long itemId = Long.parseLong(insertForm.getItemId());
                double amount = Double.parseDouble(insertForm.getAmount());
                boolean income = insertForm.getTransactionType() != 0;
                LocalDate date = LocalDate.parse(insertForm.getDate());
                Transaction transaction = new Transaction(amount, income, date);
                model.addRow(transaction, itemId);
                insertForm.setMessageLabel("Transaction inserted");
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

    class UpdateFormListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Long itemId = Long.parseLong(updateForm.getItemId());
                double amount = Double.parseDouble(updateForm.getAmount());
                boolean income = updateForm.getTransactionType() != 0;
                LocalDate date = LocalDate.parse(updateForm.getDate());
                Transaction transaction = new Transaction(amount, income, date);
                Long id = Long.parseLong(updateForm.getId());
                model.updateRow(new Transaction(id, amount, income, date), itemId);
                updateForm.setMessageLabel("Transaction updated");
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

    public class UpdateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            updateForm.setVisible(true);
            updateForm.clearForm();
            int viewRow = Integer.parseInt(e.getActionCommand());
            int modelRow = view.getTable().convertRowIndexToModel(viewRow);
            Transaction transaction = model.getRow(modelRow);
            updateForm.setId(transaction.getId().toString());
            updateForm.setAmount(String.valueOf(transaction.getAmount()));
            updateForm.setDate(transaction.getDate().toString());
            updateForm.setTransactionType(transaction.isIncome() ? 1 : 0);
            Long itemId;
            try {
                itemId = model.getItemId(transaction.getId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            updateForm.setItemId(itemId.toString());
        }
    }

}
