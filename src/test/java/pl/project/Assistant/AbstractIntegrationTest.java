package pl.project.Assistant;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base class for integration tests: runs the application against a PostgreSQL container,
 * so no local database or .env file is needed.
 * <p>
 * The container is a singleton started once for the whole test run instead of a {@code @Container}
 * field: JUnit would stop a {@code @Container} after each test class, while Spring keeps the
 * application context (and its connection pool) cached across classes.
 */
@Testcontainers
@SpringBootTest(properties = {
        // Test-only key, never used outside tests
        "jwt.secret=dGVzdC1vbmx5LWp3dC1zZWNyZXQta2V5LWZvci1pbnRlZ3JhdGlvbi10ZXN0cw=="
})
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17");

    static {
        POSTGRES.start();
    }
}
