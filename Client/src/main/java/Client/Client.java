package Client;

import Logger.Logger;
import SettingsParser.SettingsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String HELLO_TEXT = "%s подключился к чату";
    private final Logger logger;
    private final SettingsParser parser;
    private final String textForExit = "exit";
    private Receiver receiver;

    public Client() {
        logger = Logger.getInstance();
        parser = SettingsParser.getInstance();

    }

    public void start(){
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(parser.getHost(), parser.getPort());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.print("Введите Ваш Ник: ");
            String nickname = scanner.nextLine();

            out.println(nickname);

            logger.writeToLog(String.format(HELLO_TEXT, nickname));

            receiver = new Receiver(in);
            receiver.start();

            String msg = "";

            while (!msg.equalsIgnoreCase(textForExit) && !receiver.isInterrupted()) {
                msg = scanner.nextLine();
                out.println(msg);
            }

            receiver.interrupt();

        } catch (IOException e) {
            logger.writeToLog("Ошибка в конструкторе класса " + this.getClass().getSimpleName());
            e.printStackTrace();
        }

    }
}
