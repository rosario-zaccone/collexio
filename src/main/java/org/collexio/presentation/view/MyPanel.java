package org.collexio.presentation.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;


public abstract class MyPanel extends JPanel {

    protected MyPanel() {
        setBackground(BG);
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    protected static final Color BG             = new Color(245, 248, 252);
    protected static final Color PANEL          = new Color(255, 255, 255, 220);
    protected static final Color ACCENT         = new Color(0, 122, 255);
    protected static final Color ACCENT_SOFT    = new Color(0, 122, 255, 40);
    protected static final Color TEXT           = new Color(28, 28, 30);
    protected static final Color TEXT_SECONDARY = new Color(142, 142, 147);
    protected static final Color LINE           = new Color(220, 220, 224);
    protected static final Color ROW_ALT        = new Color(248, 250, 252);
    protected static final Color ROW_HOVER      = new Color(0, 122, 255, 18);
    protected static final Color ROW_SELECTED   = new Color(0, 122, 255, 40);
    protected static final Font  FONT           = new Font("SansSerif", Font.PLAIN, 13);
    protected static final Font  FONT_BOLD      = new Font("SansSerif", Font.BOLD, 13);
    protected static final Font  FONT_TITLE     = new Font("SansSerif", Font.BOLD, 15);

    public enum ButtonStyle { PRIMARY, DESTRUCTIVE, NEUTRAL }

    private boolean gradientDirty = true;
    private BufferedImage gradientCache;

    @Override
    protected void paintComponent(Graphics g) {
        if (gradientDirty || gradientCache == null ||
                gradientCache.getWidth()  != Math.max(1, getWidth()) ||
                gradientCache.getHeight() != Math.max(1, getHeight())) {

            gradientCache = new BufferedImage(
                    Math.max(1, getWidth()),
                    Math.max(1, getHeight()),
                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2 = gradientCache.createGraphics();
            g2.setPaint(new GradientPaint(
                    0, 0,           new Color(255, 255, 255),
                    0, getHeight(), new Color(235, 240, 248)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            gradientDirty = false;
        }
        g.drawImage(gradientCache, 0, 0, null);
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        if (w != getWidth() || h != getHeight()) gradientDirty = true;
        super.setBounds(x, y, w, h);
    }

    protected JPanel createTitleBar(String title) {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(248, 250, 253));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(LINE);
                g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };

        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 34));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        bar.add(lbl, BorderLayout.CENTER);
        return bar;
    }

    protected JTable createTable(TableModel model) {
        JTable table = new JTable(model) {

            private int hover = -1;

            {
                addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                    public void mouseMoved(java.awt.event.MouseEvent e) {
                        int r = rowAtPoint(e.getPoint());
                        if (r != hover) { hover = r; repaint(); }
                    }
                });
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hover = -1; repaint();
                    }
                });
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);

                if (isRowSelected(row)) {
                    c.setBackground(ROW_SELECTED);
                } else if (row == hover) {
                    c.setBackground(ROW_HOVER);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                }

                c.setForeground(TEXT);
                c.setFont(FONT);
                ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

                return c;
            }
        };

        table.setRowHeight(60);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFont(FONT);
        table.setSelectionBackground(ROW_SELECTED);
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setReorderingAllowed(false);

        applyHeader(table.getTableHeader());
        return table;
    }

    private void applyHeader(JTableHeader header) {
        header.setDefaultRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = new JLabel(value != null ? value.toString() : "");
            lbl.setFont(FONT_BOLD);
            lbl.setForeground(TEXT_SECONDARY);
            lbl.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            lbl.setOpaque(false);
            return lbl;
        });

        header.setPreferredSize(new Dimension(0, 30));
        header.setOpaque(false);
    }

    protected JScrollPane createScrollPane(JTable table, int width, int height) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(LINE));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setPreferredSize(new Dimension(width, height));
        return sp;
    }

    protected JButton createButton(String text) {
        JButton btn = new JButton(text) {
            private float hoverAmt = 0f;
            private Timer hoverTimer;

            {
                setOpaque(false);
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { animateTo(1f); }
                    public void mouseExited(java.awt.event.MouseEvent e)  { animateTo(0f); }
                });
            }

            private void animateTo(float target) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(16, null);
                hoverTimer.addActionListener(e -> {
                    hoverAmt += (target - hoverAmt) * 0.25f;
                    if (Math.abs(hoverAmt - target) < 0.01f) {
                        hoverAmt = target;
                        hoverTimer.stop();
                    }
                    repaint();
                });
                hoverTimer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean pressed = getModel().isPressed();
                int w = getWidth(), h = getHeight();

                if (!pressed) {
                    g2.setColor(new Color(0, 100, 220, 40));
                    g2.fillRoundRect(2, 4, w - 4, h - 4, 12, 12);
                }

                int red  = (int)(hoverAmt * 20);
                int gb   = (int)(122 - hoverAmt * 20);
                Color base = pressed ? new Color(0, 80, 200) : new Color(red, gb, 255);
                g2.setColor(base);
                g2.fillRoundRect(0, pressed ? 2 : 0, w, h - (pressed ? 2 : 0), 12, 12);

                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(2, pressed ? 3 : 1, w - 4, h / 2, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    protected JPanel createFilterBar(JComboBox<String> combo, String labelText) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LINE));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_BOLD);
        lbl.setForeground(TEXT_SECONDARY);

        combo.setFont(FONT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(LINE));

        bar.add(lbl);
        bar.add(combo);
        return bar;
    }
}