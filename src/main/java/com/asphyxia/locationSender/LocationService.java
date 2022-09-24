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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@Service
public class LocationService {

    private HashMap<Integer, String> routes = new HashMap<>();
    private String publishKey = "pub-c-534260b7-21ea-4b33-9def-7599f31ebe1f";
    private String subscribeKey = "sub-c-bd5e7bdb-4007-4ebd-b9c8-1f344613d945";

    private String excelPath = "C:\\Users\\user\\IdeaProjects\\locationSender\\src\\main\\resources\\routes\\hacknu-dev-data.xlsx";
    private PubNub pubNub;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws PubNubException {
        //initialize pubnub
        UserId userId = new UserId("server");
        PNConfiguration configuration = new PNConfiguration(userId);
        configuration.setPublishKey(publishKey);
        configuration.setSubscribeKey(subscribeKey);
        pubNub = new PubNub(configuration);

        //initialize hashmap
        routes.put(1, "dev1");
        routes.put(2, "dev2");
        routes.put(3, "dev3");
        routes.put(4, "dev4");
        routes.put(5, "dev5");
        routes.put(6, "dev6");
        routes.put(7, "dev7");
        routes.put(8, "dev8");
        routes.put(9, "dev9");
        routes.put(10, "dev10");
    }


    private void getRoute(int id) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
        XSSFSheet sheet = workbook.getSheet(routes.get(id));
        System.out.println(routes.get(id));
        System.out.println(sheet.getLastRowNum());
    }
    public void postMessage(int id) throws IOException {
        getRoute(id);

        LocationData locationdata = new LocationData();
        locationdata.setActivity("Cycling");
        locationdata.setAltitude(17.04397242);
        locationdata.setLatitude(51.53371153);
        locationdata.setIdentifier("Alice");
        locationdata.setLongitude(-0.126159919);
        locationdata.setFloorLabel(4);
        locationdata.setTimestamp(267813L);
        locationdata.setHorAccuracy(18.2302031);
        locationdata.setVerAccuracy(4.39101);

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
