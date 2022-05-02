package Server.utils.Session;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class ServerSession {
    private static Map<String, Session> sessions = new HashMap<String, Session>();

    /**
     * Get the session for the given HttpExchange
     * @param he - The HttpExchange that the session is allocated to.
     * @return - The session for the given HttpExchange
     */
    public static Session getSession(HttpExchange he) {
        String ipAddress = clientAddress( he );
        if (sessions.containsKey( ipAddress ) ) {
            return sessions.get( ipAddress );
        } else {
            Session session = new Session( ipAddress );
            sessions.put( ipAddress, session );
            return session;
        }
    }

    /**
     * Returns the client address of the HttpExchange
     * 
     * @param he - The http exchange to get the client address from.
     * @return - Ip address of client
     */
    public static String clientAddress(HttpExchange he) {
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
