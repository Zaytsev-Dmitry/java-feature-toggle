package com.feature.toggle.mongo;

import com.feature.toggle.core.FeatureToggleService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoFeatureToggleService implements FeatureToggleService {

    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION_NAME = "feature_flags";

    public MongoFeatureToggleService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void initialize() {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }
    }

    @Override
    public boolean isFeatureEnabled(final String name) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(COLLECTION_NAME);
        Document document = collection.find(Filters.and(
                Filters.eq("name", name.toUpperCase()),
                Filters.eq("enabled", true)
        )).first();
        return document != null;
    }
}
