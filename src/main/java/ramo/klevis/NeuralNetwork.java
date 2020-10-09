package ramo.klevis;

import java.io.IOException;

public interface NeuralNetwork{
    void init() throws IOException;
    void train(Integer train, Integer test) throws IOException;
    int predict(LabeledImage labeledImage);
}
