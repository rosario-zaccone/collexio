package org.collexio.presentation.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public abstract class MyPanel extends JPanel {

    protected static final Color BG_TOP = new Color(0xb8e4f7);
    protected static final Color BG_BOTTOM = new Color(0xc8f0e0);
    protected static final Color GLASS = new Color(255, 255, 255, 90);
    protected static final Color GLASS_STRONG = new Color(255, 255, 255, 150);
    protected static final Color GLASS_BORDER = new Color(255, 255, 255, 155);
    protected static final Color TEXT = new Color(0x0a3a5a);
    protected static final Color TEXT_MUTED = new Color(0x4a7a8a);
    protected static final Color TEAL = new Color(0x64bedd);
    protected static final Color SKY = new Color(0x3ca0d2);
    protected static final Color MINT = new Color(0x9fe6c8);
    protected static final Color CORAL = new Color(0xff8f86);
    protected static final Color ROW_SELECTED = new Color(120, 210, 255, 52);
    protected static final Font FONT = new Font("Segoe UI", Font.PLAIN, 12);
    protected static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    protected static final Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 10);
    protected static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 13);

    protected MyPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
    }

    protected JPanel createTitleBar(String title) {
        JPanel bar = new GlassPanel(10, new Color(255, 255, 255, 80));
        bar.setLayout(new BorderLayout());
        bar.setPreferredSize(new Dimension(0, 38));

        JLabel label = new JLabel(title);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        bar.add(label, BorderLayout.CENTER);
        return bar;
    }

    protected JTable createTable(TableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                boolean selected = isRowSelected(row);

                c.setFont(col == 0 || col == 2 ? FONT_BOLD : FONT);
                c.setForeground(col == 3 ? TEXT_MUTED : TEXT);
                c.setBackground(selected ? ROW_SELECTED : new Color(255, 255, 255, 185));
                boolean badge = false;

                if (c instanceof JLabel label) {
                    label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    label.setVerticalAlignment(SwingConstants.CENTER);
                }

                if (isStatusValue(getValueAt(row, col))) {
                    c = createBadge(getValueAt(row, col).toString(), isActiveValue(getValueAt(row, col)));
                    badge = true;
                } else if (col == 0) {
                    c = createAvatarCell(getValueAt(row, col), selected);
                }

                if (c instanceof JComponent jc && !badge) {
                    jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 90)));
                }
                return c;
            }
        };

        table.setOpaque(false);
        table.setRowHeight(42);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(FONT);
        table.setForeground(TEXT);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);

        applyHeader(table.getTableHeader());
        return table;
    }

    protected JScrollPane createScrollPane(JTable table, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new RoundedGlassBorder(10, GLASS_BORDER));
        scrollPane.setBackground(GLASS);
        scrollPane.setPreferredSize(new Dimension(width, height));
        return scrollPane;
    }

    protected JButton createButton(String text) {
        JButton button = createAeroButton(text, new Color(255, 255, 255), MINT);
        button.setPreferredSize(new Dimension(0, 40));
        return button;
    }

    protected JPanel createFilterBar(JComboBox<String> combo, String labelText) {
        JPanel bar = new GlassPanel(12, new Color(255, 255, 255, 80));
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 9));

        JLabel label = new JLabel(labelText);
        label.setFont(FONT_SMALL_BOLD);
        label.setForeground(TEXT_MUTED);

        combo.setFont(FONT);
        combo.setForeground(TEXT);
        combo.setBackground(new Color(255, 255, 255, 165));
        combo.setBorder(new RoundedGlassBorder(20, new Color(255, 255, 255, 205)));
        combo.setPreferredSize(new Dimension(190, 30));

        bar.add(label);
        bar.add(combo);
        return bar;
    }

    protected static JButton createAeroButton(String text, Color top, Color bottom) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int shift = getModel().isPressed() ? 1 : 0;
                int w = getWidth() - 4;
                int h = getHeight() - 5;

                g2.setColor(new Color(30, 110, 140, 35));
                g2.fillRoundRect(3, 4, w, h, 22, 22);

                Color actualTop = getClientProperty("aero.top") instanceof Color cTop ? cTop : top;
                Color actualBottom = getClientProperty("aero.bottom") instanceof Color cBottom ? cBottom : bottom;
                g2.setPaint(new GradientPaint(0, shift, actualTop, 0, h + shift, actualBottom));
                g2.fillRoundRect(1 + shift, 1 + shift, w, h, 22, 22);
                g2.setColor(new Color(255, 255, 255, 215));
                g2.drawRoundRect(1 + shift, 1 + shift, w - 1, h - 1, 22, 22);
                g2.setColor(new Color(actualBottom.getRed(), actualBottom.getGreen(), actualBottom.getBlue(), 115));
                g2.drawLine(10 + shift, h - 2 + shift, w - 9 + shift, h - 2 + shift);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BOLD);
        button.setForeground(TEXT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(0, 14, 1, 14));
        return button;
    }

    protected static JPanel createGlassPanel(int radius) {
        return new GlassPanel(radius, GLASS);
    }

    protected static Border createGlassBorder(int radius) {
        return new RoundedGlassBorder(radius, GLASS_BORDER);
    }

    protected static void paintAeroBackground(Graphics2D g2, int width, int height) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, width, height, BG_BOTTOM));
        g2.fillRect(0, 0, width, height);

        g2.setColor(new Color(100, 210, 180, 72));
        g2.fillOval(width - 260, -90, 340, 260);
        g2.setColor(new Color(90, 180, 230, 56));
        g2.fillOval(-130, height - 230, 330, 270);
    }

    private void applyHeader(JTableHeader header) {
        header.setDefaultRenderer((table, value, selected, focus, row, col) -> new HeaderLabel(
                value != null ? value.toString().toUpperCase() : ""
        ));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 30));
    }

    private Component createAvatarCell(Object value, boolean selected) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 9));
        panel.setOpaque(true);
        panel.setBackground(selected ? ROW_SELECTED : new Color(255, 255, 255, 185));
        panel.add(new AvatarLabel(value != null ? value.toString() : ""));
        return panel;
    }

    private Component createBadge(String value, boolean active) {
        JLabel label = new JLabel(value);
        label.setFont(FONT_SMALL_BOLD);
        label.setForeground(active ? new Color(0x267a4a) : new Color(0x6f7f88));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(false);
        label.setBorder(new BadgeBorder(active));
        return label;
    }

    private boolean isStatusValue(Object value) {
        if (value == null) {
            return false;
        }
        String text = value.toString().trim().toLowerCase();
        return text.equals("active") || text.equals("inactive");
    }

    private boolean isActiveValue(Object value) {
        return value != null && value.toString().trim().equalsIgnoreCase("active");
    }

    private static class GlassPanel extends JPanel {
        private final int radius;
        private final Color fill;

        GlassPanel(int radius, Color fill) {
            this.radius = radius;
            this.fill = fill;
            setOpaque(false);
            setBorder(new RoundedGlassBorder(radius, GLASS_BORDER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class RoundedGlassBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        RoundedGlassBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = 1;
            insets.left = 1;
            insets.bottom = 1;
            insets.right = 1;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    private static class HeaderLabel extends JLabel {
        HeaderLabel(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 9));
            setForeground(new Color(0x2a6a8a));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(0xd8f0ff)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class AvatarLabel extends JLabel {
        AvatarLabel(String value) {
            super(initials(value));
            setFont(FONT_SMALL_BOLD);
            setForeground(TEXT);
            setHorizontalAlignment(SwingConstants.CENTER);
            setPreferredSize(new Dimension(22, 22));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(100, 210, 180, 90));
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(new Color(255, 255, 255, 160));
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.dispose();
            super.paintComponent(g);
        }

        private static String initials(String value) {
            String trimmed = value.trim();
            if (trimmed.isEmpty()) {
                return "?";
            }
            return trimmed.substring(0, Math.min(2, trimmed.length())).toUpperCase();
        }
    }

    private static class BadgeBorder extends AbstractBorder {
        private final boolean active;

        BadgeBorder(boolean active) {
            this.active = active;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 10, 5, 10);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = 5;
            insets.left = 10;
            insets.bottom = 5;
            insets.right = 10;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(active ? new Color(80, 210, 140, 78) : new Color(140, 160, 170, 70));
            g2.fillRoundRect(x, y + 2, width - 1, height - 5, 18, 18);
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRoundRect(x, y + 2, width - 1, height - 5, 18, 18);
            g2.dispose();
        }
    }
}
