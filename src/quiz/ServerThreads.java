package quiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerThreads extends Thread {

    private static final String URL_SCORE = "C:\\Users\\kmant\\Desktop\\Quiz\\src\\quiz\\Score.txt";
    private static final String URL_QUESTIONS = "C:\\Users\\kmant\\Desktop\\Quiz\\src\\quiz\\Questions.txt";

    Socket socket;
    String amountOfQuestions = "0"; //OVERWRITE IN PROCESS OF COMMUNICATION WITH CLIENT
    String[] question = {"", "", ""};   //SKELETON OF FULL QUESTION PACKAGE
    String playerName = "Guest";    //OVERWRITE OR NOT IN PROCESS OF COMMUNICATION WITH CLIENT
    int score = 0;  
    int id; 

    public ServerThreads(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        char[] num;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;

            File questions = new File(URL_QUESTIONS);
            FileReader reader = new FileReader(questions);
            BufferedReader buffer = new BufferedReader(reader);

            boolean terminateThread = false;
            while ((message = bufferedReader.readLine()) != null) {
                if (message.equals(question[2].toLowerCase())) {
                    score++;
                }
                try {
                    question = buffer.readLine().split(";");
                    sendQuestion(question[1]);
                    Server.logsAdd("Client " + this.id + " score: " + score + "/" + amountOfQuestions); //test co przyszlo
                    amountOfQuestions = question[0];
                } catch (Exception e) {
                    /* SEND RESULT */
                    sendQuestion("Wynik: " + score + "/" + question[0]);
                    playerName = receiveMessage();
                    saveScore(playerName + ": " + score + "/" + question[0]);
                    Server.logsAdd("Client " + this.id + " disconnected");

                    break;
                }
            }
            socket.close();
        } catch (IOException ex) {
        }
    }

    private void saveScore(String x) throws IOException {
        String content = x + "\n";
        Files.write(Paths.get(URL_SCORE), content.getBytes(), StandardOpenOption.APPEND);
    }

    private String receiveMessage() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = null;
        message = bufferedReader.readLine();
        return message;
    }

    private void sendQuestion(String x) throws IOException {
        PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
        sender.println(x);
    }
}
