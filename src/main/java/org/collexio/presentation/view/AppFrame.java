package org.collexio.presentation.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.collexio.business.service.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import javax.swing.*;

public class AppFrame extends JFrame {

    public AppFrame(
            String title,
            Connection connection,
            ItemCollectionService itemCollectionService,
            ItemCollectionOrchestrator collectionOrchestrator,
            ItemService itemService,
            ItemOrchestrator itemOrchestrator,
            ItemPhotoService photoService,
            ItemSpecService specService,
            TransactionService transactionService
    ) {
        super(title);
        FlatLaf.setup(new FlatLightLaf());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        TabbedPanel tabbedPanel = null;
        try {
            tabbedPanel = new TabbedPanel(
                    connection,
                    itemCollectionService,
                    collectionOrchestrator,
                    itemService,
                    itemOrchestrator,
                    photoService,
                    specService,
                    transactionService
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
        fileMenu.addSeparator();

        JMenu viewMenu = new JMenu("View");
        JMenu lookMenu = new JMenu("Appearance");
        JMenuItem light = new JMenuItem("Light");
        JMenuItem dark = new JMenuItem("Dark");
        lookMenu.add(light);
        lookMenu.add(dark);
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

        pack();
        setVisible(true);
    }
}