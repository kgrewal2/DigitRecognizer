package ramo.klevis;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.*;

import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;

public class LabeledImageFactory {
    public static LabeledImage fromImage(Image im) {
        Function<Image, Image> passThrough = i -> i;
        Function<Image, LabeledImage> factory = passThrough.andThen(LabeledImageFactory::toBufferedImage)
                .andThen(LabeledImageFactory::scaleBufferedImage).andThen(LabeledImageFactory::toBufferedImage)
                .andThen(LabeledImageFactory::toOneDimensionalVector)
                .andThen(scaledPixels -> new LabeledImage(0, scaledPixels));
        return factory.apply(im);
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
