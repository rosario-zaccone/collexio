package org.collexio.presentation;

import org.collexio.business.service.ItemSpecService;
import org.collexio.business.service.InfoGenerationService;
import org.collexio.business.service.PriceService;
import org.collexio.ConnectionFactory;
import org.collexio.persistence.dao.DBItemSpecDAO;
import org.collexio.presentation.controller.ItemSpecController;
import org.collexio.presentation.model.ItemSpecTableModel;
import org.collexio.presentation.view.itemspec.ItemSpecPanel;
import org.collexio.utilities.factory.AbstractFactory;
import org.collexio.utilities.infogenerator.InfoGenerator;
import org.collexio.utilities.pricescraper.PriceScraper;

import javax.swing.*;
import java.sql.SQLException;

public class ItemSpecPanelTest {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame frame = new JFrame("TableDemo");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    AbstractFactory factory = new AbstractFactory("test") {
                        @Override
                        public InfoGenerator createInfoGenerator() {
                            return itemName -> "";
                        }

                        @Override
                        public PriceScraper createPriceScraper() {
                            return itemName -> 0;
                        }
                    };
                    PriceService priceService = new PriceService(factory.createPriceScraper(), factory, factory, factory);
                    InfoGenerationService infoService = new InfoGenerationService(factory.createInfoGenerator(), factory, factory, factory);
                    ItemSpecTableModel model = new ItemSpecTableModel(
                            new ItemSpecService(new DBItemSpecDAO(ConnectionFactory.getConnection())),
                            priceService,
                            infoService
                    );
                    ItemSpecPanel view = new ItemSpecPanel(model);
                    view.setOpaque(true);
                    frame.setContentPane(view);

                    frame.pack();
                    frame.setVisible(true);

                    ItemSpecController controller = new ItemSpecController(model, view);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
