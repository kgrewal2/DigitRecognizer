package ramo.klevis;

import java.io.IOException;
import java.util.*;

public class NeuralNetworkCache {
    private Map<NeuralNetworkType, NeuralNetwork> cachedNetworks;
    protected NeuralNetworkFactory factory;

    public NeuralNetworkCache() throws IOException {
        this.factory = new NeuralNetworkFactory();
        this.cachedNetworks = new HashMap<>();

        NeuralNetwork nnSimple = factory.create(NeuralNetworkType.SIMPLE);
        nnSimple.init();
        this.cachedNetworks.put(NeuralNetworkType.SIMPLE, nnSimple);

        NeuralNetworkConvolutional nnConvolutional = (NeuralNetworkConvolutional) factory.create(NeuralNetworkType.CONVOLUTIONAL);
        nnConvolutional.init();
        this.cachedNetworks.put(NeuralNetworkType.CONVOLUTIONAL, nnConvolutional);
    }

    public NeuralNetwork getNeuralNetwork(NeuralNetworkType type) {
        NeuralNetwork cachedModel = this.cachedNetworks.get(type);
        if (cachedModel != null) {
            return cachedModel;
        } else {
            NeuralNetwork newNeuralNetwork = this.factory.create(type);
            this.cachedNetworks.put(type, newNeuralNetwork);
            return newNeuralNetwork;
        }
    }
}
