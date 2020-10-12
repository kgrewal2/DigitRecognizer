package ramo.klevis;

import java.io.IOException;
import java.util.*;
import java.util.function.*;

public class NeuralNetworkCache {
    protected final Map<NeuralNetworkType, NeuralNetwork> cachedNetworks;
    protected final Function<NeuralNetworkType, NeuralNetwork> factory;

    public NeuralNetworkCache(Function<NeuralNetworkType, NeuralNetwork> factory) throws IOException {
        this.cachedNetworks = new HashMap<>();
        this.factory = factory;

        NeuralNetwork nnSimple = this.factory.apply(NeuralNetworkType.SIMPLE);
        nnSimple.init();
        this.cachedNetworks.put(NeuralNetworkType.SIMPLE, nnSimple);

        NeuralNetworkConvolutional nnConvolutional = (NeuralNetworkConvolutional) this.factory
                .apply(NeuralNetworkType.CONVOLUTIONAL);
        nnConvolutional.init();
        this.cachedNetworks.put(NeuralNetworkType.CONVOLUTIONAL, nnConvolutional);
    }

    public NeuralNetwork getNeuralNetwork(NeuralNetworkType type) {
        NeuralNetwork cachedModel = this.cachedNetworks.get(type);
        if (cachedModel != null) {
            return cachedModel;
        } else {
            NeuralNetwork newNeuralNetwork = this.factory.apply(type);
            this.cachedNetworks.put(type, newNeuralNetwork);
            return newNeuralNetwork;
        }
    }
}
