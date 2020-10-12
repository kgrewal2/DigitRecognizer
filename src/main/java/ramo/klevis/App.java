package ramo.klevis;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        UIUtilities.setUIManagerSettings();
        LOGGER.info("Application is starting ... ");
        UI ui = UI.getInstance();
        ui.showProgressBar("Collecting data this make take several seconds!");
        NeuralNetworkFacade neuralNetworkFacade = new NeuralNetworkFacade();
        ui.setNeuralNetworkCallbacks(neuralNetworkFacade.train(), neuralNetworkFacade.test());
        ui.stopProgressBar();
        ui.initUI();
    }
}
