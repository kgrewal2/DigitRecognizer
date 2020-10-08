package ramo.klevis;

import java.io.IOException;
import java.util.function.*;

public class Model {
    void train(Trainable trainable, Integer train, Integer test) {
        try {
            trainable.train(train, test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int predict(Predictor predictor, LabeledImage image) {
        int result = predictor.predict(image);
        return result;
    }
}
