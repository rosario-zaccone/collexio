package org.collexio.presentation.view;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public final class BusyDialog {
    private BusyDialog() {}

    public static <T> void run(
            Component parent,
            String title,
            String message,
            Callable<T> task,
            Consumer<T> onSuccess,
            Consumer<Exception> onError
    ) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, PresentationText.text(title), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel label = new JLabel(PresentationText.text(message));
        label.setFont(MyPanel.FONT_BOLD);
        label.setForeground(MyPanel.TEXT);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(320, dialog.getHeight()));
        dialog.setLocationRelativeTo(parent);

        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                dialog.dispose();
                try {
                    onSuccess.accept(get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    onError.accept(ex);
                } catch (ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    onError.accept(cause instanceof Exception exception ? exception : new RuntimeException(cause));
                }
            }
        };

        worker.execute();
        dialog.setVisible(true);
    }
}
