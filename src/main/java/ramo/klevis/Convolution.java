package ramo.klevis;

/**
 * For applying the convolution operator.
 *
 * @author: Simon Horne
 */
public class Convolution extends Thread {

    public Convolution() {
    }

    /**
     * Applies the ConvolutionWithPadding algorithm to the input array as many as
     * iterations.
     *
     * @return the 2D array representing the new image
     */
    public double[][] applyConvolutionWithPaddingIterative(double[][] image, int width, int height, double[][] kernel, int kernelWidth,
                                                           int kernelHeight, int iterations) {
        double[][] inputImageClone = image.clone();
        double[][] outputImage = image.clone();

        for (int i = 0; i < iterations; ++i) {
            outputImage = applyConvolutionWithPadding(inputImageClone, width, height, kernel, kernelWidth, kernelHeight);
            inputImageClone = outputImage.clone();
        }
        return outputImage;
    }

    /**
     * Takes a 2D array of grey-levels and a kernel, applies the convolution over
     * the area of the image specified by width and height
     *
     * @return Part of the final image.
     */
    private static double[][] applyConvolutionWithPadding(double[][] image, int width, int height, double[][] kernel,
                                                          int kernelWidth, int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        int top = kernelHeight / 2;
        int left = kernelWidth / 2;
        double[][] small = applyConvolution(image, width, height, kernel, kernelWidth, kernelHeight);
        double[][] large = new double[width][height];
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                large[i][j] = 0;
            }
        }
        for (int j = 0; j < smallHeight; ++j) {
            for (int i = 0; i < smallWidth; ++i) {
                large[i + left][j + top] = small[i][j];
            }
        }
        return large;
    }

    /**
     * Takes a 2D array of grey-levels and a kernel and applies the convolution over
     * the area of the image specified by width and height.
     *
     * @return the 2D array representing the new image
     */
    private static double[][] applyConvolution(double[][] image, int width, int height, double[][] kernel, int kernelWidth,
                                               int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        double[][] outputImage = new double[smallWidth][smallHeight];
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                outputImage[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                outputImage[i][j] = applySinglePixelConvolution(image, i, j, kernel, kernelWidth, kernelHeight);
            }
        }
        return outputImage;
    }

    /**
     * Takes an image (grey-levels) and a kernel and a position, applies the
     * convolution at that position
     *
     * @return the new pixel value.
     */
    private static double applySinglePixelConvolution(double[][] image, int x, int y, double[][] kernel, int kernelWidth,
                                                     int kernelHeight) {
        double newPixelValue = 0;
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
                newPixelValue = newPixelValue + (image[x + i][y + j] * kernel[i][j]);
            }
        }
        return newPixelValue;
    }


}
