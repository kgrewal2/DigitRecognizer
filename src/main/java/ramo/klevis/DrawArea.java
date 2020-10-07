package ramo.klevis;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author sylsau
 */
public class DrawArea extends JComponent {

    private Image image;
    private Graphics2D graphics2D;
    private int currentX, currentY, oldX, oldY; // Mouse Coordinates

    public DrawArea() {
        setDoubleBuffered(false);
        setBorderWithLabel();
        addListeners();
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    private void addListeners(){
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                if (graphics2D != null) {
                    graphics2D.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    graphics2D.drawLine(oldX, oldY, currentX, currentY);
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    private void setBorderWithLabel() {
        Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Please draw a digit",
                TitledBorder.LEFT, TitledBorder.TOP, sansSerifBold, Color.BLUE));
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}