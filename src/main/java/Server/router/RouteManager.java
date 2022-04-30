package Server.router;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpHandler;

import Server.constant.ServerConstant;
import Server.handler.RouteHandler;

// Loading all the routes
import Server.router.routes.TestRoute;

import com.sun.net.httpserver.HttpExchange;

public class RouteManager implements HttpHandler {
    
    private static RouteManager instance;
    
    private static final Logger LOGGER = Logger.getLogger(RouteManager.class.getName());

    private Map<String, RouteHandler> routeHandlers;

    private FileHandler fileRequestHandler;

    private RouteManager() throws IOException {
        routeHandlers = new HashMap<String, RouteHandler>();
        fileRequestHandler = new FileHandler(true, true);

        // Load in all the route handlers here

        // Example:
        this.addHandler("/", new TestRoute(true, true));

        LOGGER.info("RouteManager initialized");
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
        String path =  getPath(he);

        LOGGER.info("Route Requested With RouteManager: "  + path);

        if (routeHandlers.containsKey(path)) {
            RouteHandler handle = routeHandlers.get(path);

            handle.handle(he);
        }else {
            // If the route is not found, then we will try to serve a file instead
            fileRequestHandler.handle(he);
        }
    }

    public String getPath(HttpExchange he) {
        return he.getRequestURI().getPath();
    }

    public String getMethod(HttpExchange he) {
        return he.getRequestMethod();
    }
    
}
