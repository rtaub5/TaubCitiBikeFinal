package taub.citi;

public class Station
{
    public String station_type;
    public boolean has_kiosk;
    public String name;
    public String external_id;
    public boolean electric_bike_surcharge_waiver;
    public double lat;
    public String [] rental_methods;
    public String station_id;
    // rental_urls
    public int capacity;
    public Object [] eightd_station_services;
    public double lon;
    public String short_name;
    public boolean eightd_has_key_dispenser;
    public int region_id;

    // station status fields
    public int num_docks_disabled;
    public int num_docks_available;
    public long legacy_id;
    public int num_ebikes_available;
    public int num_scooters_available;
    public int num_bikes_disabled;
    public int num_bikes_available;
    public int is_returning;
    public long last_reported;
    public boolean eightd_has_available_keys;
    public int is_renting;
    public int is_installed;

    public String getStationId()
    {
        return station_id;
    }

}

