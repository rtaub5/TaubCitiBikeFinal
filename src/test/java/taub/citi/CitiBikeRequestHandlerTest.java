package taub.citi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitiBikeRequestHandlerTest
{

    APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
    Context context = mock(Context.class);

    CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
 //   @Test
//    void handleRequest()
//    {
//        APIGatewayProxyRequestEvent event = mock(APIGatewayProxyRequestEvent.class);
//        Context context = mock(Context.class);
//        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
//        CitiBikeRequestHandler.CitiBikeResponse response = handler.handleRequest(event, context);
//       assertNotNull(response);







    @Test
    void findClosestStations()
    {
        CitiBikeRequestHandler.Location from = new CitiBikeRequestHandler.Location(40.823682398765996, -73.95549774169922);
        CitiBikeRequestHandler.Location to = new CitiBikeRequestHandler.Location(40.785220723422235, -73.9657974243164);
        CitiBikeRequest request = new CitiBikeRequest(from, to);
        CitiBikeRequestHandler handler = new CitiBikeRequestHandler();
        CitiBikeRequestHandler.CitiBikeResponse response = handler.findClosestStations(request);

        assertNotNull(response);


        //  CitiBikeRequest request = new CitiBikeRequest(from, to);


    }
}