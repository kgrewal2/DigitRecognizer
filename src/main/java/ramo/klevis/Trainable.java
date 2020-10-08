package ramo.klevis;

import java.io.IOException;

public interface Trainable {
    void train(Integer train, Integer test) throws IOException;
}
