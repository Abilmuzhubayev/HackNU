package com.asphyxia.locationSender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
}
