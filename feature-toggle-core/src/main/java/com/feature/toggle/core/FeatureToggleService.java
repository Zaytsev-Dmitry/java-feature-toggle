package com.feature.toggle.core;

public interface FeatureToggleService {
    boolean isFeatureEnabled(final String name);
}
