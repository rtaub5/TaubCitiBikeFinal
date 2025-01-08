package taub.citi;

public class StationsCache
{
    public Stations getStations()
    {
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        Stations stationInfo = service.getStations().blockingGet();
        return stationInfo;
    }
}
