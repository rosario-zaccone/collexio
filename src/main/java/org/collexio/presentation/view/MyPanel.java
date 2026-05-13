package org.collexio.presentation.view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.function.IntFunction;

public abstract class MyPanel extends JPanel {

    public enum ThemeStyle {
        YEAR_2000,
        FLAT
    }

    private static ThemeStyle themeStyle = ThemeStyle.FLAT;

    protected static Color BG_BOTTOM = new Color(0x8eddf2);
    protected static Color SURFACE = new Color(0xeaf8ff);
    protected static Color SURFACE_SOFT = new Color(0xd9f4ed);
    protected static Color FIELD = Color.WHITE;
    protected static Color HABBO_BLUE = new Color(0x168ad8);
    protected static Color HABBO_BLUE_DARK = new Color(0x075d96);
    protected static Color HABBO_BORDER = new Color(0x79c6df);
    protected static Color TEXT = new Color(0x143448);
    protected static Color TEXT_MUTED = new Color(0x4a6978);
    protected static Color LINK_BLUE = new Color(0x168ad8);
    protected static Color OTHER_AZURE = new Color(0x4ebf63);
    protected static Color UPDATE_YELLOW = new Color(0x2d9fe0);
    protected static Color DELETE_RED = new Color(0xd94242);
    protected static Color ROW_SELECTED = new Color(0xcff5ff);
    protected static Font FONT = new Font("Segoe UI", Font.PLAIN, 12);
    protected static Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    protected static Font FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
    protected static Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);

    static {
        setThemeStyle(themeStyle);
    }

    protected MyPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
    }

    public static ThemeStyle getThemeStyle() {
        return themeStyle;
    }

    public static boolean isFlatTheme() {
        return themeStyle == ThemeStyle.FLAT;
    }

    public static void setThemeStyle(ThemeStyle style) {
        themeStyle = style;
        if (style == ThemeStyle.FLAT) {
            BG_BOTTOM = new Color(0xf7f7f8);
            SURFACE = Color.WHITE;
            SURFACE_SOFT = new Color(0xf1f1f4);
            FIELD = new Color(0xf8f8fa);
            HABBO_BLUE = new Color(0x7450e8);
            HABBO_BLUE_DARK = new Color(0x5630bf);
            HABBO_BORDER = new Color(0x000000, true);
            TEXT = new Color(0x25232d);
            TEXT_MUTED = new Color(0x77727f);
            LINK_BLUE = new Color(0x7450e8);
            OTHER_AZURE = new Color(0x7450e8);
            UPDATE_YELLOW = new Color(0x7450e8);
            DELETE_RED = new Color(0xd94655);
            ROW_SELECTED = new Color(0xeee9ff);
            FONT = new Font("Dialog", Font.PLAIN, 12);
            FONT_BOLD = new Font("Dialog", Font.BOLD, 12);
            FONT_SMALL_BOLD = new Font("Dialog", Font.BOLD, 11);
            FONT_TITLE = new Font("Dialog", Font.BOLD, 14);
        } else {
            BG_BOTTOM = new Color(0x8eddf2);
            SURFACE = new Color(0xeaf8ff);
            SURFACE_SOFT = new Color(0xd9f4ed);
            FIELD = Color.WHITE;
            HABBO_BLUE = new Color(0x168ad8);
            HABBO_BLUE_DARK = new Color(0x075d96);
            HABBO_BORDER = new Color(0x79c6df);
            TEXT = new Color(0x143448);
            TEXT_MUTED = new Color(0x4a6978);
            LINK_BLUE = new Color(0x168ad8);
            OTHER_AZURE = new Color(0x4ebf63);
            UPDATE_YELLOW = new Color(0x2d9fe0);
            DELETE_RED = new Color(0xd94242);
            ROW_SELECTED = new Color(0xcff5ff);
            FONT = new Font("Segoe UI", Font.PLAIN, 12);
            FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);
            FONT_SMALL_BOLD = new Font("Segoe UI", Font.BOLD, 11);
            FONT_TITLE = new Font("Segoe UI", Font.BOLD, 14);
        }
    }

    protected static Color tableGridColor() {
        return isFlatTheme() ? new Color(0x000000, true) : new Color(0xa8dfee);
    }

    protected static Color neutralButtonColor() {
        return isFlatTheme() ? new Color(0x8a8792) : new Color(0x5fbf6f);
    }

    protected Color getRowBackground(JTable table, int row) {
        if (isFlatTheme()) {
            return row % 2 == 0 ? SURFACE : SURFACE_SOFT;
        }
        return row % 2 == 0 ? new Color(0xf7fdff) : new Color(0xe9f8fb);
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
    }

    protected JPanel createTitleBar(String title) {
        JPanel bar = new JPanel();
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 0));
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
                Color rowBackground = selected ? ROW_SELECTED : getRowBackground(this, row);
                c.setBackground(rowBackground);
                boolean badge = false;

                if (c instanceof JLabel label) {
                    label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }

                if (isStatusValue(getValueAt(row, col))) {
                    c = createBadge(
                            getValueAt(row, col).toString(),
                            isActiveValue(getValueAt(row, col)),
                            rowBackground
                    );
                    badge = true;
                } else if (col == 0) {
                    c = createAvatarCell(getValueAt(row, col), rowBackground);
                }

                if (c instanceof JComponent jc && !badge && !isFlatTheme()) {
                    jc.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, tableGridColor()));
                }
                return c;
            }
        };

        table.setOpaque(true);
        table.setBackground(isFlatTheme() ? SURFACE : Color.WHITE);
        table.setRowHeight(isFlatTheme() ? 62 : 64);
        table.setShowGrid(!isFlatTheme());
        table.setGridColor(tableGridColor());
        table.setIntercellSpacing(isFlatTheme() ? new Dimension(0, 0) : new Dimension(1, 1));
        table.setFont(FONT);
        table.setForeground(TEXT);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        table.putClientProperty("rowBackgroundProvider", (IntFunction<Color>) row -> getRowBackground(table, row));

        applyHeader(table.getTableHeader());
        return table;
    }

    protected JScrollPane createScrollPane(JTable table, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(isFlatTheme() ? SURFACE : Color.WHITE);
        scrollPane.setBorder(isFlatTheme()
                ? BorderFactory.createEmptyBorder()
                : new RoundedGlassBorder(18, HABBO_BORDER));
        scrollPane.setBackground(isFlatTheme() ? SURFACE : new Color(0xd9f4ed));
        scrollPane.setPreferredSize(new Dimension(width, height));
        return scrollPane;
    }

    protected JButton createButton(String text) {
        JButton button = createAeroButton(PresentationText.text(text), Color.WHITE, buttonColorFor(text));
        button.putClientProperty("text.key", text);
        button.setPreferredSize(new Dimension(0, isFlatTheme() ? 54 : 46));
        return button;
    }

    protected JPanel createFilterBar(JComboBox<String> combo, String labelText) {
        JPanel bar = new GlassPanel(isFlatTheme() ? 8 : 2);
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, isFlatTheme() ? 10 : 7));
        bar.setPreferredSize(new Dimension(0, isFlatTheme() ? 56 : 46));

        JLabel label = new JLabel(PresentationText.text(labelText));
        label.putClientProperty("text.key", labelText);
        label.setFont(FONT_SMALL_BOLD);
        label.setForeground(TEXT_MUTED);

        combo.setFont(FONT);
        combo.setForeground(TEXT);
        combo.setBackground(FIELD);
        combo.setBorder(new RoundedGlassBorder(12, HABBO_BORDER));
        combo.setPreferredSize(new Dimension(200, isFlatTheme() ? 34 : 30));

        bar.add(label);
        bar.add(combo);
        return bar;
    }

    protected JPanel createEmptyFilterBar() {
        JPanel bar = new GlassPanel(isFlatTheme() ? 8 : 2);
        bar.setPreferredSize(new Dimension(0, isFlatTheme() ? 56 : 46));
        return bar;
    }

    protected static JButton createAeroButton(String text, Color top, Color bottom) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                int shift = getModel().isPressed() ? 2 : 0;
                Color color = getClientProperty("button.color") instanceof Color clientColor ? clientColor : bottom;
                if (getModel().isRollover()) {
                    color = brighten(color);
                }
                if (isFlatTheme()) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(color);
                    g2.fillRoundRect(shift, shift, getWidth() - 1 - shift, getHeight() - 1 - shift, 12, 12);
                } else {
                    int width = getWidth() - 1 - shift;
                    int height = getHeight() - 1 - shift;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(
                            0,
                            shift,
                            brighten(color, 74),
                            0,
                            getHeight(),
                            darken(color, 22)
                    ));
                    g2.fillRoundRect(shift, shift, width, height, 18, 18);
                    g2.setPaint(new GradientPaint(
                            0,
                            shift + 1,
                            new Color(0xff, 0xff, 0xff, 170),
                            0,
                            Math.max(2, getHeight() / 2),
                            new Color(0xff, 0xff, 0xff, 35)
                    ));
                    g2.fillRoundRect(shift + 2, shift + 2, width - 4, Math.max(8, height / 2), 16, 16);
                    g2.setColor(new Color(0xff, 0xff, 0xff, 155));
                    g2.drawRoundRect(shift + 1, shift + 1, width - 2, height - 2, 17, 17);
                    g2.setColor(borderFor(color));
                    g2.drawRoundRect(shift, shift, width, height, 18, 18);
                }
                g2.dispose();
                setForeground(Color.WHITE);
                super.paintComponent(g);
            }
        };

        button.setFont(FONT_BOLD);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(isFlatTheme() ? new Insets(0, 14, 1, 14) : new Insets(0, 12, 2, 12));
        return button;
    }

    public static Color actionColorFor(String text) {
        return buttonColorFor(text);
    }

    protected static JPanel createGlassPanel(int radius) {
        return new GlassPanel(radius);
    }

    protected static Border createGlassBorder(int radius) {
        return new RoundedGlassBorder(radius, HABBO_BORDER);
    }

    protected static void paintAeroBackground(Graphics2D g2, int width, int height) {
        if (isFlatTheme()) {
            g2.setColor(BG_BOTTOM);
            g2.fillRect(0, 0, width, height);
            return;
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(0x4aa4ec), 0, height, BG_BOTTOM));
        g2.fillRect(0, 0, width, height);
        g2.setPaint(new GradientPaint(
                0,
                Math.max(1, height / 2),
                new Color(0x63, 0xc9, 0x6d, 110),
                0,
                height,
                new Color(0x2f, 0xae, 0x66, 165)
        ));
        g2.fillRoundRect(-80, Math.max(0, height / 2), width + 160, height, 90, 90);
        g2.setPaint(new GradientPaint(
                0,
                0,
                new Color(0xff, 0xff, 0xff, 130),
                0,
                Math.max(1, height / 3),
                new Color(0xff, 0xff, 0xff, 15)
        ));
        g2.fillRect(0, 0, width, Math.max(1, height / 3));
    }

    private static Color buttonColorFor(String text) {
        String normalized = text == null ? "" : text.toLowerCase();
        if (normalized.contains("delete") || normalized.contains("elimina")) {
            return DELETE_RED;
        }
        if (isFlatTheme()) {
            if (normalized.contains("print") || normalized.contains("cancel")) {
                return neutralButtonColor();
            }
            return LINK_BLUE;
        }
        if (normalized.contains("add")
                || normalized.contains("insert")
                || normalized.contains("aggiungi")
                || normalized.contains("inserisci")) {
            return OTHER_AZURE;
        }
        if (normalized.contains("update")
                || normalized.contains("edit")
                || normalized.contains("price")
                || normalized.contains("aggiorna")
                || normalized.contains("prezzo")
                || normalized.contains("stima")) {
            return UPDATE_YELLOW;
        }
        if (normalized.contains("browse")
                || normalized.contains("print")
                || normalized.contains("cancel")
                || normalized.contains("sfoglia")
                || normalized.contains("stampa")
                || normalized.contains("annulla")) {
            return OTHER_AZURE;
        }
        return LINK_BLUE;
    }

    private static Color borderFor(Color color) {
        return new Color(
                Math.max(color.getRed() - 75, 0),
                Math.max(color.getGreen() - 75, 0),
                Math.max(color.getBlue() - 75, 0)
        );
    }

    private static Color brighten(Color color) {
        return brighten(color, 20);
    }

    private static Color brighten(Color color, int amount) {
        return new Color(
                Math.min(color.getRed() + amount, 255),
                Math.min(color.getGreen() + amount, 255),
                Math.min(color.getBlue() + amount, 255)
        );
    }

    private static Color darken(Color color, int amount) {
        return new Color(
                Math.max(color.getRed() - amount, 0),
                Math.max(color.getGreen() - amount, 0),
                Math.max(color.getBlue() - amount, 0)
        );
    }

    protected static boolean usesDarkText(Color color) {
        return (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000 > 170;
    }

    private void applyHeader(JTableHeader header) {
        header.setDefaultRenderer((table, value, selected, focus, row, col) -> new HeaderLabel(
                value != null ? value.toString().toUpperCase() : ""
        ));
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, isFlatTheme() ? 38 : 30));
    }

    private Component createAvatarCell(Object value, Color rowBackground) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 9));
        panel.setOpaque(true);
        panel.setBackground(rowBackground);
        panel.add(new AvatarLabel(value != null ? value.toString() : ""));
        return panel;
    }

    private Component createBadge(String value, boolean active, Color rowBackground) {
        JLabel label = new JLabel(value);
        label.setFont(FONT_SMALL_BOLD);
        label.setForeground(active ? OTHER_AZURE : TEXT_MUTED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(rowBackground);
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

        GlassPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
            setBorder(new RoundedGlassBorder(radius, HABBO_BORDER));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (isFlatTheme()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(
                        0,
                        0,
                        new Color(0xff, 0xff, 0xff, 235),
                        0,
                        getHeight(),
                        new Color(0xd8, 0xf5, 0xfb, 225)
                ));
                g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                g2.setPaint(new GradientPaint(
                        0,
                        2,
                        new Color(0xff, 0xff, 0xff, 170),
                        0,
                        Math.max(3, getHeight() / 2),
                        new Color(0xff, 0xff, 0xff, 35)
                ));
                g2.fillRoundRect(3, 3, getWidth() - 7, Math.max(8, getHeight() / 2), 16, 16);
            }
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
            return isFlatTheme() ? new Insets(0, 0, 0, 0) : new Insets(1, 1, 1, 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            int size = isFlatTheme() ? 0 : 1;
            insets.top = size;
            insets.left = size;
            insets.bottom = size;
            insets.right = size;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (isFlatTheme()) {
                g2.dispose();
                return;
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xff, 0xff, 0xff, 185));
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.setColor(color);
                int innerRadius = Math.max(0, radius - 2);
                g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, innerRadius, innerRadius);
            }
            g2.dispose();
        }
    }

    private static class HeaderLabel extends JLabel {
        HeaderLabel(String text) {
            super(text);
            setFont(FONT_SMALL_BOLD);
            setForeground(isFlatTheme() ? TEXT_MUTED : Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (isFlatTheme()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE_SOFT);
                g2.fillRoundRect(0, 4, getWidth(), getHeight() - 6, 10, 10);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x30b6f0), 0, getHeight(), HABBO_BLUE_DARK));
                g2.fillRoundRect(0, 2, getWidth(), getHeight() - 4, 10, 10);
                g2.setPaint(new GradientPaint(
                        0,
                        2,
                        new Color(0xff, 0xff, 0xff, 175),
                        0,
                        Math.max(3, getHeight() / 2),
                        new Color(0xff, 0xff, 0xff, 25)
                ));
                g2.fillRoundRect(2, 4, getWidth() - 4, Math.max(6, getHeight() / 2), 8, 8);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class AvatarLabel extends JLabel {
        AvatarLabel(String value) {
            super(initials(value));
            setFont(FONT_SMALL_BOLD);
            setForeground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.CENTER);
            setPreferredSize(new Dimension(26, 26));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            if (isFlatTheme()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(OTHER_AZURE);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x6eea83), 0, getHeight(), OTHER_AZURE));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setPaint(new GradientPaint(
                        0,
                        1,
                        new Color(0xff, 0xff, 0xff, 160),
                        0,
                        Math.max(3, getHeight() / 2),
                        new Color(0xff, 0xff, 0xff, 35)
                ));
                g2.fillRoundRect(2, 2, getWidth() - 5, Math.max(6, getHeight() / 2), 10, 10);
            }
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
            if (isFlatTheme()) {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(active ? new Color(0xf0ebff) : SURFACE_SOFT);
                g2.fillRoundRect(x, y + 2, width - 1, height - 5, 10, 10);
            } else {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = active ? new Color(0xcff5df) : new Color(0xe5f7fb);
                g2.setColor(fill);
                g2.fillRoundRect(x, y + 2, width - 1, height - 5, 12, 12);
                g2.setPaint(new GradientPaint(
                        0,
                        y + 2,
                        new Color(0xff, 0xff, 0xff, 150),
                        0,
                        y + Math.max(3, height / 2),
                        new Color(0xff, 0xff, 0xff, 25)
                ));
                g2.fillRoundRect(x + 2, y + 4, width - 5, Math.max(6, height / 2), 10, 10);
            }
            g2.dispose();
        }
    }
}
