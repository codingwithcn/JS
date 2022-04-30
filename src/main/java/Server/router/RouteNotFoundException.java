package Server.router;

public class RouteNotFoundException extends RuntimeException  {
    public RouteNotFoundException(String message) {
        super(message);
    }
}
