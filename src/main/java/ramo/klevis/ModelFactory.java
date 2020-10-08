package ramo.klevis;

import java.io.IOException;

public interface ModelFactory {
    public Model makeModel(ModelType t) throws IOException;
}
