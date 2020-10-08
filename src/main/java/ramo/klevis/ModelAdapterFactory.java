package ramo.klevis;

import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class ModelAdapterFactory {
    private MemoizedAIModelFactory factory;

    public ModelAdapterFactory() throws IOException {
        this.factory = new MemoizedAIModelFactory(new NeuralNetworkFactory());
    }

    public Function3<AIModelType, Integer, Integer, Boolean> makeTrainAdapter(Model model) {
        return (type, train, test) -> {
            try {
                Trainable trainable = this.factory.makeAIModel(type);
                model.train(trainable, train, test);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        };
    }

    public Function3<AIModelType, Image, Consumer<Integer>, Boolean> makePredictAdapter(Model model) {
        return (AIModelType type, Image drawImage, Consumer<Integer> uiCallback) -> {
            try {
                Predictor predictor = this.factory.makeAIModel(type);
                LabeledImage image = LabeledImageFactory.fromImage(drawImage);
                int predict = model.predict(predictor, image);

                uiCallback.accept(predict);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        };
    }
}
