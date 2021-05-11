package com.api.yirang.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddressDto {

    @JsonProperty("common")
    Map<String, Object> header;

    @JsonProperty("juso")
    List<Map<String, Object>> addresses;

}
