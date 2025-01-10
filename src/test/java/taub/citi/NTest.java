package taub.citi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class NTest
{
    @Test
    void handleRequest() throws IOException {

        // given
        String json = readJsonFromResource("bikeRoute.json");

        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
        Context context = mock(Context.class);
        when(event.getBody()).thenReturn(json);

        // when
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
        CitiBikeRequestHandler.CitiBikeResponse citiBikeResponse = handler.handleRequest(event, context);

        // then
        // Lenox Ave & W 146 St
        assertEquals("40.821111", citiBikeResponse.from().lat());
        assertEquals("-73.935971", citiBikeResponse.from().lon());

        // 79 St & Roosevelt Ave
        assertEquals("40.74718", citiBikeResponse.to().lat());
        assertEquals("-73.88675", citiBikeResponse.to().lon());
    }

    public String readJsonFromResource(String filename) {
        // Use getClass().getResourceAsStream() to get the file as an InputStream
        try (InputStream inputStream = getClass().getResourceAsStream("/" + filename); // Add '/' for absolute path
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // Read the content of the file into a StringBuilder
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString(); // Return the file content as a String
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions (file not found, IO errors)
            return null;
        }
    }
}

