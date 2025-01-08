package taub.citi;

import com.google.gson.Gson;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class StationsCache
{
    public static Instant lastModified;
    CitiBikeService service;
    Stations stationInfo;

    public Stations getStations()
    {
        long duration = Duration.between(lastModified, Instant.now()).toHours();
        if (stationInfo != null && duration < 1)
        {
            return  stationInfo;
        } else if (stationInfo != null && duration > 1) {
            stationInfo = service.getStations().blockingGet();
            lastModified = Instant.now();
            uploadStationsToS3();

        } else if (stationInfo == null && duration < 1)
        {
            readStationsFromS3();
            lastModified = Instant.now();

        } else {
            stationInfo = service.getStations().blockingGet();
            lastModified = Instant.now();
            uploadStationsToS3();

        }
        return  stationInfo;
    }

    private void uploadStationsToS3()
    {
        Region region = Region.US_EAST_2;
        S3Client s3Client = S3Client.builder()
                .region(region)
                .build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("taub.citibike")
                .key("station_information.json")
                .build();
        Gson gson = new Gson();
        String content = gson.toJson(stationInfo);
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
    }

    private void readStationsFromS3()
    {
        S3Client s3Client = S3Client.create();

        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket("taub.citibike")
                .key("station_information.json")
                .build();

        InputStream in = s3Client.getObject(getObjectRequest);
        Reader reader = new InputStreamReader(in);
        stationInfo  = new Gson().fromJson(reader, Stations.class);
    }
}
