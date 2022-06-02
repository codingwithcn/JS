package Server.handler.routes.routeTest;

import java.io.IOException;

import Server.handler.RouteHandler;

import com.sun.net.httpserver.HttpExchange;

import Server.utils.Session.*;

public class TestRoute extends RouteHandler {

    public TestRoute() throws IOException {
        super();

        getLogger().info("TestRoute Handler initialized");
    }

    @Override
    public void PostHandler(HttpExchange he) throws IOException {
        sendJSONResponse(he, getParamaters(he) );
    }

    @Override
    public void GetHandler(HttpExchange he) throws IOException {
        serverResource(he, "/testRoute/index.html");
        Session session = ServerSession.getSession(he);

        if (session.get("test") == null) {
            session.set("test", "test");
        }else {
            getLogger().info("Session already created");
        }
    }

    @Override
    public boolean isGzippable() {
        return true;
    }

    @Override
    public boolean isCasheable() {
        return true;
    }

    @Override
    public String getRoute() {
        return "/testRoute";
    }
    
}
