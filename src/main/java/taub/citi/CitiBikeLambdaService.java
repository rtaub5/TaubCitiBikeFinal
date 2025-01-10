package taub.citi;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.*;

public interface CitiBikeLambdaService
{
    @Headers("Content-Type: application/json")
    @POST("/")
    Single<CitiBikeRequestHandler.CitiBikeResponse> sendBikeRoute(@Body CitiBikeRequest request);

}
