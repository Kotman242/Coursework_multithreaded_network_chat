package Client;

import Logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class Receiver extends Thread {
    private final BufferedReader bufferedReader;
    private final Logger logger;

    public Receiver(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        logger = Logger.getInstance();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(bufferedReader.readLine());
            }
        } catch (SocketException e) {
            System.out.println("Сервер отключен!");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            logger.writeToLog("Ошибка в методе run, класса " + this.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
