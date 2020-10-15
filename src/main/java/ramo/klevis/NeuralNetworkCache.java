package ramo.klevis;

import java.io.IOException;
import java.util.*;

public class NeuralNetworkCache {
    protected final Map<NeuralNetworkType, NeuralNetwork> cachedNetworks;
    protected final NeuralNetworkFactory factory;

    public NeuralNetworkCache(NeuralNetworkFactory factory) throws IOException {
        this.cachedNetworks = new HashMap<>();
        this.factory = factory;

        NeuralNetwork nnSimple = this.factory.create(NeuralNetworkType.SIMPLE);
        nnSimple.init();
        this.cachedNetworks.put(NeuralNetworkType.SIMPLE, nnSimple);

        NeuralNetwork nnConvolutional = this.factory.create(NeuralNetworkType.CONVOLUTIONAL);
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
