package ramo.klevis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;

public class UI {
    private final static Logger LOGGER = LoggerFactory.getLogger(UI.class);
    private final static int FRAME_WIDTH = 1200, FRAME_HEIGHT = 628;
    private static UI uiInstance = null;
    private DrawArea drawArea;
    private JFrame mainFrame;
    private JPanel drawAndResultPanel;
    private JPanel resultPanel;
    private final Consumer<Integer> updateUI = prediction -> {
        JLabel predictNumber = new JLabel("" + prediction);
        predictNumber.setForeground(Color.DARK_GRAY);
        predictNumber.setFont(new Font("SansSerif", Font.BOLD, mainFrame.getHeight()/4));
        resultPanel.removeAll();
        resultPanel.add(predictNumber);
        resultPanel.updateUI();
    };
    private JSpinner testDataSpinner, trainDataSpinner;
    private JFrame progressBarFrame;
    private Function<Integer, Function<Integer, Function<NeuralNetworkType, Boolean>>> trainCallback;
    private Function<Image, Function<Consumer<Integer>, Function<NeuralNetworkType, Boolean>>> testCallback;

    private UI() {
    }

    public void setNeuralNetworkCallbacks(
            Function<Integer, Function<Integer, Function<NeuralNetworkType, Boolean>>> train,
            Function<Image, Function<Consumer<Integer>, Function<NeuralNetworkType, Boolean>>> test) {
        this.trainCallback = train;
        this.testCallback = test;
    }

    public void showProgressBar() {
        progressBarFrame = new JFrame();
        progressBarFrame.setTitle("Digit Recognizer");
        progressBarFrame.add(UIUtils.getProgressBar("Starting Application: Digit Recognizer"));
        progressBarFrame.setLocationRelativeTo(null);
        progressBarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        progressBarFrame.pack();
        progressBarFrame.setVisible(true);
    }

    public void stopProgressBar() {
        progressBarFrame.dispose();
    }

    public static UI getInstance() {
        if (uiInstance == null) {
            uiInstance = new UI();
        }
        return uiInstance;
    }

    public void initUI() {
        mainFrame = getMainFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(getBottomPanel(), BorderLayout.NORTH);
        mainPanel.add(getCentralPanel(), BorderLayout.CENTER);
        mainPanel.add(getSignatureLabel(), BorderLayout.SOUTH);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private JPanel getCentralPanel() {
        drawAndResultPanel = new JPanel(new GridLayout(0, 2));
        drawArea = new DrawArea();
        drawArea.add(getClearButton());
        drawAndResultPanel.add(drawArea);
        JPanel resultAndLoggerPanel = new JPanel(new GridLayout(2,1));
        resultPanel = getResultPanel();
        resultAndLoggerPanel.add(resultPanel);
        resultAndLoggerPanel.add(CustomLogger.getLogger());
        drawAndResultPanel.add(resultAndLoggerPanel);
        return drawAndResultPanel;
    }

    private JPanel getResultPanel() {
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(UIUtils.getTitledBorder("Result Area"));
        return resultPanel;
    }

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(UIUtils.getTitledBorder("Recognize Digit"));
        bottomPanel.add(getRecognizeButtonFor(NeuralNetworkType.SIMPLE, "Recognize with Simple Neural Network"));
        bottomPanel.add(getRecognizeButtonFor(NeuralNetworkType.CONVOLUTIONAL, "Recognize with Convolutional Neural Network"));
        return bottomPanel;
    }

    private JSpinner getSpinner(int spinnerValue, int spinnerMin, int spinnerMax, int spinnerStepSize) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(spinnerValue, spinnerMin, spinnerMax, spinnerStepSize);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setFont(UIUtils.SANS_SERIF_BOLD16);
        return spinner;
    }

    private JPanel getTrainingPanel() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        topPanel.add(getTrainingOptionsPanel());
        topPanel.add(getStartTrainingPanel());
        return topPanel;
    }

    private JPanel getStartTrainingPanel(){
        JButton trainNNButton = getTrainButton("Train: Simple Neural Network", "1 or 2 minutes", NeuralNetworkType.SIMPLE);
        JButton trainCNNButton = getTrainButton("Train: Convolutional Neural Network", "more than 1 hour and >10GB Memory",
                NeuralNetworkType.CONVOLUTIONAL);

        JPanel startTrainingPanel = new JPanel(new GridLayout(2, 1));
        startTrainingPanel.setBorder(UIUtils.getTitledBorder("Start Training"));
        startTrainingPanel.add(trainNNButton);
        startTrainingPanel.add(trainCNNButton);
        return startTrainingPanel;
    }

    private JPanel getTrainingOptionsPanel(){
        JLabel trainDataLabel = new JLabel("Train Data");
        trainDataSpinner = getSpinner(NeuralNetworkOptions.TRAIN_SIZE, NeuralNetworkOptions.MIN_TRAIN_SIZE, NeuralNetworkOptions.MAX_TRAIN_SIZE, 1000);
        JPanel trainDataPanel = new JPanel();
        trainDataPanel.add(trainDataLabel);
        trainDataPanel.add(trainDataSpinner);

        JLabel testDataLabel = new JLabel("Test Data");
        testDataSpinner = getSpinner(NeuralNetworkOptions.TEST_SIZE, NeuralNetworkOptions.MIN_TEST_SIZE, NeuralNetworkOptions.MAX_TEST_SIZE, 500);
        JPanel testDataPanel = new JPanel();
        testDataPanel.add(testDataLabel);
        testDataPanel.add(testDataSpinner);

        JPanel trainingOptionsPanel = new JPanel(new GridLayout(2, 1));
        trainingOptionsPanel.setBorder(UIUtils.getTitledBorder("Training Options"));
        trainingOptionsPanel.add(trainDataPanel);
        trainingOptionsPanel.add(testDataPanel);
        return trainingOptionsPanel;
    }

    private JButton getTrainButton(String text, String requirements, NeuralNetworkType type) {
        JButton button = UIUtils.getFancyButton(text, Color.LIGHT_GRAY);
        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to proceed?\nTraining may take " + requirements);
            if (option == JOptionPane.OK_OPTION) {
                JProgressBar progressBar = UIUtils.getProgressBar("Training may take "+requirements);
                SwingUtilities.invokeLater(() -> mainFrame.add(progressBar, BorderLayout.NORTH));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of " + text);
                        CustomLogger.info("Started Training: "+text, UI.class);
                        Integer trainCount = (Integer) trainDataSpinner.getValue();
                        Integer testCount = (Integer) testDataSpinner.getValue();
                        this.trainCallback.apply(trainCount).apply(testCount).apply(type);
                        LOGGER.info("End of " + text);
                        CustomLogger.info("Successfully Trained: " + text, UI.class);
                    } finally {
                        progressBar.setVisible(false);
                    }
                });
            }
        });
        return button;
    }

    private JFrame getMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Digit Recognizer");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        ImageIcon imageIcon = new ImageIcon("icon.png");
        mainFrame.setIconImage(imageIcon.getImage());
        return mainFrame;
    }

    private JLabel getSignatureLabel() {
        JLabel signature = new JLabel("ramok.tech", SwingConstants.CENTER);
        signature.setBorder(UIUtils.EMPTY_BORDER);
        signature.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        signature.setForeground(Color.BLUE);
        return signature;
    }

    private JButton getRecognizeButtonFor(NeuralNetworkType type, String label) {
        Color lightBlueColor = new Color(156, 203, 255);
        JButton button = UIUtils.getFancyButton(label, lightBlueColor);
        button.addActionListener(
                e -> {
                    CustomLogger.info("Recognizing Digit with "+type.toString() + " Neural Network", UI.class);
                    this.testCallback.apply(drawArea.getImage()).apply(updateUI).apply(type);
                });
        return button;
    }

    private JButton getClearButton() {
        JButton button = UIUtils.getFancyButton("Clear", Color.LIGHT_GRAY);
        button.addActionListener(e -> {
            drawArea.reset();
            drawAndResultPanel.updateUI();
            CustomLogger.info("Cleared Drawing Panel", UI.class);
        });
        return button;
    }
}