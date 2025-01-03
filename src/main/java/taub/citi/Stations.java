package taub.citi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Stations
{
    public Data data;

    public Stations(List<Station> stations)
    {
        data = new Data(stations);
    }


    public Station getStationStatus(String currStationId)
    {

        Station currStation = this.data.stations.get(0);
        for (Station station : this.data.stations)
        {
            if (station.station_id.equals(currStationId))
            {
                currStation = station;
                break;
            }
        }
        return currStation;
    }

    // usage is to call the method from the list of stationInfos and pass in the stationstatuses
    // (This method won't work in the opposite order)
    public Stations combineStationInfo(List<Station> stationStatus, Selection selection)
    {
        List<Station> stationInfo = this.data.stations;
        stationInfo.sort(Comparator.comparing(Station::getStationId));
        stationStatus.sort(Comparator.comparing(Station::getStationId));
        List<Station> combinedStations = new ArrayList<>();
        for (int ix = 0; ix < stationStatus.size(); ix++)
        {
            Station station = stationStatus.get(ix);
            station.name = stationInfo.get(ix).name;
            station.lat = stationInfo.get(ix).lat;
            station.lon = stationInfo.get(ix).lon;

            // if finding the closest bike, omit stations with no bikes available
            if (selection == Selection.BIKE && station.num_bikes_available != 0)
            {
                combinedStations.add(station);
            } else if (selection == Selection.SLOT
                    && station.num_docks_available != 0) {
                // if finding the closest dock, omit stations with no docks available
                combinedStations.add(station);
            }
        }
        Stations combined = new Stations(combinedStations);
        return combined;
    }

    public Station findClosestStation(double lat, double lon)
    {
        Station closestStation = this.data.stations.get(0);
        double closestDistance = haversine(lat, lon, this.data.stations.get(0).lat, this.data.stations.get(0).lon);
        for (Station station : this.data.stations)
        {
            double stationLat = station.lat;
            double stationLon = station.lon;
            double distance = haversine(lat, lon, stationLat, stationLon);
            if (distance < closestDistance)
            {
                closestStation = station;
                closestDistance = distance;
            }
        }
        return  closestStation;
    }

    // method taken from chatGPT to determine distance using latitude and longitude
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km

        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Differences in coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = R * c;

        return distance;
    }

}
