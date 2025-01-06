package taub.citi;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CitiBikeService
{
    @GET("/gbfs/en/station_information.json")
    Single<Stations> getStations();

    @GET("/gbfs/en/station_status.json")
    Single<Stations> getStatuses();
}
