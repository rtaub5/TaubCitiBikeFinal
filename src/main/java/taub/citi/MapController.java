package taub.citi;

import com.google.gson.Gson;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MapController
{
    CitiBikeLambdaService service = new CitiBikeLambdaServiceFactory().getService();
    CitiBikeRequestHandler.CitiBikeResponse response;
    GeoPosition start;
    GeoPosition end;
    public MapController(List<Waypoint> orderedWaypoints)
    {
        start = orderedWaypoints.get(0).getPosition();
        end = orderedWaypoints.get(1).getPosition();
        CitiBikeRequestHandler.Location locationStart = new CitiBikeRequestHandler.Location(start.getLatitude(), start.getLongitude());
        CitiBikeRequestHandler.Location locationEnd = new CitiBikeRequestHandler.Location(end.getLatitude(), end.getLongitude());
        CitiBikeRequest request = new CitiBikeRequest(locationStart, locationEnd);
        response = service.sendBikeRoute(request).blockingGet();

    }
    public Station getStartingStation()
    {
        return  response.startingStation();
    }

    public Station getEndingStation()
    {
        return  response.endingStation();
    }
}
