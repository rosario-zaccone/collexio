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
        applyAeroDefaults();
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
            JOptionPane.showMessageDialog(this, "Startup failed: " + e.getMessage());
            System.exit(1);
        }

        root.add(createWindowTitleBar(title), BorderLayout.NORTH);
        root.add(tabbedPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(900, 560));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public TabbedPanel getTabbedPanel() {
        return tabbedPanel;
    }

    private JPanel createWindowTitleBar(String title) {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x64bedd), getWidth(), 0, new Color(0x3ca0d2)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 70));
                g2.drawLine(0, 1, getWidth(), 1);
                g2.dispose();
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 34));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 9));
        controls.setOpaque(false);
        controls.add(createCircleButton(new Color(0xffcc44), () -> setState(Frame.ICONIFIED)));
        controls.add(createCircleButton(new Color(0x44cc66), this::toggleMaximize));
        controls.add(createCircleButton(new Color(0xff4433),
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

    private JComponent createCircleButton(Color color, Runnable action) {
        JPanel button = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(new Color(255, 255, 255, 150));
                g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
                g2.dispose();
            }
        };
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(13, 13));
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

    private void applyAeroDefaults() {
        Font font = new Font("Segoe UI", Font.PLAIN, 12);
        Font bold = new Font("Segoe UI", Font.BOLD, 12);
        UIManager.put("Panel.background", new Color(0, 0, 0, 0));
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", bold);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 9));
        UIManager.put("TextField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("ComboBox.font", font);
    }
}
