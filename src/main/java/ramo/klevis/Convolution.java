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
     * Takes an image (grey-levels) and a kernel and a position, applies the
     * convolution at that position
     *
     * @return the new pixel value.
     */
    public static double singlePixelConvolution(double[][] image, int x, int y, double[][] kernel, int kernelWidth,
                                                int kernelHeight) {
        double newPixelValue = 0;
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
                newPixelValue = newPixelValue + (image[x + i][y + j] * kernel[i][j]);
            }
        }
        return newPixelValue;
    }

    /**
     * Takes a 2D array of grey-levels and a kernel and applies the convolution over
     * the area of the image specified by width and height.
     *
     * @return the 2D array representing the new image
     */
    public static double[][] areaConvolution(double[][] image, int width, int height, double[][] kernel, int kernelWidth,
                                             int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        double[][] newImage = new double[smallWidth][smallHeight];
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                newImage[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                newImage[i][j] = singlePixelConvolution(image, i, j, kernel, kernelWidth, kernelHeight);
            }
        }
        return newImage;
    }

    /**
     * Takes a 2D array of grey-levels and a kernel, applies the convolution over
     * the area of the image specified by width and height
     *
     * @return Part of the final image.
     */
    public static double[][] areaConvolutionWithPadding(double[][] image, int width, int height, double[][] kernel,
                                                        int kernelWidth, int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        int top = kernelHeight / 2;
        int left = kernelWidth / 2;
        double[][] small = areaConvolution(image, width, height, kernel, kernelWidth, kernelHeight);
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
     * Applies the convolution2DPadded algorithm to the input array as many as
     * iterations.
     *
     * @return the 2D array representing the new image
     */
    public double[][] convolutionType2(double[][] image, int width, int height, double[][] kernel, int kernelWidth,
                                       int kernelHeight, int iterations) {
        double[][] newInput = image.clone();
        double[][] output = image.clone();

        for (int i = 0; i < iterations; ++i) {
            output = areaConvolutionWithPadding(newInput, width, height, kernel, kernelWidth, kernelHeight);
            newInput = output.clone();
        }
        return output;
    }
}
