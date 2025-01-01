package citi;

public class Station
{
    //CHECKSTYLE:OFF
  //  public String station_type;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
   // public boolean has_kiosk;
    //CHECKSTYLE:ON
    public String name;
    //CHECKSTYLE:OFF
   // public String external_id;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public boolean electric_bike_surcharge_waiver;
    //CHECKSTYLE:ON
    public double lat;
    //CHECKSTYLE:OFF
  //  public String [] rental_methods;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
    public String station_id;
    //CHECKSTYLE:ON
  //  public int capacity;
    //CHECKSTYLE:OFF
  //  public Object [] eightd_station_services;
    //CHECKSTYLE:ON
    public double lon;
    //CHECKSTYLE:OFF
   // public String short_name;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
   // public boolean eightd_has_key_dispenser;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public int region_id;
    //CHECKSTYLE:ON

    // station status fields
    //CHECKSTYLE:OFF
  //  public int num_docks_disabled;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
    public int num_docks_available;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
   // public long legacy_id;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public int num_ebikes_available;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public int num_scooters_available;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
 //   public int num_bikes_disabled;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
    public int num_bikes_available;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
   // public int is_returning;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
 //   public long last_reported;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public boolean eightd_has_available_keys;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
 //   public int is_renting;
    //CHECKSTYLE:ON
    //CHECKSTYLE:OFF
  //  public int is_installed;
    //CHECKSTYLE:ON

    public String getStationId()
    {
        return station_id;
    }


}

