package Server;

import HistoryKeeper.HistoryKeeper;
import SettingsParser.SettingsParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private final Logger log = LoggerFactory.getLogger(Server.class);
    private final SettingsParser parser;
    private final HistoryKeeper logger;
    private final ConnectionHolder connectionHolder;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");


    public Server() {
        connectionHolder = ConnectionHolder.getInstance();
        parser = SettingsParser.getInstance();
        logger = HistoryKeeper.getInstance();
    }

    public void start(){
        logger.writeToHistory("Сервер запущен");
        log.info("{} Старт сервера", LocalDateTime.now().format(formatter));

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
