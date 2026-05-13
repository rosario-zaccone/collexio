package org.collexio.presentation.view.itemspec;

import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.CrudTableModel;
import org.collexio.presentation.view.ButtonColumn;
import org.collexio.presentation.view.MyPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ItemSpecPanel extends MyPanel {
	private final ItemSpecTableModel model;
	private final JTable table;

	private final ButtonColumn deleteButton;
	private final ButtonColumn updateButton;
	private final ButtonColumn priceButton;
	private final JButton addButton;
	private final InsertSpecForm insertForm;
	private final UpdateSpecForm updateSpecForm;

	public ItemSpecPanel(ItemSpecTableModel model) {
		this.model = model;
        table = createTable(model);
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
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

		priceButton = new ButtonColumn(table, null, 4);
		updateButton = new ButtonColumn(table, null, 5);
		deleteButton = new ButtonColumn(table, null, 6);
		this.insertForm = new InsertSpecForm();
		this.updateSpecForm = new UpdateSpecForm();

        JScrollPane scrollPane = createScrollPane(table, 700, 300);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        addButton = createButton("+ Add");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 15f));
        addButton.setPreferredSize(new Dimension(0, 58));

        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(addButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

	}

	public ItemSpecTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public InsertSpecForm getInsertForm() {
		return insertForm;
	}

	public UpdateSpecForm getUpdateSpecForm() {
		return updateSpecForm;
	}

	public ButtonColumn getPriceButton() {
		return priceButton;
	}

	public ButtonColumn getDeleteButton() {
		return deleteButton;
	}

	public ButtonColumn getUpdateButton() {
		return updateButton;
	}
}
