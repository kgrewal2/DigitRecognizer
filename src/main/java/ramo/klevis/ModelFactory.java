package ramo.klevis;

import java.io.IOException;

public class ModelFactory {
    private static NeuralNetwork nn = null;
    private static ConvolutionalNeuralNetwork cnn = null;

    public static Model makeModel(ModelType type) throws IOException {
        switch (type) {
            case NEURAL: {
                if (nn != null) {
                    return nn;
                }

                NeuralNetwork network = new NeuralNetwork();
                nn = network;
                nn.init();

                return nn;
            }
            case CONVOLUTIONAL: {
                if (cnn != null) {
                    return cnn;
                }

                ConvolutionalNeuralNetwork network = new ConvolutionalNeuralNetwork();
                cnn = network;
                cnn.init();

                return cnn;
            }
            default:
                return null;
        }
    }
}
