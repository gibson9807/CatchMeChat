package Model.Server;

import Model.User;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class ClientAccept extends Thread {
    private final static int SOCKET_PORT = 8080;
    private final  ServerSocket serverSocket;
    private final HashMap<String, Object> clientsMap;
    private final JTextArea msgBox;
    private Socket socket;

    public ClientAccept(final JTextArea msgBox) throws IOException {
        this.msgBox = msgBox;
        serverSocket = new ServerSocket(SOCKET_PORT);
        clientsMap = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket = serverSocket.accept();
                User newUser = createNewUser(socket);
               // String userNameCheck = new DataInputStream(socket.getInputStream()).readUTF();
                if (clientsMap.containsKey(newUser.getName())) {
                    DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                    dout.writeUTF("AlreadyRegistered");
                } else {
                    clientsMap.put(newUser.getName(), socket);
                    msgBox.append(newUser.getName() + " dołączył(a)! \n");
                    DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                    System.out.println("DOUT w ClientAccept");
                    sendListUser(dout);
                    new MsgRead(socket, newUser, msgBox).start();
                    System.out.println("client accept " + newUser + " ----- " + socket);
                    socket.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private User createNewUser(final Socket socket) throws IOException {
        String userName = new DataInputStream(this.socket.getInputStream()).readUTF();
        return new User(userName);
    }

    private void sendListUser(final DataOutputStream dout) throws IOException {
        Set<String> clientKeySet = clientsMap.keySet();
//        String list="";
//        for (String name : clientKeySet) {
//            list=list+name+",";
//        }
//        if(list.endsWith(",")){
//            list=list.substring(0,list.length()-1);
//        }

        for(String name:clientKeySet){
            dout.writeUTF("user:" + name+"testClientAccept");
            System.out.println("TEST W FOR: "+name);
        }

       // System.out.println(list);
        //dout.writeUTF("user:" + list);
    }

    /*private void createDataOutputStreamForClientMap(User key, User newUser) {
        try {
            new DataOutputStream(((Socket) clientsMap.get(key)).getOutputStream()).writeUTF();
        } catch (IOException e) {
            e.printStackTrace();
            clientsMap.remove(key);
            msgBox.append(key + ": usunięte!");
            new PrepareClientList(clientsMap, msgBox).start();
        }
    }*/
}