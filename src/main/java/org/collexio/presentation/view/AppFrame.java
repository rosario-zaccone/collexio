package org.collexio.presentation.view;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.collexio.business.service.*;
import org.collexio.presentation.model.ItemCollectionTableModel;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.model.ItemTableModel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import javax.swing.*;

public class AppFrame extends JFrame {
    private TabbedPanel tabbedPanel;

    public AppFrame(
            String title,
            ItemCollectionTableModel itemCollectionModel,
            ItemSpecTableModel itemSpecModel,
            ItemTableModel itemModel
    ) {
        super(title);
        FlatLaf.setup(new FlatLightLaf());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        try {
            tabbedPanel = new TabbedPanel(
                    itemCollectionModel,
                    itemSpecModel,
                    itemModel
            );
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            System.exit(1);
        }
        add(tabbedPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_A);
        JMenuItem dump = new JMenuItem("Dump database");
        fileMenu.add(dump);


        JMenu viewMenu = new JMenu("View");
        JMenu lookMenu = new JMenu("Appearance");
        JMenuItem light = new JMenuItem("Light");
        JMenuItem dark = new JMenuItem("Dark");
        JMenuItem intellij = new JMenuItem("IntelliJ");
        JMenuItem darcula = new JMenuItem("Darcula");
        JMenuItem macLight = new JMenuItem("Mac Light");
        JMenuItem macDark = new JMenuItem("Mac Dark");

        lookMenu.add(light);
        lookMenu.add(dark);
        lookMenu.addSeparator();

        lookMenu.add(intellij);
        lookMenu.add(darcula);
        lookMenu.addSeparator();

        lookMenu.add(macLight);
        lookMenu.add(macDark);

        viewMenu.add(lookMenu);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        dump.addActionListener(e -> {
            JDialog test = new JDialog(this, "Dump", true);
            test.setSize(300, 200);
            test.setLocationRelativeTo(this);
            test.setVisible(true);
        });

        light.addActionListener(e -> {
            FlatLaf.setup(new FlatLightLaf());
            FlatLaf.updateUI();
        });

        dark.addActionListener(e -> {
            FlatLaf.setup(new FlatDarkLaf());
            FlatLaf.updateUI();
        });

        intellij.addActionListener(e -> {
            FlatLaf.setup(new FlatIntelliJLaf());
            FlatLaf.updateUI();
        });

        darcula.addActionListener(e -> {
            FlatLaf.setup(new FlatDarculaLaf());
            FlatLaf.updateUI();
        });

        macLight.addActionListener(e -> {
            FlatLaf.setup(new FlatMacLightLaf());
            FlatLaf.updateUI();
        });

        macDark.addActionListener(e -> {
            FlatLaf.setup(new FlatMacDarkLaf());
            FlatLaf.updateUI();
        });


        pack();
        setVisible(true);
    }

    public TabbedPanel getTabbedPanel() {
        return tabbedPanel;
    }
}