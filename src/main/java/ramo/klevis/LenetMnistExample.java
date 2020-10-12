package ramo.klevis;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.LearningRatePolicy;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LenetMnistExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(LenetMnistExample.class);
    // TODO: I changed the path here. (kgrewal2)
    private static final String OUTPUT = "./LeNet500.zip";

    public static void main(String[] args) throws Exception {
        int nInputChannels = 1;
        int nPossibleOutcomes = 10;
        int testBatchSize = 64;
        int nEpochs = 100;
        int nIterations = 1;
        int seed = 123;

        LOGGER.info("Load data....");
        DataSetIterator mnistTrain = new MnistDataSetIterator(testBatchSize, true, 12345);

        LOGGER.info("Build model....");
        // learningRateSchedule is Map<nIterations, learningRate>
        Map<Integer, Double> learningRateSchedule = new HashMap<>();
        learningRateSchedule.put(0, 0.01);
        learningRateSchedule.put(1000, 0.005);
        learningRateSchedule.put(3000, 0.001);

        // Training
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(nIterations)
                .regularization(true).l2(0.0005)
                .learningRate(.01)
                // Uncomment the line below for learning decay and bias
                // .biasLearningRate(0.02)
                .learningRateDecayPolicy(LearningRatePolicy.Schedule)
                /*
                NOTE: learningRateSchedule defined below overrides the rate set in `.learningRate()`
                If you are using the Transfer Learning API, the same override will carry over to your new model configuration
                */
                .learningRateSchedule(learningRateSchedule)
                // Uncomment the block below to use inverse policy rate decay for learning rate
                /*
                .learningRateDecayPolicy(LearningRatePolicy.Inverse)
                .lrPolicyDecayRate(0.001)
                .lrPolicyPower(0.75)
                */
                .weightInit(WeightInit.XAVIER).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                // Uncomment the line below to configure Nesterovs
                // .updater(new Nesterovs(0.9))
                .list().layer(0, new ConvolutionLayer.Builder(5, 5)
                // nIn() and nOut specifies the depth
                // nOut is the number of filters to be applied
                .nIn(nInputChannels).stride(1, 1).nOut(20).activation(Activation.IDENTITY).build())
                .layer(1,
                        new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX).kernelSize(2, 2).stride(2, 2)
                                .build())
                .layer(2, new ConvolutionLayer.Builder(5, 5)
                        // Note that nIn need not be specified in later layers
                        .stride(1, 1).nOut(50).activation(Activation.IDENTITY).build())
                .layer(3,
                        new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX).kernelSize(2, 2).stride(2, 2)
                                .build())
                .layer(4, new DenseLayer.Builder().activation(Activation.RELU).nOut(500).build())
                .layer(5,
                        new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nOut(nPossibleOutcomes)
                                .activation(Activation.SOFTMAX).build())
                .setInputType(InputType.convolutionalFlat(28, 28, 1))
                .backprop(true).pretrain(false).build();

                // TODO: What to do with this comment?
        /*
         * Regarding the .setInputType(InputType.convolutionalFlat(28,28,1)) line: This
         * does a few things. (a) It adds preprocessors, which handle things like the
         * transition between the convolutional/subsampling layers and the dense layer
         * (b) Does some additional configuration validation (c) Where necessary, sets
         * the nIn (number of input neurons, or input depth in the case of CNNs) values
         * for each layer based on the size of the previous layer (but it won't override
         * values manually set by the user) InputTypes can be used with other layer
         * types too (RNNs, MLPs etc) not just CNNs. For normal images (when using
         * ImageRecordReader) use InputType.convolutional(height,width,depth). MNIST
         * record reader is a special case, that outputs 28x28 pixel grayscale
         * (nChannels=1) images, in a "flattened" row vector format (i.e., 1x784
         * vectors), hence the "convolutionalFlat" input type used here.
         */

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        LOGGER.info("Train model....");
        model.setListeners(new ScoreIterationListener(1));
        DataSetIterator mnistTest = null;
        for (int i = 0; i < nEpochs; i++) {
            model.fit(mnistTrain);
            LOGGER.info("*** Completed epoch {} ***", i);
            if (mnistTest == null) {
                mnistTest = new MnistDataSetIterator(10000, false, 12345);
            }
            LOGGER.info("Evaluate model....");
            Evaluation eval = model.evaluate(mnistTest);
            if (eval.accuracy() >= 0.9901) {
                File saveLocation = new File(OUTPUT);
                ModelSerializer.writeModel(model, saveLocation, true);
                LOGGER.info("found ");
                break;
            }
            LOGGER.info(eval.stats());
            mnistTest.reset();
        }
        LOGGER.info("**********Example finished**********");
    }
}