package ramo.klevis;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UIUtilities {
    static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(5, 10, 5, 10);
    static final Border RAISED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), EMPTY_BORDER);
    static final Border LOWERED_BORDER = BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), EMPTY_BORDER);

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

    public static void addBorderWithTitle(JPanel panel, String title) {
        Font sansSerifBold = new Font("SansSerif", Font.ITALIC, 16);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.LEFT, TitledBorder.TOP, sansSerifBold));
    }
}
