package ramo.klevis;

import java.io.IOException;
import java.util.*;

public class MemoizedModelFactory implements ModelFactory {
    private Map<ModelType, Model> cache;
    protected ModelFactory factory;

    public MemoizedModelFactory(ModelFactory factory) throws IOException {
        this.cache = new HashMap<ModelType, Model>();

        NeuralNetworkSimple nns = (NeuralNetworkSimple) factory.makeModel(ModelType.NEURAL);
        nns.init();
        this.cache.put(ModelType.NEURAL, nns);

        NeuralNetworkConvolutional nnc = (NeuralNetworkConvolutional) factory.makeModel(ModelType.CONVOLUTIONAL);
        nnc.init();
        this.cache.put(ModelType.CONVOLUTIONAL, nnc);

        this.factory = factory;
    }

    public Model makeModel(ModelType type) throws IOException {
        Model cachedModel = this.cache.get(type);
        if (cachedModel != null) {
            return cachedModel;
        } else {
            Model ret = this.factory.makeModel(type);
            this.cache.put(type, ret);

            return ret;
        }
    }
}
