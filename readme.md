# 🚦 Feature Toggle Framework

A modular, annotation-driven feature toggle system for Java applications. Supports MongoDB and PostgreSQL backends to dynamically enable or disable features at runtime.

---

## 📌 Overview

This project is divided into three modules:

1. **feature-toggle-core** — Core logic, annotations, and AOP-based runtime feature validation.
2. **feature-toggle-mongo** — MongoDB-based feature flag storage.
3. **feature-toggle-postgres** — PostgreSQL-based feature flag storage.

---

## ✨ Features

- ✅ **Annotation-based** toggles with `@FeatureToggle`
- 🗄 **Flexible storage** support (MongoDB, PostgreSQL)
- 🔌 **Spring Boot integration** via auto-configuration
- ⚙ **Automatic schema initialization** (collections/tables)
- 🧩 **Extensible**: Plug your own `FeatureToggleService` implementation

---

## 🧱 Modules

### 🔹 feature-toggle-core

Core library defining:

- `@FeatureToggle`: Annotation to guard methods behind a feature flag
- `FeatureToggleAspect`: Aspect that intercepts annotated methods
- `FeatureToggleService`: Interface for feature state lookup

### 🔹 feature-toggle-mongo

MongoDB implementation:

- Auto-configures `MongoFeatureToggleService`
- Stores flags in `feature_flags` collection
- Initializes collection on startup (if missing)
- Queries documents by `name` and `enabled: true`

### 🔹 feature-toggle-postgres

PostgreSQL implementation:

- Auto-configures `JdbcFeatureToggleService`
- Uses `feature_flags` table
- Creates table on startup if not present
- Checks feature by `name` and `enabled = true`

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21+
- Spring Boot 3.x
- MongoDB or PostgreSQL instance

### 📦 Installation (Maven)

```xml
<!--Repository -->
<repositories>
    <repository>
        <id>github-feature-toggle-mvn-repo</id>
        <url>https://raw.github.com/Zaytsev-Dmitry/java-feature-toggle/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

```xml
<!-- Core module -->
<dependency>
    <groupId>com.example</groupId>
    <artifactId>feature-toggle-mongo</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- MongoDB module -->
<dependency>
  <groupId>com.example</groupId>
  <artifactId>feature-toggle-mongo</artifactId>
  <version>1.0.0</version>
</dependency>

<!-- PostgreSQL module-->
<dependency>
  <groupId>com.example</groupId>
  <artifactId>feature-toggle-postgres</artifactId>
  <version>1.0.0</version>
</dependency>
```

---

### ⚙ Configuration

#### MongoDB (`application.properties`)
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/your-database
```

#### PostgreSQL (`application.properties`)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
```

---

## 🧪 Usage Example

### 1. Annotate a method
```java
import com.example.featuretoggle.FeatureToggle;

@Service
public class MyService {

    @FeatureToggle("MY_FEATURE")
    public void performAction() {
        System.out.println("Feature is enabled!");
    }
}
```

### 2. Add feature flags

#### For MongoDB
```json
{
  "name": "MY_FEATURE",
  "enabled": true
}
```

#### For PostgreSQL
```sql
INSERT INTO feature_flags (name, enabled) VALUES ('MY_FEATURE', true);
```

If the feature is disabled (`enabled = false`) or not found, an `IllegalStateException` is thrown by the `FeatureToggleAspect`.

---


## 🧩 Extending the Framework

### Custom storage backend

1. Implement `FeatureToggleService`:

```java
public class CustomFeatureToggleService implements FeatureToggleService {
    @Override
    public boolean isFeatureEnabled(String name) {
        // Custom logic
        return true;
    }
}
```

2. Register as a Spring bean:

```java
@Configuration
public class CustomFeatureToggleConfig {
    @Bean
    public FeatureToggleService featureToggleService() {
        return new CustomFeatureToggleService();
    }
}
```

The auto-configuration will skip default implementations if a custom bean is present.

---

## 🤝 Contributing

1. Fork the repository
2. Create a branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add new feature'`
4. Push the branch: `git push origin feature/your-feature`
5. Open a pull request

---