package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapter {
    private MemoizedModelFactory factory;

    public ModelAdapter() throws IOException {
        this.factory = new MemoizedModelFactory(new NeuralNetworkFactory());
    }

    public boolean train(ModelType type, Integer train, Integer test) {
        try {
            Trainable trainable = this.factory.makeModel(type);
            trainable.train(train, test);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean predict(ModelType type, Image drawImage, Consumer<Integer> uiCallback) {
        try {
            Predictor predictor = this.factory.makeModel(type);
            int predict = 0;
            predict = predictor.predict(LabeledImageFactory.fromImage(drawImage));

            uiCallback.accept(predict);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
