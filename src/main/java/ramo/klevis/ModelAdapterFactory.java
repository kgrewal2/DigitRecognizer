package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapterFactory {
    private NeuralNetworkCache neuralNetworkCache;

    public ModelAdapterFactory() throws IOException {
        this.neuralNetworkCache = new NeuralNetworkCache();
    }

    public boolean train(NeuralNetworkType type, Integer train, int test){
        try {
            Trainable trainable = this.neuralNetworkCache.getAIModel(type);
            trainable.train(train, test);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean test(NeuralNetworkType type, Image drawImage, Consumer<Integer> uiCallback){
        try {
            Predictor predictor = this.neuralNetworkCache.getAIModel(type);
            LabeledImage image = LabeledImageFactory.fromImage(drawImage);
            int predict = predictor.predict(image);
            uiCallback.accept(predict);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
