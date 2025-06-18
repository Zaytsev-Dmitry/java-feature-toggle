package com.feature.toggle.postgres;

import com.feature.toggle.core.FeatureToggleAspect;
import com.feature.toggle.core.FeatureToggleService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FeatureTogglePostgresAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeatureToggleService.class)
    public FeatureToggleService featureToggleService(final DataSource dataSource) {
        return new JdbcFeatureToggleService(dataSource);
    }

    @Bean
    public FeatureToggleAspect featureToggleAspect(final FeatureToggleService featureToggleService) {
        return new FeatureToggleAspect(featureToggleService);
    }
}
