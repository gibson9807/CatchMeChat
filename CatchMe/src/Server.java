import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class Server extends JFrame {
    private final static int SOCKET_PORT = 8080;
    private JPanel mainPanel;
    private JPanel serverStatus;
    private JPanel serverList;
    private JLabel sStatus;
    private JTextArea msgBox;

    ServerSocket serverSocket;
    HashMap clientsMap = new HashMap();

    public Server(String title) {
        super(title);
        initializeGuiComponents();
        startServer();
    }

    private void initializeGuiComponents() {
        //Initialization of GUI components
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }

    private void startServer() {
        //Server features
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
            this.sStatus.setText("Serwer uruchomiony");
            new ClientAccept().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientAccept extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Socket socket = serverSocket.accept();
                    String str = new DataInputStream(socket.getInputStream()).readUTF();
                    if (clientsMap.containsKey(str)) {
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("Jesteś już zarejestrowany");
                    } else {
                        clientsMap.put(str, socket);
                        msgBox.append(str + " dołączył(a)! \n");
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("");
                        new MsgRead(socket, str).start();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    class MsgRead extends Thread {
        Socket socket;
        String ID;

        MsgRead(Socket socket, String ID) {
            this.socket = socket;
            this.ID = ID;
        }

        @Override
        public void run() {
            try {
                while (!clientsMap.isEmpty()) {
                    String inputStreamText = new DataInputStream(socket.getInputStream()).readUTF();
                    if (inputStreamText.equals("closedWindow")) {
                        clientsMap.remove(ID);
                        msgBox.append(ID + ": usunięty(a)! \n");
                        new PrepareClientList().start();
                        Set<String> k = clientsMap.keySet();
                        Iterator itr = k.iterator();
                        while (itr.hasNext()) {
                            String key = (String) itr.next();
                            if (!key.equalsIgnoreCase(ID)) {
                                createDataOutputStreamForClientMap(key, inputStreamText);
                            }
                        }
                    } else if (inputStreamText.contains("PrivateMsg")) {
                        inputStreamText = inputStreamText.substring(20);
                        StringTokenizer st = new StringTokenizer(inputStreamText, ":");
                        String id = st.nextToken();
                        inputStreamText = st.nextToken();
                        String str = "< " + ID + " do " + id + " > " + inputStreamText;
                        createDataOutputStreamForClientMap(id, str);

                    } else {
                        Set clientKeySet = clientsMap.keySet();
                        Iterator itr = clientKeySet.iterator();
                        while (itr.hasNext()) {
                            String key = (String) itr.next();
                            if (!key.equalsIgnoreCase(ID)) {
                                String str = "< " + ID + " do wszystkich >" + inputStreamText;
                                createDataOutputStreamForClientMap(key, str);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void createDataOutputStreamForClientMap(String key, String str) {
            try {
                new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(str);
            } catch (Exception ex) {
                clientsMap.remove(key);
                msgBox.append(key + ": usunięte!");
                new PrepareClientList().start();
            }
        }
    }

    class PrepareClientList extends Thread {
        public void run() {
            try {
                String ids = "";
                Set k = clientsMap.keySet();
                Iterator itr = k.iterator();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    ids += key + ",";
                }
                if (ids.length() != 0)
                    ids = ids.substring(0, ids.length() - 1);
                itr = k.iterator();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    try {
                        new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(":;.,/=" + ids);
                    } catch (Exception ex) {
                        clientsMap.remove(key);
                        msgBox.append(key + ": usunięte!");

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Server("CatchMe Server");
        frame.setVisible(true);
    }
}


