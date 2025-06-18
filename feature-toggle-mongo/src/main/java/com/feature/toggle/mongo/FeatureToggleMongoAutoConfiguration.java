package com.feature.toggle.mongo;

import com.feature.toggle.core.FeatureToggleAspect;
import com.feature.toggle.core.FeatureToggleService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class FeatureToggleMongoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeatureToggleService.class)
    public FeatureToggleService featureToggleService(final MongoTemplate mongoTemplate) {
        return new MongoFeatureToggleService(mongoTemplate);
    }

    @Bean
    public FeatureToggleAspect featureToggleAspect(final FeatureToggleService featureToggleService) {
        return new FeatureToggleAspect(featureToggleService);
    }
}
