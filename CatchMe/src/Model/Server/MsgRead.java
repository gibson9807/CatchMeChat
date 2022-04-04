package Model.Server;

import Model.User;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class MsgRead extends Thread {
    private final JTextArea msgBox;
    private final Socket socket;
    private final User user;
    private final HashMap<User, Object> clientsMap;

    MsgRead(Socket socket, User user, final JTextArea msgBox) {
        this.socket = socket;
        this.user = user;
        this.msgBox = msgBox;
        clientsMap = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (!clientsMap.isEmpty()) {
                String inputStreamText = new DataInputStream(socket.getInputStream()).readUTF();
                if (inputStreamText.equals("closedWindow")) {
                    clientsMap.remove(user);
                    msgBox.append(user + ": usunięty(a)! \n");
                    new PrepareClientList(clientsMap, msgBox).start();
                    Set<User> clientsSet = clientsMap.keySet();
                    Iterator<User> itr = clientsSet.iterator();
                    while (itr.hasNext()) {
                        User key = itr.next();
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
                    Set<User> clientKeySet = clientsMap.keySet();
                    Iterator<User> itr = clientKeySet.iterator();
                    while (itr.hasNext()) {
                        User key = itr.next();
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
        } catch (IOException e) {
            e.printStackTrace();
            clientsMap.remove(key);
            new PrepareClientList(clientsMap, msgBox).start();
        }
    }
}
