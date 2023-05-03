package client;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class RegServiceTest {
    @Test
    public void testRegService() throws IOException {
        BufferedReader income = mock(BufferedReader.class);
        when(income.readLine()).thenReturn("What is your name?", "Welcome");
        PrintWriter outcome = mock(PrintWriter.class);

        // имитируем ввод для сканера
        String input = "Ivan";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        boolean actual = new Client().regService(income, outcome);

        verify(income, times(2)).readLine();
        verify(outcome, only()).println(anyString());
        assertTrue(actual);
    }
}
