package com.api.yirang.img.repository.mongo;

import com.api.yirang.img.model.mongo.MapImg;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MapImgRepository extends MongoRepository<MapImg, String> {

    Optional<MapImg> findMapImgByMapName(String mapName);

}
