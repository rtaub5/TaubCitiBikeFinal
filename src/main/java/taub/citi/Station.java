package taub.citi;

public class Station
{
    public String name;
    public double lat;
    //CHECKSTYLE:OFF
    public String station_id;
    //CHECKSTYLE:ON
    public double lon;
    //CHECKSTYLE:OFF
    public int num_docks_available;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
    public int num_bikes_available;
    //CHECKSTYLE:ON

    public String getStationId()
    {
        return station_id;
    }


}

