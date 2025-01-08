package taub.citi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import retrofit2.Call;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CitiBikeLambdaServiceTest
{

    CitiBikeLambdaService service = new CitiBikeLambdaServiceFactory().getService();
    @Test
    void sendBikeRoute()
    {

        InputStream in = ClassLoader.getSystemResourceAsStream("bikeRoute.json");
        Reader reader = new InputStreamReader(in);
        Gson gson = new Gson();
        CitiBikeRequest request = gson.fromJson(reader, CitiBikeRequest.class);
        CitiBikeRequestHandler.CitiBikeResponse response = service.sendBikeRoute(request).blockingGet();
    }
}
