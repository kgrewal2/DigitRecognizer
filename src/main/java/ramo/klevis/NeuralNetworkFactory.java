package ramo.klevis;

import java.io.*;

public class NeuralNetworkFactory{
    public NeuralNetwork create(NeuralNetworkType type) {
        switch (type) {
            case SIMPLE: {
                return new NeuralNetworkSimple();
            }
            case CONVOLUTIONAL: {
                return new NeuralNetworkConvolutional();
            }
            default:
                return null;
        }
    }
}
