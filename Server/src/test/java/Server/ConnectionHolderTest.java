package Server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ConnectionHolderTest {

    static ConnectionHolder connectionHolder = ConnectionHolder.getInstance();
    static Connection connection = Mockito.mock(Connection.class);
    static Connection connection1 = Mockito.mock(Connection.class);

    @BeforeAll
    static void addAll() {
        connectionHolder.add(connection);
        connectionHolder.add(connection1);
    }

    @Test
    void addTest() {
        int expected = 2;
        int result = connectionHolder.getConnections().size();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void removeTest() {
        connectionHolder.remove(connection1);

        int expected = 1;
        int result = connectionHolder.getConnections().size();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void containTest() {
        Connection result = connectionHolder.getConnections().get(0);

        Connection expected = connection;

        Assertions.assertEquals(expected, result);
    }
}
