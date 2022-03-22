package Model.Server;

import Model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ClientAccept extends Thread {
    Socket socket;
    User newUser;
    ServerSocket serverSocket;
    HashMap clientsMap;

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
                    //msgBox.append(newUser.getName() + " dołączył(a)! \n");
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
            //msgBox.append(key + ": usunięte!");
            new PrepareClientList().start();
        }
    }
}