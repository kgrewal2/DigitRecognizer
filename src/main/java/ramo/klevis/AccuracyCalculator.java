package ramo.klevis;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.earlystopping.scorecalc.ScoreCalculator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccuracyCalculator implements ScoreCalculator<MultiLayerNetwork> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccuracyCalculator.class);
    private final MnistDataSetIterator dataSetIterator;
    int i = 0;

    public AccuracyCalculator(MnistDataSetIterator dataSetIterator) {
        this.dataSetIterator = dataSetIterator;
    }

    @Override
    public double calculateScore(MultiLayerNetwork network) {
        Evaluation evaluate = network.evaluate(dataSetIterator);
        double accuracy = evaluate.accuracy();
        LOGGER.error("Accuracy " + i + " " + accuracy);
        i++;
        return 1 - evaluate.accuracy();
    }
}
