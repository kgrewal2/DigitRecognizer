package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapter {
    public boolean train(ModelType type, Integer train, Integer test) {
        try {
            Trainable trainable = ModelFactory.makeModel(type);
            trainable.train(train, test);
        } catch (IOException e) {

        }

        return true;
    }

    public boolean predict(ModelType type, Image drawImage, Consumer<Integer> uiCallback) {
        try {
            Predictor predictor = ModelFactory.makeModel(type);
            int predict = predictor.predict(LabeledImageFactory.fromImage(drawImage));
            uiCallback.accept(predict);
        } catch (IOException e) {

        }

        return true;
    }
}
