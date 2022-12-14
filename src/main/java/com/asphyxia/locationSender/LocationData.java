package com.asphyxia.locationSender;

import kotlin.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationData implements Serializable, Comparable<LocationData> {
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
        if ((params.get(3)).equals("null")) {
            locationData.setIdentifier(null);
        } else {
            locationData.setIdentifier((String) params.get(3));
        }
        locationData.setTimestamp(((Double) params.get(4)).longValue());
        Pair<Object, Boolean> floorLabel = (Pair<Object, Boolean>) params.get(5);
        if (floorLabel.getSecond().equals(Boolean.FALSE)) {
            locationData.setFloorLabel(null);
        } else {
            locationData.setFloorLabel(((Double) ((Pair<Object, Boolean>) params.get(5)).getFirst()).intValue());
        }
        locationData.setHorAccuracy((Double) params.get(6));
        locationData.setVerAccuracy((Double) params.get(7));
        if (params.get(9).equals("UNKNOWN")) {
            locationData.setActivity(null);
        } else {
            locationData.setActivity((String) params.get(9));
        }
        return locationData;
    }

    @Override
    public int compareTo(@NotNull LocationData o) {
        return Comparators.TIME.compare(this, o);
    }

    public static class Comparators {
        public static Comparator<LocationData> TIME = new Comparator<LocationData>() {
            @Override
            public int compare(LocationData o1, LocationData o2) {
                if ((o1.timestamp - o2.timestamp) > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }

        };
    }
}
