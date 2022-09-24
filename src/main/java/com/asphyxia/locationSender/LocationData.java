package com.asphyxia.locationSender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationData implements Serializable {
    private double latitude;
    private double longitude;
    private double altitude;
    private String identifier;
    private Long timestamp;
    private Integer floorLabel;
    private double horAccuracy;
    private double verAccuracy;
    private final double confidence = 0.6827;
    private String activity;

    public static LocationData getLocationData (List<Object> params) {
        LocationData locationData = new LocationData();
        locationData.setLatitude((Double) params.get(0));
        locationData.setLongitude((Double) params.get(1));
        locationData.setAltitude((Double) params.get(2));
        locationData.setIdentifier((String) params.get(3));
        locationData.setTimestamp(((Double) params.get(4)).longValue());
        locationData.setFloorLabel(((Double) params.get(5)).intValue());
        locationData.setHorAccuracy((Double) params.get(6));
        locationData.setVerAccuracy((Double) params.get(7));
        locationData.setActivity((String) params.get(9));
        return locationData;
    }
}
