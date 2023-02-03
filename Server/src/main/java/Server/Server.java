package Server;

import Logger.Logger;
import SettingsParser.SettingsParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final SettingsParser parser;
    private final Logger logger;
    private final ConnectionHolder connectionHolder;


    public Server() {
        connectionHolder = ConnectionHolder.getInstance();
        parser = SettingsParser.getInstance();
        logger = Logger.getInstance();
    }

    public void start(){
        logger.writeToLog("Сервер запущен");
        System.out.println("Сервер запущен");

        try (ServerSocket serverSocket = new ServerSocket(parser.getPort())) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Connection connection = new Connection(clientSocket);
                connectionHolder.add(connection);
                connection.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            connectionHolder.getConnections().forEach(connection -> connection.closeAll());
        }
    }
}
