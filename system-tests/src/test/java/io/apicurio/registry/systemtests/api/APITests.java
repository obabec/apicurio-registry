package io.apicurio.registry.systemtests.api;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.api.features.CreateArtifact;
import io.apicurio.registry.systemtests.api.features.CreateReadUpdateDelete;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class APITests extends TestBase {
    /* TEST RUNNERS */

    protected void runCreateReadUpdateDeleteTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    true
            );
        } else {
            CreateReadUpdateDelete.testCreateReadUpdateDelete(registry, null, null, false);
        }
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runCreateArtifactTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            CreateArtifact.testCreateArtifact(registry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD, true);
        } else {
            CreateArtifact.testCreateArtifact(registry, null, null, false);
        }
    }

    /* TESTS - PostgreSQL */

    @Test
    public void testRegistrySqlNoIAMCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("live-test")
    public void testRegistrySqlKeycloakCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.SQL, null, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    public void testRegistrySqlNoIAMCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.SQL, null, false);
    }

    @Test
    public void testRegistrySqlKeycloakCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.SQL, null, true);
    }

    /* TESTS - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("live-test")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateReadUpdateDelete() throws InterruptedException {
        runCreateReadUpdateDeleteTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakCreateArtifact() throws InterruptedException {
        runCreateArtifactTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
}
