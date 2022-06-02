package Server.router;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import Server.handler.RouteHandler;

public class FileHandler extends RouteHandler {

    public FileHandler(boolean gzippable, boolean casheable) throws IOException {
        super();
    }

    @Override
    public void PostHandler(HttpExchange he) throws IOException {
        
    }

    @Override
    public void GetHandler(HttpExchange he) throws IOException {
        serverResource(he, he.getRequestURI().getPath());
    }

    @Override
    public void PutHandler(HttpExchange he) throws IOException {
        
    }

    @Override
    public void DeleteHandler(HttpExchange he) throws IOException {
        
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
        return "/file";
    }
    
}
