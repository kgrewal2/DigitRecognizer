package ramo.klevis;

import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;

import java.io.Serializable;

/**
 * Created by klevis.ramo on 11/27/2017.
 */
public class LabeledImage implements Serializable {
    private final double[] pixels;
    private final double[] meanNormalizedFeatures;
    private double label;
    private Vector features;

    public LabeledImage(int label, double[] pixels) {
        this.pixels = pixels;
        this.label = label;
        meanNormalizedFeatures = getMeanNormalizedFeatures(pixels);
        features = Vectors.dense(meanNormalizedFeatures);
    }

    private double[] getMeanNormalizedFeatures(double[] pixels) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double pixelSum = 0;
        for (double pixel : pixels) {
            pixelSum = pixelSum + pixel;
            if (pixel > max) {
                max = pixel;
            }
            if (pixel < min) {
                min = pixel;
            }
        }
        double meanPixelValue = pixelSum / pixels.length;
        double[] normalizedFeatures = new double[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            normalizedFeatures[i] = (pixels[i] - meanPixelValue) / (max - min);
        }
        return normalizedFeatures;
    }

    @Override
    public String toString() {
        return "LabeledImage{label=" + label + "}";
    }

    public Vector getFeatures() {
        return features;
    }

    public double[] getPixels() {
        return pixels;
    }
}
