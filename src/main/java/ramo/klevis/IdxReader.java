package ramo.klevis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class IdxReader {

    public static final String INPUT_IMAGE_PATH_TRAIN_DATA = "resources/train-images.idx3-ubyte";
    public static final String INPUT_IMAGE_PATH_TEST_DATA = "resources/t10k-images.idx3-ubyte";

    public static final String INPUT_LABEL_PATH_TRAIN_DATA = "resources/train-labels.idx1-ubyte";
    public static final String INPUT_LABEL_PATH_TEST_DATA = "resources/t10k-labels.idx1-ubyte";
    public static final int VECTOR_DIMENSION = 784; // HEIGHT = 28, HEIGHT = 28, DIMENSION = 28*28
    private final static Logger LOGGER = LoggerFactory.getLogger(IdxReader.class);

    public static List<LabeledImage> loadData(final int size) {
        return getLabeledImages(INPUT_IMAGE_PATH_TRAIN_DATA, INPUT_LABEL_PATH_TRAIN_DATA, size);
    }

    public static List<LabeledImage> loadTestData(final int size) {
        return getLabeledImages(INPUT_IMAGE_PATH_TEST_DATA, INPUT_LABEL_PATH_TEST_DATA, size);
    }

    private static List<LabeledImage> getLabeledImages(final String inputImagePath, final String inputLabelPath,
                                                       final int amountOfDataSet) {

        final List<LabeledImage> labeledImageArrayList = new ArrayList<>(amountOfDataSet);

        try (FileInputStream inputImage = new FileInputStream(inputImagePath);
             FileInputStream inputLabel = new FileInputStream(inputLabelPath)) {

            // Skipped due to the format of the dataset
            inputImage.skip(16);
            inputLabel.skip(8);
            LOGGER.debug("Available bytes in inputImage stream after read: " + inputImage.available());
            LOGGER.debug("Available bytes in inputLabel stream after read: " + inputLabel.available());

            double[] imagePixels = new double[VECTOR_DIMENSION];

            LOGGER.info("Creating ADT filed with Labeled Images ...");
            long start = System.currentTimeMillis();
            for (int i = 0; i < amountOfDataSet; i++) {

                if (i % 1000 == 0) {
                    LOGGER.info("Number of images extracted: " + i);
                }
                for (int index = 0; index < VECTOR_DIMENSION; index++) {
                    imagePixels[index] = inputImage.read();
                }
                int label = inputLabel.read();
                labeledImageArrayList.add(new LabeledImage(label, imagePixels));
            }
            LOGGER.info("Time to load LabeledImages in seconds: " + ((System.currentTimeMillis() - start) / 1000d));
        } catch (Exception e) {
            LOGGER.error("Something went wrong: \n" + e);
            throw new RuntimeException(e);
        }

        return labeledImageArrayList;
    }

}