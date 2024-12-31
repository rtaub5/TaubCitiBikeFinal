package taub.citi;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CitiBikeService
{
    @GET("/gbfs/en/station_information.json")
    Single<Stations> getStations();

    @GET("/gbfs/en/station_status.json")
    Single<Stations> getStatuses();
}
