package org.collexio.presentation.view.itemcollection;

import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.view.ButtonColumn;
import org.collexio.presentation.view.MyPanel;

import javax.swing.*;
import java.awt.*;

public class ItemCollectionPanel extends MyPanel {
	private final ItemCollectionTableModel model;
	private final JTable table;

	private final ButtonColumn deleteButton;
	private final ButtonColumn updateButton;
	private final ButtonColumn itemsButton;
	private final JButton addButton;
	private final InsertItemCollectionForm insertForm;
	private final UpdateItemCollectionForm updateForm;

	public ItemCollectionPanel(ItemCollectionTableModel model) {
		this.model = model;
		table = createTable(model);
		this.insertForm = new InsertItemCollectionForm();
		this.updateForm = new UpdateItemCollectionForm();

		itemsButton = new ButtonColumn(table, null, 3);
		updateButton = new ButtonColumn(table, null, 4);
		deleteButton = new ButtonColumn(table, null, 5);

		JScrollPane scrollPane = createScrollPane(table, 700, 300);
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

		addButton = createButton("+ Add");
		addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		addButton.setFont(addButton.getFont().deriveFont(Font.BOLD, 14f));

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		add(scrollPane, BorderLayout.CENTER);
		add(addButton, BorderLayout.SOUTH);
		addButton.setPreferredSize(new Dimension(0, 50));
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

	}

	public ItemCollectionTableModel getModel() {
		return model;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getAddButton() {
		return addButton;
	}

	public InsertItemCollectionForm getInsertForm() {
		return insertForm;
	}

	public UpdateItemCollectionForm getUpdateForm() {
		return updateForm;
	}

	public ButtonColumn getItemsButton() {
		return itemsButton;
	}

	public ButtonColumn getDeleteButton() {
		return deleteButton;
	}

	public ButtonColumn getUpdateButton() {
		return updateButton;
	}
}
