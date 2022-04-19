import Model.Server.ClientAccept;

import javax.swing.*;
import java.io.IOException;

public class Server extends JFrame {
    private final static int SOCKET_PORT = 8080;
    private JPanel mainPanel;
    private JPanel serverStatus;
    private JPanel serverList;
    private JLabel sStatus;
    private JTextArea msgBox;

 /*   ServerSocket serverSocket;
    HashMap clientsMap = new HashMap();*/

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
        try {
            this.sStatus.setText("Serwer uruchomiony");
            new ClientAccept(msgBox).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*class ClientAccept extends Thread {
        Socket socket;
        User newUser;

        @Override
        public void run() {
            try {
                while (true) {
                    socket = serverSocket.accept();
                    newUser = createNewUser(socket);

                    if (clientsMap.containsKey(newUser)) {
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("Jesteś już zarejestrowany");
                    } else {
                        clientsMap.put(newUser, socket);
                        msgBox.append(newUser.getName() + " dołączył(a)! \n");
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        sendListUser(newUser);
                        new MsgRead(socket, newUser).start();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private User createNewUser(final Socket socket) throws IOException {
            String userName = new DataInputStream(this.socket.getInputStream()).readUTF();
            return new User(userName);
        }

        private void sendListUser(final User newUser) {
            Set clientKeySet = clientsMap.keySet();
            for (final Object o : clientKeySet) {
                User key = (User) o;
                createDataOutputStreamForClientMap(key, newUser);
            }
        }

        private void createDataOutputStreamForClientMap(User key, User newUser) {
            try {
                new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF("user:" + newUser.getName());
            } catch (Exception ex) {
                clientsMap.remove(key);
                msgBox.append(key + ": usunięte!");
                new PrepareClientList().start();
            }
        }
    }
*/
    /*public class MsgRead extends Thread {
        Socket socket;
        User user;

        MsgRead(Socket socket, User user) {
            this.socket = socket;
            this.user = user;
        }

        @Override
        public void run() {
            try {
                while (!clientsMap.isEmpty()) {
                    String inputStreamText = new DataInputStream(socket.getInputStream()).readUTF();
                    if (inputStreamText.equals("closedWindow")) {
                        clientsMap.remove(user);
                        msgBox.append(user + ": usunięty(a)! \n");
                        new PrepareClientList().start();
                        Set<User> clientsSet = clientsMap.keySet();
                        Iterator itr = clientsSet.iterator();
                        while (itr.hasNext()) {
                            User key = (User) itr.next();
                            if (key.equalsIgnoreCase(user)) {
                                createDataOutputStreamForClientMap(key, inputStreamText);
                            }
                        }
                    } else if (inputStreamText.contains("PrivateMsg")) {
                        inputStreamText = inputStreamText.substring(20);
                        StringTokenizer st = new StringTokenizer(inputStreamText, ":");
                        User id = new User(st.nextToken());
                        inputStreamText = st.nextToken();
                        String str = "< " + user.getName() + " do " + id.getName() + " > " + inputStreamText;
                        createDataOutputStreamForClientMap(id, str);

                    } else {
                        Set clientKeySet = clientsMap.keySet();
                        Iterator itr = clientKeySet.iterator();
                        while (itr.hasNext()) {
                            User key = (User) itr.next();
                            if (key.equalsIgnoreCase(user)) {
                                String str = "< " + user.getName() + " do wszystkich >" + inputStreamText;
                                createDataOutputStreamForClientMap(key, str);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void createDataOutputStreamForClientMap(User key, String str) {
            try {
                new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(str);
            } catch (Exception ex) {
                clientsMap.remove(key);
                msgBox.append(key + ": usunięte!");
                new PrepareClientList().start();
            }
        }
    }
*/
   /* class PrepareClientList extends Thread {
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
*/
    public static void main(String[] args) {
        JFrame frame = new Server("CatchMe Server");
        frame.setVisible(true);
    }
}


