package ramo.klevis;

import java.io.IOException;

public interface Trainable {
    public void train(Integer train, Integer test) throws IOException;
}
