package com.example.housespot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    String district;
    String city;
    float latitude;
    float longitude;
    float landSize;
    float buildingSize;
    int floors;
    int bedrooms;
    int bathrooms;
    int carportGarage;
}
