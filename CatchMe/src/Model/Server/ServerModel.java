package Model.Server;

import Model.User;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ServerModel {
    private final static int SOCKET_PORT = 8080;

    ServerSocket serverSocket;
    Map clientsMap = new HashMap<>();

    public ServerModel(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

}
