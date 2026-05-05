package org.collexio.presentation.view.transaction;

import org.collexio.business.domain.Transaction;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.model.TransactionTableModel;
import org.collexio.presentation.view.ButtonColumn;
import org.collexio.presentation.view.MyPanel;
import org.collexio.presentation.view.item.InsertItemForm;
import org.collexio.presentation.view.item.UpdateItemForm;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;

// wallet area ?
public class TransactionPanel extends MyPanel {
	private final TransactionTableModel model;
	private final JTable table;
	private final ItemTableModel itemModel;
	private final ButtonColumn deleteButton;
	private final ButtonColumn updateButton;
	private final JButton addButton;
	private final InsertTransactionForm insertForm;
	private final UpdateTransactionForm updateForm;
	private final JComboBox<String> filterField;

	TableRowSorter<TransactionTableModel> filter;

	public TransactionPanel(TransactionTableModel model, ItemTableModel itemModel) {
		this.model = model;
		this.itemModel = itemModel;
		filterField = new JComboBox<String>();

		table = createTable(model);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
														   boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (c instanceof JComponent jc) {
					jc.setToolTipText(value != null
							? "<html><body style='width: 300px;'>" + value + "</body></html>"
							: null);
				}
				return c;
			}
		});
		filter = new TableRowSorter<>(this.model);
		table.setRowSorter(filter);
		refreshFilter();
		this.insertForm = new InsertTransactionForm();
		this.updateForm = new UpdateTransactionForm();

		updateButton = new ButtonColumn(table, null, 5);
		deleteButton = new ButtonColumn(table, null, 6);

		JPanel filterPanel = createFilterBar(filterField, "Filter by Item ID:");

		JScrollPane scrollPane = createScrollPane(table, 700, 300);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		addButton = createButton("+ Add");
		addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));
		addButton.setPreferredSize(new Dimension(0, 50));

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(filterPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(addButton, BorderLayout.SOUTH);
	}

	public TransactionTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public InsertTransactionForm getInsertForm() {
		return insertForm;
	}

	public UpdateTransactionForm getUpdateForm() {
		return updateForm;
	}

	public ButtonColumn getDeleteButton() {
		return deleteButton;
	}

	public ButtonColumn getUpdateButton() {
		return updateButton;
	}

	public JComboBox<String> getFilterField() {
		return filterField;
	}

	public void setFilterField(RowFilter<TransactionTableModel, Integer> rf) {
		filter.setRowFilter(rf);
	}

	public void setItemIdForFilter(String id) {
		filterField.setSelectedItem(id);
	}

	public void refreshFilter() {
		try {
			var ids = new java.util.ArrayList<>(itemModel.getItemIds().stream().map(Object::toString).toList());
			ids.add(0, "");
			filterField.setModel(new DefaultComboBoxModel<>(ids.toArray(String[]::new)));
		} catch (SQLException e) {
			filterField.setModel(new DefaultComboBoxModel<>(new String[] { "" }));
		}
	}
}
