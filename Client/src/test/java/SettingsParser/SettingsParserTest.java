package SettingsParser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SettingsParserTest {

    SettingsParser settingsParser = SettingsParser.getInstance();

    @Test
    public void getHostTrueTest() {
        String expecte = "127.0.0.0";
        String result = settingsParser.getHost();

        Assertions.assertEquals(expecte, result);
    }

    @Test
    public void getHostFalseTest() {
        String expecte = "127.1.0.1";
        String result = settingsParser.getHost();

        Assertions.assertNotEquals(expecte, result);
    }

    @Test
    public void getPortTrueTest() {
        int result = settingsParser.getPort();
        int expected = 8089;

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void getPortFalseTest() {
        int result = settingsParser.getPort();
        int expected = 9089;

        Assertions.assertNotEquals(expected, result);
    }

}
