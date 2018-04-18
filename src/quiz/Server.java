package quiz;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Server {

    private static final int PORT = 65432;
    private static ServerSocket serverSocket;
    private static JTextArea logs; // LOG TEXT AREA IN FRAME
    public static int id = 1; // CLIENT ID

    public static void main(String[] args) throws IOException {
        serverFrame();
        startServer(PORT);
        logsAdd("Server Active");

        while (true) {
            Socket client = serverSocket.accept();
            new ServerThreads(id, client).start();
            logsAdd("Client " + id++ + ": connected");
        }
    }

    /* LOG CREATOR */
    public static void logsAdd(String x) {
        logs.append(x + "\n");
    }

    /* START SERVER */
    private static void startServer(int x) {
        try {
            serverSocket = new ServerSocket(x);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* FRAME CONSTRUCTOR */
    private static void serverFrame() {
        JFrame frame = new JFrame("Server");
        JPanel panel = new JPanel();
        logs = new JTextArea("Started\n");
        JScrollPane scroll = new JScrollPane(logs);

        scroll.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
        logs.setEditable(false);
        panel.setLayout(new BorderLayout());
        panel.add(scroll);
        frame.add(panel);

        frame.setVisible(true);
        frame.setSize(350, 250);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
