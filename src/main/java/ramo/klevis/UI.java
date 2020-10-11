package ramo.klevis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.function.*;

public class UI {

    private final static Logger LOGGER = LoggerFactory.getLogger(UI.class);

    private final static int FRAME_WIDTH = 1200, FRAME_HEIGHT = 628;

    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);

    private int TRAIN_SIZE = 30000, TEST_SIZE = 10000;

    private DrawArea drawArea;
    private JFrame mainFrame;
    private JPanel drawAndResultPanel;
    private JPanel resultPanel;
    private JSpinner testDataSpinner, trainDataSpinner;
    private ProgressBar progressBar;
    private JFrame progressBarFrame;

    private Function<Integer, Function<Integer, Function<NeuralNetworkType, Boolean>>> trainCallback;
    private Function<Image, Function<Consumer<Integer>, Function<NeuralNetworkType, Boolean>>> testCallback;

    public UI() {
    }

    public void setNeuralNetworkCallbacks(
            Function<Integer, Function<Integer, Function<NeuralNetworkType, Boolean>>> train,
            Function<Image, Function<Consumer<Integer>, Function<NeuralNetworkType, Boolean>>> test) {
        this.trainCallback = train;
        this.testCallback = test;
    }

    public static void setUIManagerSettings() {
        // TURN ON ANTIALIASING
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 16)));
    }

    public void showProgressBar(String message) {
        progressBarFrame = new JFrame();
        progressBar = new ProgressBar(progressBarFrame, true);
        progressBar.showProgressBar(message);
    }

    public void stopProgressBar() {
        progressBar.setVisible(false);
        progressBarFrame.dispose();
    }

    public void initUI() {
        mainFrame = getMainFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(getTopPanel(), BorderLayout.NORTH);
        mainPanel.add(getDrawAndResultPanel(), BorderLayout.CENTER);
        mainPanel.add(getBottomPanel(), BorderLayout.SOUTH);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private JPanel getDrawAndResultPanel() {
        drawAndResultPanel = new JPanel(new GridLayout(0, 2));
        drawArea = new DrawArea();
        drawAndResultPanel.add(drawArea);
        resultPanel = new JPanel();
        drawAndResultPanel.add(resultPanel);
        return drawAndResultPanel;
    }

    private final Consumer<Integer> updateUI = prediction -> {
        JLabel predictNumber = new JLabel("" + prediction);
        predictNumber.setForeground(Color.RED);
        predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
        resultPanel.removeAll();
        resultPanel.add(predictNumber);
        resultPanel.updateUI();
    };

    private JPanel getBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(getRecognizeButtonForSimpleNN());
        bottomPanel.add(getRecognizeButtonForCNN());
        bottomPanel.add(getClearButton());
        bottomPanel.add(getSignatureLabel());
        return bottomPanel;
    }

    private JSpinner getSpinner(int spinnerValue, int spinnerMin, int spinnerMax, int spinnerStepSize) {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(spinnerValue, spinnerMin, spinnerMax, spinnerStepSize);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setFont(sansSerifBold);
        return spinner;
    }

    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 3, 5, 5));

        JLabel trainDataLabel = new JLabel("Train Data");
        trainDataSpinner = getSpinner(TRAIN_SIZE, 10000, 60000, 1000);
        JPanel trainDataPanel = new JPanel();
        trainDataPanel.add(trainDataLabel);
        trainDataPanel.add(trainDataSpinner);

        JLabel testDataLabel = new JLabel("Test Data");
        testDataSpinner = getSpinner(TEST_SIZE, 1000, 10000, 500);
        JPanel testDataPanel = new JPanel();
        testDataPanel.add(testDataLabel);
        testDataPanel.add(testDataSpinner);

        JButton trainNNButton = getTrainButton("Train SimpleNN", "1 or 2 minutes", NeuralNetworkType.SIMPLE);
        JButton trainCNNButton = getTrainButton("Train CNN", "more than 1 hour and >10GB Memory",
                NeuralNetworkType.CONVOLUTIONAL);

        topPanel.add(trainDataPanel);
        topPanel.add(testDataPanel);
        topPanel.add(trainNNButton);
        topPanel.add(trainCNNButton);

        return topPanel;
    }

    private JButton getTrainButton(String text, String requirements, NeuralNetworkType type) {
        JButton button = new JButton(text);
        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to proceed?\nTraining may take " + requirements);
            if (option == JOptionPane.OK_OPTION) {
                ProgressBar progressBar = new ProgressBar(mainFrame);
                SwingUtilities.invokeLater(() -> progressBar.showProgressBar("Training may take " + requirements));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of " + text);
                        Integer trainCount = (Integer) trainDataSpinner.getValue();
                        Integer testCount = (Integer) testDataSpinner.getValue();
                        this.trainCallback.apply(trainCount).apply(testCount).apply(type);
                        LOGGER.info("End of " + text);
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
        signature.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        signature.setForeground(Color.BLUE);
        return signature;
    }

    private JButton getRecognizeButtonForSimpleNN() {
        JButton button = new JButton("Recognize Digit With Simple NN");
        button.addActionListener(
                e -> this.testCallback.apply(drawArea.getImage()).apply(updateUI).apply(NeuralNetworkType.SIMPLE));
        return button;
    }

    private JButton getRecognizeButtonForCNN() {
        JButton button = new JButton("Recognize Digit With Conv NN");
        button.addActionListener(e -> this.testCallback.apply(drawArea.getImage()).apply(updateUI)
                .apply(NeuralNetworkType.CONVOLUTIONAL));
        return button;
    }

    private JButton getClearButton() {
        JButton button = new JButton("Clear");
        button.addActionListener(e -> {
            drawArea.reset();
            drawAndResultPanel.updateUI();
        });
        return button;
    }
}