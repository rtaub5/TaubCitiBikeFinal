package taub.citi;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import taub.citi.view.RoutePainter;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapController
{
    CitiBikeLambdaService service = new CitiBikeLambdaServiceFactory().getService();
    CitiBikeRequestHandler.CitiBikeResponse response;
    List<Waypoint> orderedWaypoints;
    CitiBikeRequest request;
    Station startingStation;
    Station endingStation;
    GeoPosition start;
    GeoPosition end;
    JXMapViewer mapViewer;
    public boolean mapped = false;
    CompoundPainter<JXMapViewer> painter;
    Set<Waypoint> waypoints = new HashSet<>();
    WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
    RoutePainter routePainter;

    public MapController(JXMapViewer mapViewer)
    {
        this.mapViewer = mapViewer;
    }

    public void getStations()
    {
        CitiBikeRequestHandler.Location locationStart =
                new CitiBikeRequestHandler.Location(start.getLatitude(), start.getLongitude());
        CitiBikeRequestHandler.Location locationEnd =
                new CitiBikeRequestHandler.Location(end.getLatitude(), end.getLongitude());
        request = new CitiBikeRequest(locationStart, locationEnd);
        Disposable disposable = service.sendBikeRoute(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        (response) -> handleResponse(response),
                        Throwable::printStackTrace);
    }

    private void handleResponse(CitiBikeRequestHandler.CitiBikeResponse response)
    {
        startingStation = response.startingStation();
        endingStation = response.endingStation();
        displayRoute();
    }

    private void displayRoute()
    {
        GeoPosition startPosition = new GeoPosition(startingStation.lat, startingStation.lon);
        GeoPosition endPosition = new GeoPosition(endingStation.lat, endingStation.lon);
        orderedWaypoints.add(1, new DefaultWaypoint(startPosition));
        orderedWaypoints.add(2, new DefaultWaypoint(endPosition));
        waypoints = Set.copyOf(orderedWaypoints);
        waypointPainter.setWaypoints(waypoints);
        List<GeoPosition> track = new ArrayList<>();

        for (Waypoint waypoint : orderedWaypoints)
        {
            track.add(waypoint.getPosition());
        }
        routePainter = new RoutePainter(track);
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(track), 0.7);
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);
        painters.add(waypointPainter);
        painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
        mapped = true;
    }

    public void doPoints()
    {
        Waypoint[] points = waypoints.toArray(new Waypoint[0]);
        start = points[0].getPosition();
        end = points[1].getPosition();
        orderedWaypoints = new ArrayList<>();
        orderedWaypoints.add(points[0]);
        orderedWaypoints.add(points[1]);
        getStations();
    }

    public void clearScreen()
    {
        if (!mapped)
        {
            waypoints.clear();
            waypointPainter.setWaypoints(waypoints);
            mapViewer.setOverlayPainter(waypointPainter);
        } else
        {
            painter.removePainter(waypointPainter);
            painter.removePainter(routePainter);
            mapped = false;
        }
    }

    public void whenMouseClicked(int x, int y)
    {
        Point2D.Double point = new Point2D.Double(x, y);
        GeoPosition position = mapViewer.convertPointToGeoPosition(point);
        waypoints.add(new DefaultWaypoint(position));
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    public int getWaypointsSize()
    {
        return waypoints.size();
    }
}
