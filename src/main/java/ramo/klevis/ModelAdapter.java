package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapter {
    // TODO: Function always returning True
    public boolean train(ModelType type, Integer train, Integer test) {
        try {
            Trainable trainable = ModelFactory.makeModel(type);
            if (trainable != null) {
                trainable.train(train, test);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // TODO: Function always returning True
    public boolean predict(ModelType type, Image drawImage, Consumer<Integer> uiCallback) {
        try {
            Predictor predictor = ModelFactory.makeModel(type);
            int predict = 0;
            if (predictor != null) {
                predict = predictor.predict(LabeledImageFactory.fromImage(drawImage));
            }
            uiCallback.accept(predict);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
