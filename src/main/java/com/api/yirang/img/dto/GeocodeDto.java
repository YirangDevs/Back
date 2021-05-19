package com.api.yirang.img.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GeocodeDto {

    @JsonProperty("status")
    String status;

    @JsonProperty("meta")
    Map<String, Object> meta;

    @JsonProperty("addresses")
    List<Map<String, Object>> addresses;

    @JsonProperty("errorMessage")
    String errorMessage;

}
