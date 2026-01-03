package backend.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Centralizes creation of the JPA EntityManagerFactory so we can override
 * connection settings without editing persistence.xml each time.
 */
public final class PersistenceFactory {
    private static final Logger LOGGER = Logger.getLogger(PersistenceFactory.class.getName());
    private static final String UNIT_NAME = "concordiaPU";

    private PersistenceFactory() {
    }

    public static EntityManagerFactory create() {
        Map<String, Object> overrides = new HashMap<>();
        putIfPresent(overrides, "jakarta.persistence.jdbc.url",
            firstNonBlank(
                System.getProperty("concordia.db.url"),
                System.getenv("CONCORDIA_DB_URL")
            )
        );
        putIfPresent(overrides, "jakarta.persistence.jdbc.user",
            firstNonBlank(
                System.getProperty("concordia.db.user"),
                System.getProperty("concordia.db.username"),
                System.getenv("CONCORDIA_DB_USER"),
                System.getenv("CONCORDIA_DB_USERNAME")
            )
        );
        putIfPresent(overrides, "jakarta.persistence.jdbc.password",
            firstNonBlank(
                System.getProperty("concordia.db.password"),
                System.getenv("CONCORDIA_DB_PASSWORD")
            )
        );

        if (!overrides.isEmpty()) {
            LOGGER.info(() -> "Creating persistence unit '" + UNIT_NAME + "' with overrides: " + summarize(overrides));
        }
        return Persistence.createEntityManagerFactory(UNIT_NAME, overrides);
    }

    private static void putIfPresent(Map<String, Object> overrides, String key, String value) {
        if (value != null && !value.isBlank()) {
            overrides.put(key, value);
        }
    }

    private static String firstNonBlank(String... candidates) {
        if (candidates == null) {
            return null;
        }
        for (String candidate : candidates) {
            if (candidate != null && !candidate.isBlank()) {
                return candidate;
            }
        }
        return null;
    }

    private static String summarize(Map<String, Object> overrides) {
        Map<String, String> safe = new HashMap<>();
        overrides.forEach((key, value) -> safe.put(key, maskIfPassword(key, value)));
        return safe.toString();
    }

    private static String maskIfPassword(String key, Object value) {
        if ("jakarta.persistence.jdbc.password".equals(key)) {
            return "<hidden>";
        }
        return String.valueOf(value);
    }
}
