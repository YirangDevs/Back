package com.api.yirang.common.configure;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMongoRepositories(
        basePackages = {
            "com.api.yirang.seniors.repository.persistence.mongo"
        }
)
@PropertySource("classpath:properties/application-db.properties")
public class MongoDataBaseConfig extends AbstractMongoClientConfiguration {

    private String DB_PROTOCOL = "mongodb://";

    @Value("${spring.mongodb.datasource.url}")
    private String URL;

    @Value("${spring.mongodb.datasource.port}")
    private String PORT;

    @Value("${spring.mongodb.datasource.username}")
    private String USER_NAME;

    @Value("${spring.mongodb.datasource.password}")
    private String PASSWORD;

    @Value("${spring.mongodb.datasource.database}")
    private String DATABASE;

    @Value("#{${spring.mongodb.datasource.models}}")
    private List<String> LIST_OF_MODELS;

    @Override
    public MongoClient mongoClient(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DB_PROTOCOL);
        stringBuilder.append(USER_NAME);
        stringBuilder.append(":");
        stringBuilder.append(PASSWORD);
        stringBuilder.append("@");
        stringBuilder.append(URL);
        stringBuilder.append(":");
        stringBuilder.append(PORT);
        stringBuilder.append("/");
        stringBuilder.append(DATABASE);

        System.out.println("url: " + stringBuilder.toString());
        ConnectionString connectionString = new ConnectionString(stringBuilder.toString());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                                                                     .applyConnectionString(connectionString)
                                                                     .build();
        return MongoClients.create(mongoClientSettings);
    }

    @Override
    protected String getDatabaseName() {
        return this.DATABASE;
    }

    @Override
    protected Collection<String> getMappingBasePackages(){
        return this.LIST_OF_MODELS;
    }
}
