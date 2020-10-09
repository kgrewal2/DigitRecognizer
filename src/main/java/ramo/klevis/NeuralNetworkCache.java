package ramo.klevis;

import java.io.IOException;
import java.util.*;

public class NeuralNetworkCache {
    private Map<NeuralNetworkType, NeuralNetwork> cache;
    protected NeuralNetworkFactory factory;

    public NeuralNetworkCache() throws IOException {
        this.factory = new NeuralNetworkFactory();
        this.cache = new HashMap<>();

        NeuralNetworkSimple nns = (NeuralNetworkSimple) factory.makeAIModel(NeuralNetworkType.NEURAL);
        nns.init();
        this.cache.put(NeuralNetworkType.NEURAL, nns);

        NeuralNetworkConvolutional nnc = (NeuralNetworkConvolutional) factory.makeAIModel(NeuralNetworkType.CONVOLUTIONAL);
        nnc.init();
        this.cache.put(NeuralNetworkType.CONVOLUTIONAL, nnc);

        this.factory = factory;
    }

    public NeuralNetwork getAIModel(NeuralNetworkType type) throws IOException {
        NeuralNetwork cachedModel = this.cache.get(type);
        if (cachedModel != null) {
            return cachedModel;
        } else {
            NeuralNetwork newNeuralNetwork = this.factory.makeAIModel(type);
            this.cache.put(type, newNeuralNetwork);
            return newNeuralNetwork;
        }
    }
}
