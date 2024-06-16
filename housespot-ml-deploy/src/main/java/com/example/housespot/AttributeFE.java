package com.example.housespot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeFE {
    String districtAndCity;
    float latitude;
    float longitude;
    float landSize;
    float buildingSize;
    int floors;
    int bedrooms;
    int bathrooms;
    int carportGarage;
}
