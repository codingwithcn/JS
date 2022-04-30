import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import Server.constant.ServerConstant;
public class Main {

    // Get references to Java's built-in logging classes.
	private static final Logger 		logger = Logger.getGlobal();
	private static final ConsoleHandler handler = new ConsoleHandler();

	private static final ClassLoader loader = Main.class.getClassLoader();

	private static Server server;
	private static String serverHome;
	private static int port;

    public static void main(String[] args) {
		// Configure global logger for console logging.
		logger.addHandler(handler);
		logger.setUseParentHandlers(false);

        logger.setLevel(Level.FINE);
		handler.setLevel(Level.FINE);

        logger.info("start");

        serverHome =  args.length > 0 ? args[0] : loader.getResource(ServerConstant.WEBAPP_DIR).getPath();
        port = args.length != 1 ? ServerConstant.DEFAULT_PORT : Integer.parseInt(args[1]);

        ServerConstant.setPathToRoot(serverHome);
        
        server = new Server();

        Thread thread = new Thread(server);

        thread.start();

        Runtime.getRuntime().addShutdownHook(new ShutDown());

        try {
            thread.join();
        }catch (Exception e) {}

        logger.info("end");
    }

    public static Server getServer() {
		return server;
	}

	public static String getServerHome() {
		return serverHome;
	}

	public static int getServerPort() {
		return port;
	}
}