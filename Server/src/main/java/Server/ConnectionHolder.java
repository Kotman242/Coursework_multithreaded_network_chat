package Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConnectionHolder {
    private static ConnectionHolder instance;
    private final List<Connection> connections;

    private ConnectionHolder() {
        connections = Collections.synchronizedList(new ArrayList<>());
    }

    public static ConnectionHolder getInstance() {
        if (instance == null) {
            instance = new ConnectionHolder();
        }
        return instance;
    }

    public boolean add(Connection connection) {
        return connections.add(connection);
    }

    public boolean remove(Connection connection) {
        return connections.remove(connection);
    }

    public List<Connection> getConnections() {
        return connections;
    }
}
