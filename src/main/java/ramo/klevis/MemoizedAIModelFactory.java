package ramo.klevis;

import java.io.IOException;
import java.util.*;

public class MemoizedAIModelFactory implements AIModelFactory {
    private Map<AIModelType, AIModel> cache;
    protected AIModelFactory factory;

    public MemoizedAIModelFactory(AIModelFactory factory) throws IOException {
        this.cache = new HashMap<AIModelType, AIModel>();

        NeuralNetworkSimple nns = (NeuralNetworkSimple) factory.makeAIModel(AIModelType.NEURAL);
        nns.init();
        this.cache.put(AIModelType.NEURAL, nns);

        NeuralNetworkConvolutional nnc = (NeuralNetworkConvolutional) factory.makeAIModel(AIModelType.CONVOLUTIONAL);
        nnc.init();
        this.cache.put(AIModelType.CONVOLUTIONAL, nnc);

        this.factory = factory;
    }

    public AIModel makeAIModel(AIModelType type) throws IOException {
        AIModel cachedModel = this.cache.get(type);
        if (cachedModel != null) {
            return cachedModel;
        } else {
            AIModel ret = this.factory.makeAIModel(type);
            this.cache.put(type, ret);

            return ret;
        }
    }
}
