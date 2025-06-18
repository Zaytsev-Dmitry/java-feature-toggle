package com.feature.toggle.postgres;

import com.feature.toggle.core.FeatureToggleService;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcFeatureToggleService implements FeatureToggleService {
    public static final String CREATE_FEATURE_TABLE = """
            CREATE TABLE feature_flags (
                id BIGSERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL UNIQUE,
                enabled BOOLEAN DEFAULT FALSE NOT NULL
            )
            """;
    public static final String SELECT_FEATURE_NAME = """
                SELECT name
                FROM feature_flags
                WHERE upper(name) = ? AND enabled = true
                LIMIT 1
            """;
    private final DataSource dataSource;

    public JdbcFeatureToggleService(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.initialize();
    }

    public void initialize() {
        try (Connection conn = dataSource.getConnection()) {
            if (!tableExists(conn, "feature_flags")) {
                createFeatureFlagsTable(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize feature_flags table", e);
        }
    }

    private boolean tableExists(final Connection conn, final String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet tables = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return tables.next();
        }
    }

    private void createFeatureFlagsTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(CREATE_FEATURE_TABLE);
        }
    }

    @Override
    public boolean isFeatureEnabled(final String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_FEATURE_NAME)) {

            stmt.setString(1, name.toUpperCase());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if feature is enabled: " + name, e);
        }
    }
}
