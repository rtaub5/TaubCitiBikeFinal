package taub.citi;

import com.google.gson.Gson;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class MapController
{
    CitiBikeLambdaService service = new CitiBikeLambdaServiceFactory().getService();
    CitiBikeRequestHandler.CitiBikeResponse response;
    CitiBikeRequest request;
    Station startingStation;
    Station endingStation;
    GeoPosition start;
    GeoPosition end;

    public MapController(List<Waypoint> orderedWaypoints)
    {
        start = orderedWaypoints.get(0).getPosition();
        end = orderedWaypoints.get(1).getPosition();
    }

    private void getStations(CitiBikeRequestHandler.CitiBikeResponse response)
    {
       startingStation = response.startingStation();
       endingStation = response.endingStation();

    }

    public Station getStartingStation()
    {
        return startingStation;
    }

    public Station getEndingStation()
    {
        return  endingStation;
    }

    public Station getStations()
    {
        CitiBikeRequestHandler.Location locationStart =
                new CitiBikeRequestHandler.Location(start.getLatitude(), start.getLongitude());
        CitiBikeRequestHandler.Location locationEnd =
                new CitiBikeRequestHandler.Location(end.getLatitude(), end.getLongitude());
        request = new CitiBikeRequest(locationStart, locationEnd);
        //  response = service.sendBikeRoute(request).blockingGet();
        Disposable disposable = service.sendBikeRoute(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        (response) -> getStations(response),
                        Throwable::printStackTrace);
     return startingStation;
    }
}
