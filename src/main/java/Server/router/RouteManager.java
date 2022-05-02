package Server.router;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;

import Logger.AppLogger;
import Server.handler.RouteHandler;
import Server.router.routes.routeTest.TestRoute;

import com.sun.net.httpserver.HttpExchange;

public class RouteManager implements HttpHandler {

    private static RouteManager instance;

    private static final AppLogger LOGGER = AppLogger.getInstance();

    private Map<String, RouteHandler> routeHandlers;

    private FileHandler fileRequestHandler;

    private RouteManager() throws IOException {
        routeHandlers = new HashMap<String, RouteHandler>();
        fileRequestHandler = new FileHandler(true, true);

        // Load in all the route handlers here

        // Example:
        this.addHandler("/testRoute", new TestRoute(true, true));
    }

    public static RouteManager getInstance() throws IOException {
        if (instance == null) {
            instance = new RouteManager();
        }

        return instance;
    }

    public void addHandler(String route, Object handler) {
        routeHandlers.put(route, (RouteHandler) handler);
    }

    @Override
    public void handle(HttpExchange he) throws RouteNotFoundException, IOException {
        String path = getPath(he);
        String method = getMethod(he);

        if (routeHandlers.containsKey(path)) {
            RouteHandler handle = routeHandlers.get(path);

            handle.handle(he);
        } else {
            // If the route is not found, then we will try to serve a file instead
            fileRequestHandler.handle(he);
        }

        LOGGER.info(
            clientAddress(he) + " -  - " + "`" + method + " - " + path + "`" + " - " + responseCode(he)
        );
    }

    /**
     * Returns the response code of the HttpExchange
     * 
     * @param he - The http exchange to get the response code from.
     * @return - Response Code
     */
    private int responseCode(HttpExchange he) {
        return he.getResponseCode();
    }

    /**
     * Returns the URI of the HttpExchange
     * 
     * @param he - The http exchange to get the URI from.
     * @return - The URI of the HttpExchange.
     */
    public String getPath(HttpExchange he) {
        return he.getRequestURI().getPath();
    }

    /**
     * Returns the method of the HttpExchange
     * 
     * @param he - The http exchange to get the method from.
     * @return - Method of request
     */
    public String getMethod(HttpExchange he) {
        return he.getRequestMethod();
    }

    /**
     * Returns the client address of the HttpExchange
     * 
     * @param he - The http exchange to get the client address from.
     * @return - Ip address of client
     */
    public String clientAddress(HttpExchange he) {
        String fwdAddr = he.getRequestHeaders().getFirst("X-Forwarded-For");
        if (fwdAddr != null && !fwdAddr.isEmpty()) {
            // it is behind a proxy.
            return fwdAddr;
        }

        // not a proxy, return natural address.
        InetAddress addr = he.getRemoteAddress().getAddress();
        return addr == null ? he.getRemoteAddress().getHostName() : addr.getHostAddress();
    }

}
