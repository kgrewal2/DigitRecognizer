package ramo.klevis;

import java.io.IOException;

public class ModelFactory {
    private static NeuralNetworkSimple nn = null;
    private static NeuralNetworkConvolutional cnn = null;

    public static Model makeModel(ModelType type) throws IOException {
        switch (type) {
            case NEURAL: {
                if (nn != null) {
                    return nn;
                }
                // TODO: 2 lines below can be turned into one line
                NeuralNetworkSimple network = new NeuralNetworkSimple();
                nn = network;
                nn.init();

                return nn;
            }
            case CONVOLUTIONAL: {
                if (cnn != null) {
                    return cnn;
                }
                // TODO: 2 lines below can be turned into one line
                NeuralNetworkConvolutional network = new NeuralNetworkConvolutional();
                cnn = network;
                cnn.init();
                return cnn;
            }
            default:
                return null;
        }
    }
}
