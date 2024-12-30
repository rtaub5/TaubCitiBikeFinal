package taub.citi;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class CitiBikeServiceTest
{
    CitiBikeService service = new CitiBikeServiceFactory().getService();

    @Test
    void getStations()
    {
        Stations stations = service.getStations().blockingGet();
        Station station1 = stations.data.stations.get(0);
        assertNotNull(station1.station_type);
        assertNotNull(station1.station_id);
        assertNotNull(station1.name);
    }

    @Test
    void getStatuses()
    {

        Stations stations = service.getStatuses().blockingGet();
        Station station1 = stations.data.stations.get(0);
        assertNotNull(station1.station_id);
        assertNotNull(station1.num_docks_available);
        assertNotNull(station1.last_reported);
    }

    @Test
    void getStatusByStationId()
    {
        String currStationId = "1846085734612252774";
        Stations stations = service.getStatuses().blockingGet();
        Station currStation = stations.data.stations.get(0);
        for (Station station : stations.data.stations)
        {
            if (station.station_id.equals(currStationId))
            {
                currStation = station;
                break;
            }
        }
        assertEquals(currStation.station_id, currStationId);
    }

    @Test
    void getClosestAvailableBikeLocation()
    {
        double currLat = 40.5;
        double currLon = -73.1;
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationStatus = service.getStatuses().blockingGet();
        int retval = 1; // finding the closest bike
        List<Station> combinedStations =
                combineStationInfo(stationInfo.data.stations, stationStatus.data.stations, retval);
        Station station = findClosestStation(currLat, currLon, combinedStations);
        assertNotEquals(0, station.num_bikes_available);
    }

    @Test
    void getClosestAvailableSlotLocation()
    {
        double currLat = 40.5;
        double currLon = -73.1;
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationStatus = service.getStatuses().blockingGet();
        int retval = 2; // finding the closest slot
        List<Station> combinedStations =
                combineStationInfo(stationInfo.data.stations, stationStatus.data.stations, retval);
        Station station = findClosestStation(currLat, currLon, combinedStations);
        assertNotEquals(0, station.num_docks_available);
    }

    private List<Station> combineStationInfo(List<Station> stationInfo, List<Station> stationStatus, int retVal)
    {
        stationInfo.sort(Comparator.comparing(Station::getStationId));
        stationStatus.sort(Comparator.comparing(Station::getStationId));
        List<Station> combinedStations = new ArrayList<>();
        for (int ix = 0; ix < stationStatus.size(); ix++)
        {
            Station station = stationStatus.get(ix);
            station.lat = stationInfo.get(ix).lat;
            station.lon = stationInfo.get(ix).lon;

            // if finding the closest bike, omit stations with no bikes available
            if (retVal == 1)
            {
                if (station.num_bikes_available != 0)
                {
                    combinedStations.add(station);
                }
            } else if (retVal == 2) // if finding the closest dock, omit stations with no docks available
            {
                if (station.num_docks_available != 0)
                {
                    combinedStations.add(station);
                }
            }
        }
        return combinedStations;
    }

    public Station findClosestStation(double lat, double lon, List<Station> stations)
    {
        Station closestStation = stations.get(0);
        double closestDistance = haversine(lat, lon, stations.get(0).lat, stations.get(0).lon);
        for (Station station : stations)
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
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
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