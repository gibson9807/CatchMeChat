package Model.Server;

import Model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class MsgRead extends Thread {
    Socket socket;
    User user;
    private HashMap<User, Object> clientsMap;

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
                    //msgBox.append(user + ": usunięty(a)! \n");
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
            //msgBox.append(key + ": usunięte!");
            new PrepareClientList().start();
        }
    }
}
