package ramo.klevis;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.util.concurrent.Executors;

/**
 * Created by klevis.ramo on 11/24/2017.
 */
public class Run {

    private final static Logger LOGGER = LoggerFactory.getLogger(Run.class);
    private static JFrame mainFrame = new JFrame();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        LOGGER.info("Application is starting ... ");

        NeuralNetworkAdapter adapterFactory = new NeuralNetworkAdapter();

        ProgressBar progressBar = new ProgressBar(mainFrame, true);
        progressBar.showProgressBar("Collecting data this make take several seconds!");
        UI ui = new UI(adapterFactory);
        Executors.newCachedThreadPool().submit(() -> {
            try {
                ui.initUI();
            } finally {
                progressBar.setVisible(false);
                mainFrame.dispose();
            }
        });
    }
}
