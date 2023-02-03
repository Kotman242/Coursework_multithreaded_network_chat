package SettingsParser;

import Logger.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SettingsParser {

    private static final String PATH_TO_SETTINGS = "./src/main/resources/Settings.txt";
    private static SettingsParser instance;
    private final Logger LOGGER;
    private int port=-1;
    private String host;

    private SettingsParser() {
        LOGGER = Logger.getInstance();
    }

    public static SettingsParser getInstance() {
        if (instance == null) {
            instance = new SettingsParser();

        }
        return instance;
    }

    public int getPort() {
        if(port==-1) parse();
        return port;
    }

    public String getHost() {
        if(host.equals(""))parse();
        return host;
    }

    public void parse(){
        try (FileReader fileReader = new FileReader(PATH_TO_SETTINGS)) {
            Properties properties = new Properties();
            properties.load(fileReader);

            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("host");
            LOGGER.writeToLog(String.format("%s загружен успешно, port: %d, host: %s", this.getClass().getSimpleName(), port, host));
        } catch (IOException e) {
            LOGGER.writeToLog("Ошибка в конструкторе класса " + this.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
