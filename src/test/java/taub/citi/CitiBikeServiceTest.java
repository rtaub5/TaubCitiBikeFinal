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
        Station station = stations.getStationStatus(currStationId);
        assertEquals(station.station_id, currStationId);
    }

    @Test
    void getClosestAvailableBikeLocation()
    {
        double currLat = 40.5;
        double currLon = -73.1;
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationStatus = service.getStatuses().blockingGet();
        int selection = 1; // finding the closest bike
        Stations combinedStations =
                stationInfo.combineStationInfo(stationStatus.data.stations, selection);
        Station station = combinedStations.findClosestStation(currLat, currLon);
        assertNotEquals(0, station.num_bikes_available);
    }

    @Test
    void getClosestAvailableSlotLocation()
    {
        double currLat = 40.5;
        double currLon = -73.1;
        Stations stationInfo = service.getStations().blockingGet();
        Stations stationStatus = service.getStatuses().blockingGet();
        int selection = 2; // finding the closest slot
        Stations combinedStations =
                stationInfo.combineStationInfo(stationStatus.data.stations, selection);
        Station station = combinedStations.findClosestStation(currLat, currLon);
        assertNotEquals(0, station.num_docks_available);
    }
}