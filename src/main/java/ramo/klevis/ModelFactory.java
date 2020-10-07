package ramo.klevis;

import java.io.IOException;

public class ModelFactory {
    public static Model makeModel(ModelType type) throws IOException {
        switch (type) {
            case NEURAL: {
                NeuralNetwork nn = new NeuralNetwork();
                nn.init();
                return nn;
            }
            case CONVOLUTIONAL: {
                ConvolutionalNeuralNetwork cnn = new ConvolutionalNeuralNetwork();
                cnn.init();
                return cnn;
            }
            default:
                return null;
        }
    }
}
