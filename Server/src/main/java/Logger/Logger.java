package Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final static String PATH_TO_SAVE_LOGS = "./src/main/java/Logs/%s";
    private static Logger instance;

    private Logger() {
    }

    public static Logger getInstance() {
        if(instance==null){
            synchronized (Logger.class){
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public boolean writeToLog(String msg) {
        File pathToLogs = new File(String.format(PATH_TO_SAVE_LOGS, LocalDate.now().format(FORMATTER_DATE)));
        if (!pathToLogs.exists()) {
            pathToLogs.mkdirs();
        }
        try (PrintWriter printWriter = new PrintWriter(
                new FileWriter(pathToLogs + "/file.log", true))) {
            printWriter.write(LocalDateTime.now().format(FORMATTER) + " " + msg + "\n");
            printWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
