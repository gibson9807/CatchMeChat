package Model.Server;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PrepareClientList extends Thread {

    private HashMap<Object, Object> clientsMap;

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
                    //msgBox.append(key + ": usunięte!");

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
