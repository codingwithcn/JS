package Server.router.routes;

import java.io.IOException;

import Server.handler.RouteHandler;

import com.sun.net.httpserver.HttpExchange;

public class TestRoute extends RouteHandler {

    public TestRoute(boolean gzippable, boolean casheable) throws IOException {
        super(gzippable, casheable);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void PostHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        sendJSONResponse(he, getParamaters(he) );
    }

    @Override
    public void GetHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        serverResource(he, "/index.html");
    }

    @Override
    public void PutHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void DeleteHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        
    }
    
}
