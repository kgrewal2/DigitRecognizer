package ramo.klevis;

import java.io.IOException;

public interface AIModelFactory {
    public AIModel makeAIModel(AIModelType t) throws IOException;
}
