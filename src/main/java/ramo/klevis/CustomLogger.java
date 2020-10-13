package ramo.klevis;


import org.apache.hadoop.util.Time;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class CustomLogger {
    private static List<JTextPane> observerLoggers;
    private enum LogType{
        INFO,
        DEBUG,
        ERROR
    }
    private static long initTime;

    public static void init(){
        initTime = Time.now();
        observerLoggers = new ArrayList<>();
    }

    private static void addLogger(JTextPane newObserver){
        observerLoggers.add(newObserver);
    }

    public static JScrollPane getLogger(){
        JTextPane newLoggerPane = new JTextPane();
        newLoggerPane.setBackground(Color.BLACK);
        newLoggerPane.setForeground(Color.WHITE);
        newLoggerPane.setFont(UIUtils.MONO14);
        addLogger(newLoggerPane);
        JScrollPane scrollablePane = new JScrollPane(newLoggerPane);
        scrollablePane.setBorder(UIUtils.getTitledBorder("Logger"));
        return scrollablePane;
    }

    public static void info(String string, Class<?> c){
        log(string, c, LogType.INFO);
    }

    public static void debug(String string, Class<?> c){
        log(string, c, LogType.DEBUG);
    }

    public static void error(String string, Class<?> c){
        log(string, c, LogType.ERROR);
    }

    private static void log(String string, Class<?> c, LogType logType){
        String className = c.toString().replace("class ","");
        String time = String.valueOf(Time.now() - initTime);
        String threadName = Thread.currentThread().getName();
        String formattedMessage = "\n" + time + " [" + threadName + "] " + logType.toString() + " ";
        String message = className + " - " + string;
        for (JTextPane logger: observerLoggers) {
            StyledDocument doc = logger.getStyledDocument();
            try {
                doc.insertString(doc.getLength(),formattedMessage,getStyleFor(logType));
                doc.insertString(doc.getLength(),message,null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            logger.setCaretPosition(logger.getDocument().getLength());
        }
    }

    private static SimpleAttributeSet getStyleFor(LogType logType){
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, getColorFor(logType));
        return style;
    }

    private static Color getColorFor(LogType logType){
        switch (logType){
            case INFO:
                return Color.GREEN;
            case DEBUG:
                return Color.ORANGE;
            case ERROR:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }
}
