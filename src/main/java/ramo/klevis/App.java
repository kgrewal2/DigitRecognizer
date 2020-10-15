package ramo.klevis;

import org.apache.hadoop.util.Time;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private final static Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        long APP_START_TIME = Time.now();
        BasicConfigurator.configure();
        UIUtils.setUIManagerSettings();
        CustomLogger.init();
        LOGGER.info("Application is starting ... ");
        UI ui = UI.getInstance();
        ui.showProgressBar();
        NeuralNetworkFacade neuralNetworkFacade = new NeuralNetworkFacade();
        ui.setNeuralNetworkCallbacks(neuralNetworkFacade.train(), neuralNetworkFacade.test());
        ui.stopProgressBar();
        ui.initUI();
        long APP_LOAD_TIME = (Time.now() - APP_START_TIME) / 1000;
        CustomLogger.info("App started in " + APP_LOAD_TIME + " sec",App.class);
    }
}
