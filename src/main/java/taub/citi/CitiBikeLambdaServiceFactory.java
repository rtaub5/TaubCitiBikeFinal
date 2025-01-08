package taub.citi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CitiBikeLambdaServiceFactory
{
    public CitiBikeLambdaService getService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://kzi377fhqoxsxmnmx7uz6uhjxm0lymki.lambda-url.us-east-2.on.aws/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(CitiBikeLambdaService.class);
    }
}
