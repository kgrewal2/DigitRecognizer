package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapterFactory {
    protected AIModelFactory factory;

    public ModelAdapterFactory(AIModelFactory factory) throws IOException {
        this.factory = factory;
    }

    public Function3<AIModelType, Integer, Integer, Boolean> makeTrainAdapter(Model model) {
        return (type, train, test) -> {
            try {
                Trainable trainable = this.factory.makeAIModel(type);
                model.train(trainable, train, test);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        };
    }

    public Function3<AIModelType, Image, Consumer<Integer>, Boolean> makePredictAdapter(Model model) {
        return (AIModelType type, Image drawImage, Consumer<Integer> uiCallback) -> {
            try {
                Predictor predictor = this.factory.makeAIModel(type);
                LabeledImage image = LabeledImageFactory.fromImage(drawImage);
                int predict = model.predict(predictor, image);

                uiCallback.accept(predict);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        };
    }
}
