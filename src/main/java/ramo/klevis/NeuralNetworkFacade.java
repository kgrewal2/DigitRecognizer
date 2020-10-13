package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.*;

public class NeuralNetworkFacade {
    private final NeuralNetworkCache neuralNetworkCache;

    public NeuralNetworkFacade() throws IOException {
        this.neuralNetworkCache = new NeuralNetworkCache(new NeuralNetworkFactory());
    }

    // Integer -> Integer -> NeuralNetworkType -> Boolean
    public Function<Integer, Function<Integer, Function<NeuralNetworkType, Boolean>>> train() {
        return train -> test -> type -> {
            try {
                NeuralNetwork neuralNetwork = this.neuralNetworkCache.getNeuralNetwork(type);
                neuralNetwork.train(train, test);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        };
    }

    // Image -> Consumer<Integer> -> NeuralNetworkType -> Boolean
    public Function<Image, Function<Consumer<Integer>, Function<NeuralNetworkType, Boolean>>> test() {
        return drawImage -> uiCallback -> type -> {
            try {
                NeuralNetwork neuralNetwork = this.neuralNetworkCache.getNeuralNetwork(type);
                LabeledImage image = new LabeledImageFactory().create(drawImage);
                int predict = neuralNetwork.predict(image);
                uiCallback.accept(predict);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        };
    }
}
