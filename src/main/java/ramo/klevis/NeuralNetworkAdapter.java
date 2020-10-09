package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class NeuralNetworkAdapter {
    private NeuralNetworkCache neuralNetworkCache;

    public NeuralNetworkAdapter() throws IOException {
        this.neuralNetworkCache = new NeuralNetworkCache();
    }

    public boolean train(NeuralNetworkType type, Integer train, int test) {
        try {
            NeuralNetwork neuralNetwork = this.neuralNetworkCache.getNeuralNetwork(type);
            neuralNetwork.train(train, test);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean test(NeuralNetworkType type, Image drawImage, Consumer<Integer> uiCallback) {
        try {
            NeuralNetwork neuralNetwork = this.neuralNetworkCache.getNeuralNetwork(type);
            LabeledImage image = LabeledImageFactory.fromImage(drawImage);
            int predict = neuralNetwork.predict(image);
            uiCallback.accept(predict);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
