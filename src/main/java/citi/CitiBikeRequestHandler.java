package citi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;

public class CitiBikeRequestHandler implements
        RequestHandler<APIGatewayProxyRequestEvent, CitiBikeRequestHandler.CitiBikeResponse>
{

    @Override
    public CitiBikeResponse handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(body, CitiBikeRequest.class);
        CitiBikeResponse response = findClosestStations(request);
        return response;
    }

    private CitiBikeResponse findClosestStations(CitiBikeRequest request)
    {
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationStatus = service.getStatuses().blockingGet();
        Stations stationsWithBikes =
                stationInfo.combineStationInfo(stationStatus.data.stations, Selection.BIKE);
        Station startingStation = stationsWithBikes.findClosestStation(request.from().lat, request.from().lon);
        Stations stationsWithSlots = stationInfo.
                combineStationInfo(stationStatus.data.stations, Selection.SLOT);
        Station returningStation = stationsWithSlots.findClosestStation(request.to().lat, request.to().lon);
        CitiBikeResponse response = new CitiBikeResponse(request.from(), startingStation, returningStation, request.to());
        return  response;
    }

    record From(
            double lat,
            double lon
    )
    {}

    record To(
            double lat,
            double lon
    ) {}

    record CitiBikeResponse(
       From from,
       Station startingStation,
       Station endingStation,
       To to
    ) {}


}
