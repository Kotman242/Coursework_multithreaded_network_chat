package Server;

import HistoryKeeper.HistoryKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Connection extends Thread {

    private final Socket socket;
    private final Logger log;
    private BufferedReader in;
    private PrintWriter out;
    private final HistoryKeeper historyKeeper;
    private final String textForExit = "exit";
    private String nickname;
    private final ConnectionHolder connectionHolder;
    private final DateTimeFormatter formatterForMsg = DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm");

    public Connection(Socket socket) {
        historyKeeper = HistoryKeeper.getInstance();
        connectionHolder = ConnectionHolder.getInstance();
        log= LoggerFactory.getLogger(Connection.class);
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            closeAll();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            nickname = in.readLine();
            printAndWrite("подключился к чату.",true);
            sendMessageEveryone("подключился к чату.");
            String msg;
            while (true) {
                msg = in.readLine();
                if (msg.equalsIgnoreCase(textForExit)) {
                    closeAll();
                    break;
                }
                printAndWrite(msg);
                sendMessageEveryone(msg);

            }
        } catch (SocketException e) {
            closeAll();
        } catch (IOException e) {
            messageError("работе метода run");
            e.printStackTrace();
        }
    }

    public void closeAll() {
        try {
            connectionHolder.getConnections().remove(this);
            socket.close();
            in.close();
            out.close();
            printAndWrite(String.format("Пользователь %s отключился от чата", nickname),true);
            sendMessageEveryone(String.format("Пользователь %s отключился от чата", nickname));
            this.interrupt();
        } catch (IOException e) {
            messageError("закрытие потока");
            e.printStackTrace();
        }
    }

    private void printAndWrite(String msg) {
        System.out.println("["+LocalDateTime.now().format(formatterForMsg)+"] "+nickname + ": " + msg);
        historyKeeper.writeToHistory("["+LocalDateTime.now().format(formatterForMsg)+"] "+nickname + ": " + msg);
    }

    private void printAndWrite(String msg, boolean sendToLog) {
        if(sendToLog) log.info("{} {} {}",LocalDateTime.now().format(formatterForMsg), nickname, msg);
        historyKeeper.writeToHistory("["+LocalDateTime.now().format(formatterForMsg)+"] "+nickname + ": " + msg);
    }
    private void sendMessageEveryone(String msg) {
        for (Connection connection : connectionHolder.getConnections()) {
            if (connection != this) {
                connection.out.println("["+LocalDateTime.now().format(formatterForMsg)+"] "+nickname + ": " + msg);
            }
        }
    }

    private void messageError(String text) {
        String textOfError = String.format("Ошибка при %s в %s, поток %s",
                text,
                this.getClass().getSimpleName(),
                Thread.currentThread().getName());
        printAndWrite(textOfError,true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(socket, that.socket) && Objects.equals(nickname, that.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, nickname);
    }
}
