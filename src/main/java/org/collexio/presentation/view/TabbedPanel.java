package org.collexio.presentation.view;

import org.collexio.presentation.model.*;
import org.collexio.presentation.view.item.ItemPanel;
import org.collexio.presentation.view.itemcollection.ItemCollectionPanel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;
import org.collexio.presentation.view.transaction.TransactionPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TabbedPanel extends JPanel {
    private final JPanel cards;
    private final CardLayout cardLayout;
    private final Map<String, JButton> navigation = new LinkedHashMap<>();
    private final ItemCollectionPanel itemCollectionPanel;
    private final ItemPanel itemPanel;
    private final ItemSpecPanel itemSpecPanel;
    private final TransactionPanel transactionPanel;

    private static final String TAB_COLLECTIONS = "collections";
    private static final String TAB_SPECS = "specs";
    private static final String TAB_ITEMS = "items";
    private static final String TAB_TRANSACTIONS = "transactions";

    public TabbedPanel(
            ItemCollectionTableModel itemCollectionModel,
            ItemSpecTableModel itemSpecModel,
            ItemTableModel itemModel,
            TransactionTableModel transactionModel
    ) throws SQLException, IOException {
        super(new BorderLayout());
        setOpaque(false);

        itemCollectionPanel = new ItemCollectionPanel(itemCollectionModel);
        itemSpecPanel = new ItemSpecPanel(itemSpecModel);
        itemPanel = new ItemPanel(itemModel, itemCollectionModel);
        transactionPanel = new TransactionPanel(transactionModel, itemModel);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setOpaque(false);
        cards.add(itemCollectionPanel, TAB_COLLECTIONS);
        cards.add(itemSpecPanel, TAB_SPECS);
        cards.add(itemPanel, TAB_ITEMS);
        cards.add(transactionPanel, TAB_TRANSACTIONS);

        add(createTopbar(), BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        select(TAB_COLLECTIONS);
    }

    public void selectItemPanel() {
        select(TAB_ITEMS);
    }

    public void selectTransactionPanel() {
        select(TAB_TRANSACTIONS);
    }

    private JPanel createTopbar() {
        JPanel topbar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (MyPanel.isFlatTheme()) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(MyPanel.SURFACE);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(
                            0,
                            0,
                            new Color(0xff, 0xff, 0xff, 210),
                            0,
                            getHeight(),
                            MyPanel.SURFACE_SOFT
                    ));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setPaint(new GradientPaint(
                            0,
                            getHeight() / 2,
                            new Color(0x63, 0xc9, 0x6d, 70),
                            0,
                            getHeight(),
                            new Color(0x35, 0xba, 0xf4, 45)
                    ));
                    g2.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        topbar.setOpaque(false);
        topbar.setPreferredSize(new Dimension(0, MyPanel.isFlatTheme() ? 48 : 42));
        topbar.setLayout(new FlowLayout(
                FlowLayout.LEFT,
                MyPanel.isFlatTheme() ? 6 : 3,
                MyPanel.isFlatTheme() ? 10 : 8
        ));
        topbar.setBorder(BorderFactory.createEmptyBorder(0, MyPanel.isFlatTheme() ? 24 : 12, 0, 24));

        addNav(topbar, TAB_COLLECTIONS, "Collections");
        addNav(topbar, TAB_SPECS, "Specs");
        addNav(topbar, TAB_ITEMS, "Items");
        addNav(topbar, TAB_TRANSACTIONS, "Transactions");

        return topbar;
    }

    private void addNav(JPanel topbar, String key, String text) {
        JButton button = new NavButton(PresentationText.text(text));
        button.putClientProperty("text.key", text);
        button.addActionListener(e -> select(key));
        navigation.put(key, button);
        topbar.add(button);
    }

    public void updateLanguage() {
        navigation.forEach((name, button) -> {
            Object key = button.getClientProperty("text.key");
            if (key instanceof String textKey) {
                button.setText(PresentationText.text(textKey));
            }
        });
        updateTableLanguage(itemCollectionPanel.getTable());
        updateTableLanguage(itemSpecPanel.getTable());
        updateTableLanguage(itemPanel.getTable());
        updateTableLanguage(transactionPanel.getTable());
    }

    private void updateTableLanguage(JTable table) {
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            int modelIndex = table.getColumnModel().getColumn(i).getModelIndex();
            table.getColumnModel().getColumn(i).setHeaderValue(table.getModel().getColumnName(modelIndex));
        }
        table.getTableHeader().repaint();
        table.repaint();
    }

    private void select(String key) {
        try {
            switch (key) {
                case TAB_COLLECTIONS -> itemCollectionPanel.getModel().refresh();
                case TAB_SPECS -> itemSpecPanel.getModel().refresh();
                case TAB_ITEMS -> itemPanel.getModel().refresh();
                case TAB_TRANSACTIONS -> transactionPanel.getModel().refresh();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    PresentationText.text("Error refreshing data: ") + e.getMessage(),
                    PresentationText.text("Database Error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }

        cardLayout.show(cards, key);
        navigation.forEach((name, button) -> {
            boolean active = name.equals(key);
            button.putClientProperty("active", active);
            button.repaint();
        });
    }

    public ItemCollectionPanel getItemCollectionPanel() {
        return itemCollectionPanel;
    }

    public ItemPanel getItemPanel() {
        return itemPanel;
    }

    public ItemSpecPanel getItemSpecPanel() {
        return itemSpecPanel;
    }

    public TransactionPanel getTransactionPanel() {
        return transactionPanel;
    }

    private static class NavButton extends JButton {
        NavButton(String text) {
            super(text);
            setFont(MyPanel.FONT_BOLD);
            setForeground(MyPanel.TEXT_MUTED);
            setHorizontalAlignment(SwingConstants.CENTER);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(MyPanel.isFlatTheme() ? new Dimension(142, 38) : new Dimension(132, 30));
            setMargin(MyPanel.isFlatTheme() ? new Insets(0, 14, 1, 14) : new Insets(0, 10, 1, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            boolean active = Boolean.TRUE.equals(getClientProperty("active"));
            Graphics2D g2 = (Graphics2D) g.create();
            if (MyPanel.isFlatTheme()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setColor(MyPanel.LINK_BLUE);
                    g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 14, 14);
                    setForeground(Color.WHITE);
                } else {
                    g2.setColor(MyPanel.SURFACE_SOFT);
                    g2.fillRoundRect(0, 4, getWidth(), getHeight() - 8, 14, 14);
                    setForeground(MyPanel.TEXT_MUTED);
                }
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setPaint(new GradientPaint(0, 0, new Color(0x4ed1ff), 0, getHeight(), MyPanel.HABBO_BLUE));
                    g2.fillRoundRect(0, 2, getWidth() - 1, getHeight() - 3, 18, 18);
                    g2.setPaint(new GradientPaint(
                            0,
                            3,
                            new Color(0xff, 0xff, 0xff, 170),
                            0,
                            Math.max(5, getHeight() / 2),
                            new Color(0xff, 0xff, 0xff, 30)
                    ));
                    g2.fillRoundRect(3, 4, getWidth() - 7, Math.max(8, getHeight() / 2), 16, 16);
                    setForeground(Color.WHITE);
                } else {
                    g2.setPaint(new GradientPaint(
                            0,
                            4,
                            new Color(0xff, 0xff, 0xff, 170),
                            0,
                            getHeight(),
                            new Color(0xd8, 0xf5, 0xfb, 160)
                    ));
                    g2.fillRoundRect(0, 6, getWidth() - 1, getHeight() - 8, 18, 18);
                    setForeground(MyPanel.TEXT_MUTED);
                }
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
