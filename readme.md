
# 🔀 Feature Toggle for Spring Boot

**Feature Toggle** is a lightweight, extensible, and production-ready library for managing feature flags in Spring Boot applications.

With the `@FeatureToggle` annotation, you can enable or disable specific methods or parts of your business logic without changing code or recompiling.

---

## ✨ Features

- ✅ Easy usage with the `@FeatureToggle` annotation
- ✅ Automatic creation of the `feature_flags` table on first startup
- ✅ Extensible implementation of `FeatureToggleService`
- ✅ Safe integration with any database via `DataSource`
- ✅ AOP aspect implementation — disabled features do not execute at all

---

## 📦 Local Setup

Run the install command:

```bash
./mvnw clean install
```

Add the dependency in your main project’s `pom.xml`:

```xml
<dependency>
  <groupId>com.feature.toggle</groupId>
  <artifactId>feature-toggle</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

---

## 📦 Remote Setup
Add to your `pom.xml`:

```xml
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

<dependency>
<groupId>com.feature.toggle</groupId>
<artifactId>feature-toggle</artifactId>
<version>0.0.1-SNAPSHOT</version>
</dependency>
```

---

## How It Works

### 1. Annotation

```java
@FeatureToggle("NEW_DASHBOARD")
public void showNewDashboard() {
    // This code runs only if the feature is enabled
}
```

### 2. Aspect

```java
@Aspect
public class FeatureToggleAspect {

    @Around("@annotation(featureToggle)")
    public Object around(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        if (featureToggleService.isFeatureEnabled(featureToggle.value())) {
            return joinPoint.proceed();
        }
        throw new IllegalStateException("Feature " + featureToggle.value() + " is disabled.");
    }
}
```

---

## ⚙️ Configuration

The library provides auto-configuration:

```java
@Configuration
public class FeatureToggleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeatureToggleService.class)
    public FeatureToggleService featureToggleService(DataSource dataSource) {
        return new JdbcFeatureToggleService(dataSource);
    }

    @Bean
    public FeatureToggleAspect featureToggleAspect(FeatureToggleService featureToggleService) {
        return new FeatureToggleAspect(featureToggleService);
    }
}
```

If you want to use your own implementation:

```java
@Bean
public FeatureToggleService myCustomToggleService() {
    return new MyCustomToggleService();
}
```

---

## 🗃️ Database Table Structure

On first run, the following table is created automatically:

```sql
CREATE TABLE feature_flags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN DEFAULT FALSE NOT NULL
);
```

---

## 🧪 Usage Example

```java
@RestController
@RequiredArgsConstructor
public class FeatureController {
    private final SomeService someService;

    @FeatureToggle("EXPERIMENTAL_FEATURE")
    @GetMapping("/feature")
    public ResponseEntity<String> getFeature() {
        return ResponseEntity.ok(someService.getData());
    }
}
```

---

## 📌 Important Notes

- Methods annotated with `@FeatureToggle` **must** be called through a Spring proxy (i.e., from another Spring-managed bean) for the aspect to work correctly.
- Feature names are matched case-insensitively.
- By default, if a feature is disabled, the method **will not execute**, and an `IllegalStateException` is thrown.  
