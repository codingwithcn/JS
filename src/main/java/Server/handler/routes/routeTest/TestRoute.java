package Server.handler.routes.routeTest;

import java.io.IOException;

import Server.handler.RouteHandler;

import com.sun.net.httpserver.HttpExchange;

import Server.utils.Session.*;

public class TestRoute extends RouteHandler {

    public TestRoute(boolean gzippable, boolean casheable) throws IOException {
        super();
        //TODO Auto-generated constructor stub
        getLogger().info("TestRoute Handler initialized");
    }

    @Override
    public void PostHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        sendJSONResponse(he, getParamaters(he) );
    }

    @Override
    public void GetHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        serverResource(he, "/testRoute/index.html");
        Session session = ServerSession.getSession(he);

        if (session.get("test") == null) {
            session.set("test", "test");
        }else {
            getLogger().info("Session already created");
        }
    }

    @Override
    public void PutHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void DeleteHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isGzippable() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCasheable() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getRoute() {
        // TODO Auto-generated method stub
        return "/testRoute";
    }
    
}
