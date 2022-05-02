package Logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {
    private static AppLogger instance;

    private final Logger 		logger = Logger.getGlobal();
	private final ConsoleHandler handler = new ConsoleHandler();

    private AppLogger() {
        logger.addHandler(handler);
		logger.setUseParentHandlers(false);

        logger.setLevel(Level.FINE);
		handler.setLevel(Level.FINE);
    }

    public static AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }

        return instance;
    }

    public void log(Level level, String message) {
        logger.log(level, message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warning(String message) {
        logger.warning(message);
    }

    public void severe(String message) {
        logger.severe(message);
    }
}
