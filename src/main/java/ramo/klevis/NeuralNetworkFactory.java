package ramo.klevis;

import java.util.function.Function;

@FunctionalInterface
public interface NeuralNetworkFactory extends Function<NeuralNetworkType, NeuralNetwork> {
    public NeuralNetworkFactory factory = type -> {
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
    };
}
