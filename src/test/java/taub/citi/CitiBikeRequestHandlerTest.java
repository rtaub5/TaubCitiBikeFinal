package taub.citi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitiBikeRequestHandlerTest
{
    @Test
    void handleRequest()
    {
        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
        Context context = mock(Context.class);
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();

        // given
        String json = readJsonFromResource("bikeRoute.json");

        when(event.getBody()).thenReturn(json);

        // when
        CitiBikeRequestHandler.CitiBikeResponse citiBikeResponse = handler.handleRequest(event, context);

        // then

        // Lenox Ave & W 146 St
        assertEquals("b0bac4ef-bb57-4598-a433-75c281170d5b", citiBikeResponse.startingStation().station_id);
        // Berry St & N 8 St
        assertEquals("66dd01c5-0aca-11e7-82f6-3863bb44ef7c", citiBikeResponse.endingStation().station_id);
        assertNotNull(citiBikeResponse.to());
    }


    @Test
    void findClosestStations()
    {
        // when call handler.findcloseststatinos
        CitiBikeRequestHandler.Location from = new CitiBikeRequestHandler.Location(40.823682398765996, -73.95549774169922);
        CitiBikeRequestHandler.Location to = new CitiBikeRequestHandler.Location(40.785220723422235, -73.9657974243164);
        CitiBikeRequest request = new CitiBikeRequest(from, to);
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
        CitiBikeRequestHandler.CitiBikeResponse response = handler.findClosestStations(request);

        // check to see if response is same as json response straight from citibike server
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations stationStatus = service.getStatuses().blockingGet();
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationsWithBikes =
                stationInfo.combineStationInfo(stationStatus.data.stations, Selection.BIKE);
        Station startingStation = stationsWithBikes.findClosestStation(request.from().lat(), request.from().lon());
        Stations stationsWithSlots = stationInfo
                .combineStationInfo(stationStatus.data.stations, Selection.SLOT);
        Station returningStation = stationsWithSlots.findClosestStation(request.to().lat(), request.to().lon());
        CitiBikeRequestHandler.CitiBikeResponse response2 =
                new CitiBikeRequestHandler.CitiBikeResponse(request.from(), startingStation, returningStation, request.to());


        assertEquals(response.startingStation().station_id, response2.startingStation().station_id);
        assertEquals(response.endingStation().station_id, response2.endingStation().station_id);

    }


    // ChatGPT
    public String readJsonFromResource(String filename) {
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