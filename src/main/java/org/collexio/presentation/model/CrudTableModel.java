package org.collexio.presentation.model;

import org.collexio.business.domain.ItemCollection;
import org.collexio.business.domain.ItemSpec;

import java.io.IOException;
import java.sql.SQLException;

public interface CrudTableModel<T> {
    void removeRow(int row) throws SQLException;
    void addRow(T elem, Long associatedId) throws SQLException, IOException;
    void updateRow(T elem, Long associatedId) throws SQLException, IOException;
    T getRow(int row) throws SQLException, IOException;
}
