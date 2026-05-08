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
        cards.add(itemCollectionPanel, "collections");
        cards.add(itemSpecPanel, "specs");
        cards.add(itemPanel, "items");
        cards.add(transactionPanel, "transactions");

        add(createTopbar(), BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        select("collections");
    }

    public void selectItemPanel() {
        select("items");
    }

    public void selectTransactionPanel() {
        select("transactions");
    }

    private JPanel createTopbar() {
        JPanel topbar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 56));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 115));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        topbar.setOpaque(false);
        topbar.setPreferredSize(new Dimension(0, 44));
        topbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topbar.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        addNav(topbar, "collections", "◌ Collections");
        addNav(topbar, "specs", "◌ Specs");
        addNav(topbar, "items", "◌ Items");
        addNav(topbar, "transactions", "◌ Transactions");

        return topbar;
    }

    private void addNav(JPanel topbar, String key, String text) {
        JButton button = new NavButton(text);
        button.addActionListener(e -> select(key));
        navigation.put(key, button);
        topbar.add(button);
    }

    private void select(String key) {
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
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setForeground(new Color(0x4a9ab0));
            setHorizontalAlignment(SwingConstants.LEFT);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(110, 28));
            setMargin(new Insets(0, 12, 1, 8));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (Boolean.TRUE.equals(getClientProperty("active"))) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 110, 140, 35));
                g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 6, 22, 22);
                g2.setColor(new Color(255, 255, 255, 145));
                g2.fillRoundRect(1, 1, getWidth() - 5, getHeight() - 5, 22, 22);
                g2.setColor(new Color(255, 255, 255, 190));
                g2.drawRoundRect(1, 1, getWidth() - 6, getHeight() - 6, 22, 22);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }
}
