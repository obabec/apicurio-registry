package io.apicurio.registry.systemtests.deploy;

import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class DeployTests extends TestBase {
    /* TESTS - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAM() throws InterruptedException {
        deployTestRegistry(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloak() throws InterruptedException {
        deployTestRegistry(PersistenceKind.SQL, null, true);
    }

    /* TESTS - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAM() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloak() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAM() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloak() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAM() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloak() throws InterruptedException {
        deployTestRegistry(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
}
