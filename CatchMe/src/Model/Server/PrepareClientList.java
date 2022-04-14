package Model.Server;

import Model.User;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PrepareClientList extends Thread {

    private final HashMap<User, Object> clientsMap;

    public PrepareClientList(final HashMap<User, Object> clientsMap, final JTextArea msgBox) {
        this.clientsMap = clientsMap;
    }

    public void run() {
        StringBuilder ids = new StringBuilder();
        Set<User> k = clientsMap.keySet();
        Iterator<User> itr = k.iterator();

        while (itr.hasNext()) {
            User key = itr.next();
            ids.append(key).append(",");
        }

        if (ids.length() != 0)
            ids = new StringBuilder(ids.substring(0, ids.length() - 1));
        itr = k.iterator();

        while (itr.hasNext()) {
            User key = (User) itr.next();
            try {
                new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(":;.,/=" + ids);
            } catch (IOException e) {
                e.printStackTrace();
                clientsMap.remove(key);
            }
        }
    }
}
