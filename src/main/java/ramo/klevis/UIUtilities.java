package ramo.klevis;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class UIUtilities {
    public static void addBorderWithTitle(JPanel panel, String title) {
        Font sansSerifBold = new Font("SansSerif", Font.ITALIC, 16);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title,
                TitledBorder.LEFT, TitledBorder.TOP, sansSerifBold));
    }
}
