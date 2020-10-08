package ramo.klevis;

import java.io.*;

public class NeuralNetworkFactory implements ModelFactory {
    public Model makeModel(ModelType type) throws IOException {
        switch (type) {
            case NEURAL: {
                NeuralNetworkSimple network = new NeuralNetworkSimple();
                network.init();
                return network;
            }
            case CONVOLUTIONAL: {
                NeuralNetworkConvolutional network = new NeuralNetworkConvolutional();
                network.init();
                return network;
            }
            default:
                return null;
        }
    }
}
