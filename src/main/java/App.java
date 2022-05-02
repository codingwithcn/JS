import Logger.AppLogger;
import Server.constant.ServerConstant;

public class App {
    private static final AppLogger logger = AppLogger.getInstance();
	private static final ClassLoader loader = App.class.getClassLoader();

	private static Server server;
	private static String serverHome;
	private static int port;

    public static void main(String[] args) {

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
