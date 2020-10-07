package ramo.klevis;

import java.awt.*;
import java.util.function.Consumer;

public class Recognizer {
    public void recognize(Image drawImage, Predictor neuralNetwork, Consumer<Integer> uiCallback) {
        int predict = neuralNetwork.predict(LabeledImageFactory.fromImage(drawImage));
        uiCallback.accept(predict);
    }
}
