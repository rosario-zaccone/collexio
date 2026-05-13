package org.collexio.presentation.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatLaf;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.ItemTableModel;
import org.collexio.presentation.model.TransactionTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public class AppFrame extends JFrame {
    private TabbedPanel tabbedPanel;
    private Point dragOffset;

    public AppFrame(
            String title,
            ItemCollectionTableModel itemCollectionModel,
            ItemSpecTableModel itemSpecModel,
            ItemTableModel itemModel,
            TransactionTableModel transactionModel
    ) {
        super(title);
        FlatLaf.setup(new FlatLightLaf());
        applyModernDefaults();
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MyPanel.paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);
        setContentPane(root);

        try {
            tabbedPanel = new TabbedPanel(
                    itemCollectionModel,
                    itemSpecModel,
                    itemModel,
                    transactionModel
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, PresentationText.text("Startup failed: ") + e.getMessage());
            System.exit(1);
        }

        root.add(createWindowHeader(title), BorderLayout.NORTH);
        root.add(tabbedPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(900, 560));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public TabbedPanel getTabbedPanel() {
        return tabbedPanel;
    }

    private JPanel createWindowHeader(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(createWindowTitleBar(title), BorderLayout.NORTH);
        header.add(createMenuBar(), BorderLayout.SOUTH);
        return header;
    }

    private JPanel createWindowTitleBar(String title) {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (MyPanel.isFlatTheme()) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(MyPanel.HABBO_BLUE);
                    g2.fillRoundRect(8, 6, getWidth() - 16, getHeight() + 6, 12, 12);
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(
                            0,
                            0,
                            new Color(0x2aaee8),
                            0,
                            getHeight() + 4,
                            MyPanel.HABBO_BLUE_DARK
                    ));
                    g2.fillRoundRect(6, 4, getWidth() - 12, getHeight() + 6, 12, 12);
                    g2.setColor(new Color(0xff, 0xff, 0xff, 120));
                    g2.drawLine(14, 6, getWidth() - 16, 6);
                }
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, MyPanel.isFlatTheme() ? 38 : 32));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(MyPanel.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, MyPanel.isFlatTheme() ? 14 : 8, 0, 0));

        JPanel controls = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                MyPanel.isFlatTheme() ? 8 : 4,
                MyPanel.isFlatTheme() ? 9 : 6
        ));
        controls.setOpaque(false);
        controls.add(createWindowButton("minimize", MyPanel.LINK_BLUE, () -> setState(Frame.ICONIFIED)));
        controls.add(createWindowButton("maximize", MyPanel.LINK_BLUE, this::toggleMaximize));
        controls.add(createWindowButton("close", MyPanel.DELETE_RED,
                () -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));

        bar.add(titleLabel, BorderLayout.CENTER);
        bar.add(controls, BorderLayout.EAST);

        MouseAdapter drag = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragOffset != null && getExtendedState() != Frame.MAXIMIZED_BOTH) {
                    Point screen = e.getLocationOnScreen();
                    setLocation(screen.x - dragOffset.x, screen.y - dragOffset.y);
                }
            }
        };
        bar.addMouseListener(drag);
        bar.addMouseMotionListener(drag);
        return bar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(MyPanel.SURFACE);
        menuBar.setBorder(MyPanel.isFlatTheme()
                ? BorderFactory.createEmptyBorder()
                : BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xff, 0xff, 0xff, 120)));

        JMenu viewMenu = new JMenu(PresentationText.text("View"));
        styleMenu(viewMenu);

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem year2000 = new JRadioButtonMenuItem("2000");
        JRadioButtonMenuItem flat = new JRadioButtonMenuItem("Flat");
        styleMenuItem(year2000);
        styleMenuItem(flat);
        year2000.setSelected(MyPanel.getThemeStyle() == MyPanel.ThemeStyle.YEAR_2000);
        flat.setSelected(MyPanel.getThemeStyle() == MyPanel.ThemeStyle.FLAT);

        year2000.addActionListener(e -> switchTheme(MyPanel.ThemeStyle.YEAR_2000));
        flat.addActionListener(e -> switchTheme(MyPanel.ThemeStyle.FLAT));
        group.add(year2000);
        group.add(flat);
        viewMenu.add(year2000);
        viewMenu.add(flat);
        menuBar.add(viewMenu);
        menuBar.add(createLanguageMenu());
        return menuBar;
    }

    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu(PresentationText.text("Language"));
        styleMenu(languageMenu);

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem english = new JRadioButtonMenuItem(PresentationText.text("English"));
        JRadioButtonMenuItem italian = new JRadioButtonMenuItem(PresentationText.text("Italian"));
        styleMenuItem(english);
        styleMenuItem(italian);
        english.setSelected(PresentationText.getLanguage() == PresentationText.Language.ENGLISH);
        italian.setSelected(PresentationText.getLanguage() == PresentationText.Language.ITALIAN);

        english.addActionListener(e -> switchLanguage(PresentationText.Language.ENGLISH));
        italian.addActionListener(e -> switchLanguage(PresentationText.Language.ITALIAN));
        group.add(english);
        group.add(italian);
        languageMenu.add(english);
        languageMenu.add(italian);
        return languageMenu;
    }

    private void switchLanguage(PresentationText.Language language) {
        PresentationText.setLanguage(language);
        getContentPane().removeAll();
        JPanel root = createRootPanel();
        setContentPane(root);
        root.add(createWindowHeader(getTitle()), BorderLayout.NORTH);
        root.add(tabbedPanel, BorderLayout.CENTER);
        tabbedPanel.updateLanguage();
        refreshThemedComponents(root);
        SwingUtilities.updateComponentTreeUI(this);
        revalidate();
        repaint();
    }

    private void switchTheme(MyPanel.ThemeStyle style) {
        MyPanel.setThemeStyle(style);
        applyModernDefaults();
        getContentPane().removeAll();
        JPanel root = createRootPanel();
        setContentPane(root);
        root.add(createWindowHeader(getTitle()), BorderLayout.NORTH);
        root.add(tabbedPanel, BorderLayout.CENTER);
        refreshThemedComponents(root);
        SwingUtilities.updateComponentTreeUI(this);
        revalidate();
        repaint();
    }

    private JPanel createRootPanel() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MyPanel.paintAeroBackground((Graphics2D) g, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);
        return root;
    }

    private void refreshThemedComponents(Component component) {
        if (component instanceof JTable table) {
            table.setBackground(MyPanel.isFlatTheme() ? MyPanel.SURFACE : Color.WHITE);
            table.setForeground(MyPanel.TEXT);
            table.setSelectionBackground(MyPanel.ROW_SELECTED);
            table.setSelectionForeground(MyPanel.TEXT);
            table.setGridColor(MyPanel.tableGridColor());
            table.setShowGrid(!MyPanel.isFlatTheme());
            table.setIntercellSpacing(MyPanel.isFlatTheme() ? new Dimension(0, 0) : new Dimension(1, 1));
            table.getTableHeader().setPreferredSize(new Dimension(0, MyPanel.isFlatTheme() ? 38 : 30));
        } else if (component instanceof JComboBox<?>) {
            component.setBackground(MyPanel.FIELD);
            component.setForeground(MyPanel.TEXT);
        } else if (component instanceof JScrollPane scrollPane) {
            scrollPane.getViewport().setBackground(MyPanel.isFlatTheme() ? MyPanel.SURFACE : Color.WHITE);
            scrollPane.setBackground(MyPanel.isFlatTheme() ? MyPanel.SURFACE : new Color(0xd9f4ed));
            scrollPane.setBorder(MyPanel.isFlatTheme()
                    ? BorderFactory.createEmptyBorder()
                    : MyPanel.createGlassBorder(18));
        } else if (component instanceof JMenuBar || component instanceof JMenu || component instanceof JMenuItem) {
            component.setBackground(MyPanel.SURFACE);
            component.setForeground(MyPanel.TEXT);
        } else if (component instanceof AbstractButton button) {
            Object key = button.getClientProperty("text.key");
            if (key instanceof String textKey) {
                button.setText(PresentationText.text(textKey));
                button.putClientProperty("button.color", MyPanel.actionColorFor(textKey));
            }
            button.setFont(MyPanel.FONT_BOLD);
        } else if (component instanceof JLabel label) {
            Object key = label.getClientProperty("text.key");
            if (key instanceof String textKey) {
                label.setText(PresentationText.text(textKey));
            }
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                refreshThemedComponents(child);
            }
        }
    }

    private void styleMenu(JMenu menu) {
        menu.setFont(MyPanel.FONT_BOLD);
        menu.setForeground(MyPanel.TEXT);
        menu.setBackground(MyPanel.SURFACE);
        menu.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
    }

    private void styleMenuItem(JMenuItem item) {
        item.setFont(MyPanel.FONT);
        item.setForeground(MyPanel.TEXT);
        item.setBackground(MyPanel.SURFACE);
        item.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 18));
    }

    private JComponent createWindowButton(String type, Color color, Runnable action) {
        JPanel button = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (MyPanel.isFlatTheme()) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(color);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 9, 9);
                } else {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(
                            0,
                            0,
                            new Color(0xffffff),
                            0,
                            getHeight(),
                            color
                    ));
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    g2.setPaint(new GradientPaint(
                            0,
                            1,
                            new Color(0xff, 0xff, 0xff, 175),
                            0,
                            Math.max(3, getHeight() / 2),
                            new Color(0xff, 0xff, 0xff, 30)
                    ));
                    g2.fillRoundRect(2, 2, getWidth() - 5, Math.max(5, getHeight() / 2), 7, 7);
                    g2.setColor(new Color(0xff, 0xff, 0xff, 150));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                switch (type) {
                    case "minimize" -> g2.drawLine(cx - 5, cy + 4, cx + 5, cy + 4);
                    case "maximize" -> g2.drawRect(cx - 5, cy - 5, 10, 9);
                    case "close" -> {
                        g2.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
                        g2.drawLine(cx + 5, cy - 5, cx - 5, cy + 5);
                    }
                    default -> {
                    }
                }
                g2.dispose();
            }
        };
        button.setOpaque(false);
        button.setPreferredSize(MyPanel.isFlatTheme() ? new Dimension(22, 20) : new Dimension(24, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return button;
    }

    private void toggleMaximize() {
        setExtendedState(getExtendedState() == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
    }

    private void applyModernDefaults() {
        Font font = MyPanel.FONT;
        Font bold = MyPanel.FONT_BOLD;
        UIManager.put("Panel.background", MyPanel.SURFACE);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", bold);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", MyPanel.FONT_SMALL_BOLD);
        UIManager.put("TextField.font", font);
        UIManager.put("TextField.background", MyPanel.FIELD);
        UIManager.put("TextField.foreground", MyPanel.TEXT);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextArea.background", MyPanel.FIELD);
        UIManager.put("TextArea.foreground", MyPanel.TEXT);
        UIManager.put("ComboBox.font", font);
        UIManager.put("ComboBox.background", MyPanel.FIELD);
        UIManager.put("ComboBox.foreground", MyPanel.TEXT);
        UIManager.put("MenuBar.background", MyPanel.SURFACE);
        UIManager.put("Menu.background", MyPanel.SURFACE);
        UIManager.put("Menu.foreground", MyPanel.TEXT);
        UIManager.put("MenuItem.background", MyPanel.SURFACE);
        UIManager.put("MenuItem.foreground", MyPanel.TEXT);
        UIManager.put("Menu.selectionBackground", MyPanel.isFlatTheme() ? new Color(0xeee9ff) : new Color(0x2d9fe0));
        UIManager.put("Menu.selectionForeground", MyPanel.isFlatTheme() ? MyPanel.TEXT : Color.WHITE);
        UIManager.put(
                "MenuItem.selectionBackground",
                MyPanel.isFlatTheme() ? new Color(0xeee9ff) : new Color(0x2d9fe0)
        );
        UIManager.put("MenuItem.selectionForeground", MyPanel.isFlatTheme() ? MyPanel.TEXT : Color.WHITE);
        UIManager.put("OptionPane.background", MyPanel.SURFACE);
        UIManager.put("OptionPane.messageForeground", MyPanel.TEXT);
        UIManager.put("Component.arc", MyPanel.isFlatTheme() ? 8 : 2);
        UIManager.put("Button.arc", MyPanel.isFlatTheme() ? 8 : 2);
        UIManager.put("TextComponent.arc", MyPanel.isFlatTheme() ? 6 : 0);
    }
}
