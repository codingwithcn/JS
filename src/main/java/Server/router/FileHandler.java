package Server.router;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import Server.handler.RouteHandler;

public class FileHandler extends RouteHandler {

    public FileHandler(boolean gzippable, boolean casheable) throws IOException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public void PostHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void GetHandler(HttpExchange he) throws IOException {
        // TODO Auto-generated method stub
        serverResource(he, he.getRequestURI().getPath());
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
        return "/file";
    }
    
}
