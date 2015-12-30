import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Mounica on 12/25/15.
 */
public class LogManager {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private FileHandler handler;
    private static LogManager instance = null;

    public static LogManager getInstance(){
        if(instance == null){
            prepareLogger();
            instance = new LogManager();
        }

        return instance;
    }

    private static void prepareLogger(){
        try {
            FileHandler handler = new FileHandler("ServerLog.txt");
            logger.addHandler(handler);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String getMessage (String message){
        String logMessage = System.currentTimeMillis() + ": " + message;
        return logMessage;
    }

    public void logINFO (String message){
        logger.log(Level.INFO, getMessage(message));
    }

    public void logError (String message){
        logger.log(Level.SEVERE, getMessage(message));
    }
}
