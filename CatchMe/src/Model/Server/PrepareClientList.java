package Model.Server;

import Model.User;

import javax.swing.*;
import java.io.DataOutputStream;
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
        try {
            StringBuilder ids = new StringBuilder();
            Set k = clientsMap.keySet();
            Iterator itr = k.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                ids. append(key).append(",");
            }
            if (ids.length() != 0)
                ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            itr = k.iterator();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                try {
                    new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF(":;.,/=" + ids);
                } catch (Exception ex) {
                    clientsMap.remove(key);
                    //msgBox.append(key + ": usunięte!");

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
