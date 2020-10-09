package ramo.klevis;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.Image;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Created by klevis.ramo on 11/24/2017.
 */
public class Run {

    private final static Logger LOGGER = LoggerFactory.getLogger(Run.class);
    private static JFrame mainFrame = new JFrame();

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        LOGGER.info("Application is starting ... ");

        ModelAdapterFactory adapterFactory = new ModelAdapterFactory();

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
