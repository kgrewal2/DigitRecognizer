package ramo.klevis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author sylsau
 */
public class DrawArea extends JPanel {

    private Image image;
    private Graphics2D graphics2D;
    private int currentX, currentY, oldX, oldY; // Mouse Coordinates
    private JPanel bottomPanel;

    public DrawArea() {
        setLayout(new BorderLayout());
        setDoubleBuffered(false);
        UIUtilities.addBorderWithTitle(this,"Drawing Area");
        addListeners();
        addBottomPanel();
    }

    private void addBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void add(JButton button){
        bottomPanel.add(button);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawImage(image, 0, 0, null);
        repaint();
        revalidate();
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.setPaint(Color.black);
        repaint();
        revalidate();
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });
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
                    graphics2D.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    graphics2D.drawLine(oldX, oldY, currentX, currentY);
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }
            }
        });
    }

    public Image getImage() {
        return image;
    }

    public void reset() {
        this.image = null;
        repaint();
    }
}