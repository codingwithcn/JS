package Server.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session {
    private List<String> allowedTypes = new ArrayList<String>();
    private String sessionId;
    private Map<String, Object> sessionData = new HashMap<String, Object>();

    protected Session(String id) {
        // The types of data that are allowed to be sent to the client and saved for client... Makes it easier for data transport
        allowedTypes.add(int.class.getName());
        allowedTypes.add(String.class.getName());
        allowedTypes.add(Boolean.class.getName());

        sessionId = id;
    }

    public int length() {
        return sessionData.size();
    }
    
    /**
     * Sets the data for the session
     * @param key - The key to save the data under
     * @param value - The value of the sessiondata
     */
    public void set(String key, Object value) {
        if (allowedTypes.contains(value.getClass().getName())) {
            sessionData.put(key, value);
        } else {
            throw new IllegalArgumentException("The type of data you are trying to set is not allowed");
        }
    }

    /**
     * Removes a session data from user session
     * @param key - The key you want to remove
     */
    public void removeKey(String key) {
        sessionData.remove(key);
    }

    /**
     * Gets the data for the session
     * @param key - The key to get the data from
     * @return - The data for the session
     */
    public Object get(String key) {
        return sessionData.get(key);
    }

    /**
     * Returns session id, would most likely be users ip address
     * @return - session id
     */
    public String id() {
        return sessionId;
    }
}
