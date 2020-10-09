package ramo.klevis;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by klevis.ramo on 11/24/2017.
 */
public class Run {

    private final static Logger LOGGER = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        LOGGER.info("Application is starting ... ");
        UI.setUIManagerSettings();
        UI ui = new UI();
        ui.showProgressBar("Collecting data this make take several seconds!");
        NeuralNetworkFacade neuralNetworkFacade = new NeuralNetworkFacade();
        ui.setFacade(neuralNetworkFacade);
        ui.stopProgressBar();
        ui.initUI();
    }
}
