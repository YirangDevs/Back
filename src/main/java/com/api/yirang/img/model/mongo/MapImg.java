package com.api.yirang.img.model.mongo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "map_img")
@Getter
@ToString
@NoArgsConstructor
public class MapImg {

    @Id
    private String mapName;

    private String mapImgUrl;

    private String xCode;
    private String yCode;

    @Builder
    public MapImg(String mapName, String mapImgUrl, String xCode, String yCode) {
        this.mapName = mapName;
        this.mapImgUrl = mapImgUrl;
        this.xCode = xCode;
        this.yCode = yCode;
    }
}
