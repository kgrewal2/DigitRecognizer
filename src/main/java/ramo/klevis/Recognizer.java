package ramo.klevis;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Recognizer {
    public static Recognizer getRecognizer() {
        return new Recognizer();
    }

    public void recognize(DrawArea drawArea, Predictor neuralNetwork, JPanel resultPanel) {
        Image drawImage = drawArea.getImage();
        BufferedImage sbi = toBufferedImage(drawImage);
        Image scaled = scaleBufferedImage(sbi);
        BufferedImage scaledBuffered = toBufferedImage(scaled);
        double[] scaledPixels = toOneDimensionalVector(scaledBuffered);
        LabeledImage labeledImage = new LabeledImage(0, scaledPixels);
        JLabel predictNumber = null;

        int predict = neuralNetwork.predict(labeledImage);
        predictNumber = new JLabel("" + predict);

        predictNumber.setForeground(Color.RED);
        predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
        resultPanel.removeAll();
        resultPanel.add(predictNumber);
        resultPanel.updateUI();
    }

    private static BufferedImage toBufferedImage(Image img) {
        BufferedImage bufferedImageWithTransparency = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImageWithTransparency.createGraphics();
        graphics2D.drawImage(img, 0, 0, null);
        graphics2D.dispose();
        return bufferedImageWithTransparency;
    }

    private static double[] toOneDimensionalVector(BufferedImage image) {
        double[] imageVector = new double[28 * 28];
        int index = 0;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = new Color(image.getRGB(j, i), true);
                int red = (color.getRed());
                int green = (color.getGreen());
                int blue = (color.getBlue());
                double colorValue = 255 - (red + green + blue) / 3d;
                imageVector[index] = colorValue;
                index++;
            }
        }
        return imageVector;
    }

    private static BufferedImage scaleBufferedImage(BufferedImage imageToScale) {
        ResampleOp resizeOp = new ResampleOp(28, 28);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        BufferedImage filter = resizeOp.filter(imageToScale, null);
        return filter;
    }

}
