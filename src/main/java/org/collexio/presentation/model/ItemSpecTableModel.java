package org.collexio.presentation.model;

import org.collexio.business.domain.ItemSpec;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ItemSpecTableModel extends AbstractTableModel {
    private final static int COLUMNS = 4;
    private final String[][] data;

    public ItemSpecTableModel(List<ItemSpec> specs) {
        data = new String[specs.size()][COLUMNS];

        data[0][0] = "Id"; data[0][1] = "Type"; data[0][2] = "Name"; data[0][3] = "Description";
        for (int i = 0; i < specs.size(); i++) {
            ItemSpec spec = specs.get(i);
            for (int j = 0; j < COLUMNS; j++) {
                data[i][j] = switch(j) {
                    case 0 -> spec.getId().toString();
                    case 1 -> spec.getType().toString();
                    case 2 -> spec.getName();
                    case 3 -> spec.getDescription();
                    default -> "";
                };
            }
        }
    }


    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return data[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Id";
            case 1 -> "Type";
            case 2 -> "Name";
            case 3 -> "Description";
            default -> "";
        };
    }
}
