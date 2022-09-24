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
import kotlin.Pair;
import lombok.Data;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Data
@Service
public class LocationService {

    private HashMap<Integer, String> routes = new HashMap<>();
    private String publishKey = "pub-c-534260b7-21ea-4b33-9def-7599f31ebe1f";
    private String subscribeKey = "sub-c-bd5e7bdb-4007-4ebd-b9c8-1f344613d945";

    private String excelPath = "C:\\Users\\user\\IdeaProjects\\locationSender\\src\\main\\resources\\routes\\dev-data.xlsx";
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


    private List<LocationData> getRoute(int id) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
        XSSFSheet sheet = workbook.getSheet(routes.get(id));
        int rowNum = sheet.getPhysicalNumberOfRows();
        List<LocationData> locationDataList = new ArrayList<>();
        for (int i = 1; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            List<Object> params = new ArrayList<>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                if (row.getCell(j).getCellType() == CellType.STRING) {
                    String curValue = row.getCell(j).getStringCellValue();
                    if (j == 5) {
                        params.add(new Pair<Object, Boolean>(curValue, Boolean.FALSE));
                    } else {
                        params.add(row.getCell(j).getStringCellValue());
                    }
                } else if (row.getCell(j).getCellType() == CellType.NUMERIC) {
                    Double curValue = row.getCell(j).getNumericCellValue();
                    if (j == 5) {
                        params.add(new Pair<Object, Boolean>(curValue, Boolean.TRUE));
                    } else {
                        params.add(row.getCell(j).getNumericCellValue());
                    }
                }
            }
            LocationData locationData = LocationData.getLocationData(params);
            locationDataList.add(locationData);
        }

        locationDataList.sort(LocationData.Comparators.TIME);

        return locationDataList;
    }
    public void postMessage(int id) throws IOException, InterruptedException {
        List<LocationData> locationDataList = getRoute(id);

        for (int i = 0; i < locationDataList.size(); i++) {
            if (i > 0) {
                long difference = locationDataList.get(i).getTimestamp() - locationDataList.get(i - 1).getTimestamp();
                Thread.sleep(difference);
            }
            System.out.println(locationDataList.get(i));
            String json = objectMapper.writeValueAsString(locationDataList.get(i));
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
}
