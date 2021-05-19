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

    @Builder
    public MapImg(String mapName, String mapImgUrl) {
        this.mapName = mapName;
        this.mapImgUrl = mapImgUrl;
    }
}
