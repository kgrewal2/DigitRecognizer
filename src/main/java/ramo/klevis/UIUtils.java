package ramo.klevis;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Contains methods and variables to change UI look and feel.
 */

public class UIUtils {
    static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    static final Border RAISED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), EMPTY_BORDER);
    static final Border LOWERED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), EMPTY_BORDER);
    static final Font SANS_SERIF_BOLD16 = new Font("SansSerif", Font.ITALIC, 16);
    static final Font MONO14 = new Font("Monospaced", Font.PLAIN, 14);

    public static JButton getFancyButton(String label, Color background) {
        JButton button = new JButton(label);
        button.setBackground(background);
        button.setForeground(Color.BLACK);
        button.setBorder(RAISED_BORDER);
        addFancyButtonListeners(button);
        return button;
    }

    private static void addFancyButtonListeners(JButton button) {
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBorder(LOWERED_BORDER);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBorder(RAISED_BORDER);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }
        });
    }

    public static Border getTitledBorder(String title){
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.LEFT, TitledBorder.TOP, SANS_SERIF_BOLD16);
    }

    public static void setUIManagerSettings() {
        // TURN ON ANTIALIASING
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
    }
}
