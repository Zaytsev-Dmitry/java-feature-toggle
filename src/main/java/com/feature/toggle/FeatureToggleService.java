package com.feature.toggle;

public interface FeatureToggleService {
    boolean isFeatureEnabled(final String name);
}
