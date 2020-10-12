package ramo.klevis;

import javax.swing.*;
import java.awt.*;

/**
 * @author Klevis Ramo
 */
public class ProgressBar {

    private final JFrame mainFrame;
    private final JProgressBar progressBar;
    private boolean isUndecorated = false;

    public ProgressBar(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        progressBar = getProgressBar();
    }

    public ProgressBar(JFrame mainFrame, boolean isUndecorated) {
        this.mainFrame = mainFrame;
        this.isUndecorated = isUndecorated;
        progressBar = getProgressBar();
    }

    public void showProgressBar(String message) {
        SwingUtilities.invokeLater(() -> {
            if (isUndecorated) {
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setUndecorated(true);
            }
            mainFrame.add(progressBar, BorderLayout.NORTH);
            progressBar.setString(message);
            progressBar.setStringPainted(true);
            progressBar.setIndeterminate(true);
            progressBar.setVisible(true);
            mainFrame.add(progressBar, BorderLayout.NORTH);
            if (isUndecorated) {
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
            mainFrame.repaint();
        });
    }

    private JProgressBar getProgressBar() {
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setVisible(false);
        return progressBar;
    }

    public void setVisible(boolean visible) {
        progressBar.setVisible(visible);
    }
}
