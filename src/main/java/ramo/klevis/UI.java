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

    private static final int FRAME_WIDTH = 1200, FRAME_HEIGHT = 628;
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
    private DrawArea drawArea;
    private JFrame mainFrame;
    private JPanel drawAndDigitPredictionPanel;
    private JPanel resultPanel;
    private JSpinner testField, trainField;

    private Function3<ModelType, Integer, Integer, Boolean> trainCallback;
    private Function3<ModelType, Image, Consumer<Integer>, Boolean> predictCallback;

    private final Consumer<Integer> updateUI = prediction -> {
        JLabel predictNumber = new JLabel("" + prediction);
        predictNumber.setForeground(Color.RED);
        predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
        resultPanel.removeAll();
        resultPanel.add(predictNumber);
        resultPanel.updateUI();
    };

    public UI(Function3<ModelType, Integer, Integer, Boolean> train,
            Function3<ModelType, Image, Consumer<Integer>, Boolean> predict) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));

        this.trainCallback = train;
        this.predictCallback = predict;
    }

    public void initUI() {
        mainFrame = getMainFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(getTopPanel(), BorderLayout.NORTH);
        mainPanel.add(getDrawAndDigitPredictionPanel(), BorderLayout.CENTER);
        mainPanel.add(getSignatureLabel(), BorderLayout.SOUTH);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private JPanel getDrawAndDigitPredictionPanel() {
        drawAndDigitPredictionPanel = new JPanel(new GridLayout());
        drawAndDigitPredictionPanel.add(getActionPanel());
        drawArea = new DrawArea();
        drawAndDigitPredictionPanel.add(drawArea);
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        drawAndDigitPredictionPanel.add(resultPanel);
        return drawAndDigitPredictionPanel;
    }

    private JPanel getActionPanel() {
        JPanel actionPanel = new JPanel(new GridLayout(8, 1));
        actionPanel.add(getRecognizeButtonForSimpleNN());
        actionPanel.add(getRecognizeButtonForCNN());
        actionPanel.add(getClearButton());
        return actionPanel;
    }

    private JButton getRecognizeButtonForSimpleNN() {
        JButton button = new JButton("Recognize Digit With Simple NN");
        button.addActionListener(e -> this.predictCallback.apply(ModelType.NEURAL, drawArea.getImage(), updateUI));
        return button;
    }

    private JButton getRecognizeButtonForCNN() {
        JButton button = new JButton("Recognize Digit With Conv NN");
        button.addActionListener(e -> this.predictCallback.apply(ModelType.CONVOLUTIONAL, drawArea.getImage(), updateUI));
        return button;
    }

    private JButton getClearButton() {
        JButton button = new JButton("Clear");
        button.addActionListener(e -> {
            drawArea.setImage(null);
            drawArea.repaint();
            drawAndDigitPredictionPanel.updateUI();
        });
        return button;
    }

    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());

        JButton trainNN = new JButton("Train NN");
        trainNN.addActionListener(e -> {
            int i = JOptionPane.showConfirmDialog(mainFrame, "Are you sure this may take some time to train?");
            if (i == JOptionPane.OK_OPTION) {
                ProgressBar progressBar = new ProgressBar(mainFrame);
                SwingUtilities
                        .invokeLater(() -> progressBar.showProgressBar("Training may take one or two minutes..."));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of train Neural Network");
                        this.trainCallback.apply(ModelType.NEURAL, (Integer) trainField.getValue(),
                                (Integer) testField.getValue());
                        LOGGER.info("End of train Neural Network");
                    } finally {
                        progressBar.setVisible(false);
                    }
                });
            }
        });

        JButton trainCNN = new JButton("Train Convolutional NN");
        trainCNN.addActionListener(e -> {

            int i = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure, training requires >10GB memory and more than 1 hour?");

            if (i == JOptionPane.OK_OPTION) {
                ProgressBar progressBar = new ProgressBar(mainFrame);
                SwingUtilities.invokeLater(() -> progressBar.showProgressBar("Training may take a while..."));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of train Convolutional Neural Network");
                        this.trainCallback.apply(ModelType.CONVOLUTIONAL, (Integer) trainField.getValue(),
                                (Integer) testField.getValue());
                        LOGGER.info("End of train Convolutional Neural Network");
                    } finally {
                        progressBar.setVisible(false);
                    }
                });
            }
        });

        topPanel.add(trainCNN);
        topPanel.add(trainNN);
        JLabel tL = new JLabel("Training Data");
        tL.setFont(sansSerifBold);
        topPanel.add(tL);
        int TRAIN_SIZE = 30000;
        SpinnerNumberModel modelTrainSize = new SpinnerNumberModel(TRAIN_SIZE, 10000, 60000, 1000);
        trainField = new JSpinner(modelTrainSize);
        trainField.setFont(sansSerifBold);
        topPanel.add(trainField);

        JLabel ttL = new JLabel("Test Data");
        ttL.setFont(sansSerifBold);
        topPanel.add(ttL);
        int TEST_SIZE = 10000;
        SpinnerNumberModel modelTestSize = new SpinnerNumberModel(TEST_SIZE, 1000, 10000, 500);
        testField = new JSpinner(modelTestSize);
        testField.setFont(sansSerifBold);
        topPanel.add(testField);
        return topPanel;

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
        JLabel signature = new JLabel("ramok.tech", JLabel.HORIZONTAL);
        signature.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        signature.setForeground(Color.BLUE);
        return signature;
    }

}