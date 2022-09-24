package com.asphyxia.locationSender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Data
@Service
public class LocationService {

    private ArrayList<LocationData> locationData = new ArrayList<>();
    private String publishKey = "pub-c-534260b7-21ea-4b33-9def-7599f31ebe1f";
    private String subscribeKey = "sub-c-bd5e7bdb-4007-4ebd-b9c8-1f344613d945";
    private PubNub pubNub;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws PubNubException {
        UserId userId = new UserId("server");
        PNConfiguration configuration = new PNConfiguration(userId);
        configuration.setPublishKey(publishKey);
        configuration.setSubscribeKey(subscribeKey);
        pubNub = new PubNub(configuration);
    }
    public void postMessage(int id) throws JsonProcessingException {

        LocationData locationdata = new LocationData();
        locationdata.setActivity("Aaaa");
        locationdata.setAltitude(1.2);
        locationdata.setLatitude(2.2);
        locationdata.setIdentifier("Aaaa");
        locationdata.setLongitude(1.1);
        locationdata.setFloorLabel(4);
        locationdata.setTimestamp(123L);
        locationdata.setHorAccuracy(1.1);
        locationdata.setVerAccuracy(2.2);

        String json = objectMapper.writeValueAsString(locationdata);

        JSONObject jsonObject = new JSONObject(json);

        pubNub.publish()
                .channel("location_channel")
                .message(jsonObject)
                .async((result, status) -> {
                    if (status.isError()) {
                        System.out.println("status code: " + status.getStatusCode());
                    }
                    else System.out.println("timetoken: " + result.getTimetoken());
                });

    }
}
