package ramo.klevis;

public class NeuralNetworkFactory {
    public NeuralNetwork create(NeuralNetworkType type) {
        switch (type) {
            case SIMPLE: {
                return new NeuralNetworkSimple();
            }
            case CONVOLUTIONAL: {
                return new NeuralNetworkConvolutional();
            }
            default:
                throw new IllegalArgumentException("Invalid Type: " + type.toString());
        }
    }
}
