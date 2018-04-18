package quiz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class Client implements ActionListener {

    private static Socket socket;
    private static JFrame frame;
    private static JTextArea question;
    private static JButton startButton;
    private static JTextField answer;
    private static JButton answerButton;
    public static JTextField name;

    public static String getName; // PLAYER NAME

    public static void main(String[] args) throws IOException {
        createFrame();
        actionPerformed();
        connectToServer();

        while (true) {
        }
    }

    /* ACTION LISTENER */
    public static void actionPerformed() {
        /* ANSWER BUTTON LISTENER */
        answerButton.addActionListener((e) -> {
            answerButtonClicked();
        });
        /* START BUTTON LISTENER */
        startButton.addActionListener((e) -> {
            startButton.setEnabled(false);
            name.setEnabled(false);
            try {
                sendAnswer(name.getText());
                questionAcquired();
            } catch (IOException ex) {}
        });
    }

    /* POLACZ Z SERWEREM */
    private static void connectToServer() throws IOException {
        socket = new Socket("localhost", 65432);
    }

    /* SLUCHAJ ODPOWIEDZI */
    private static void questionAcquired() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = bufferedReader.readLine();
        question.setText(message);

    }

    /* WYSLIJ ODPOWIEDZ */
    private static void sendAnswer(String x) throws IOException {
        PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
        sender.println(x);
    }

    /* GENERUJ OKNO KLIENTA */
    private static void createFrame() {
        frame = new JFrame("Client");
        JPanel panel = new JPanel();
        name = new JTextField("");
        question = new JTextArea("");
        startButton = new JButton("Start");
        answer = new JTextField("");
        answerButton = new JButton("Answer");

        GridBagConstraints c = new GridBagConstraints();

        question.setEditable(false);

        panel.setLayout(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 5;
        c.weighty = 8;
        c.ipady = 80;
        c.ipadx = 250;
        panel.add(question, c);
        c.ipady = 0;
        c.ipadx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 1;
        c.gridwidth = 3;
        c.weightx = 8;
        panel.add(name, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        panel.add(answer, c);
        c.weightx = 1;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(startButton, c);
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
        panel.add(answerButton, c);
        frame.add(panel);

        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /* DO LISTENERA NA ODPOWIEDZI */
    public static void answerButtonClicked() {
        if (null != answer.getText()) {
            try {
                sendAnswer(answer.getText().toLowerCase());
                answer.setText("");
                questionAcquired();
                if (question.getText().contains("Wynik")) {
                    sendAnswer(name.getText());
                }
            } catch (IOException ex) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
