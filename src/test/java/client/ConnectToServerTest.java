package client;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class ConnectToServerTest {

    @Test
    public void testConnectToServer_whenFail() {
        boolean actual = new Client().connectToServer();
        assertFalse(actual);
    }
}
