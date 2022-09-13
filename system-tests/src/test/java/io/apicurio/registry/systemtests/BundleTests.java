package io.apicurio.registry.systemtests;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryBundleOperatorType;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.LoadKubernetesManifests;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag("bundle")
@LoadKubernetesManifests("/keycloak/keycloak.yaml")
@KubernetesTest
class BundleTests extends TestBase{
    private KubernetesClient client;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeEach: " + testContext.getDisplayName());

        ApicurioRegistryBundleOperatorType registryBundleOperator = new ApicurioRegistryBundleOperatorType();

        operatorManager.installOperator(registryBundleOperator);
    }

    @Test
    @LoadKubernetesManifests("")
    public void testRegistrySql(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.SQL, null, false, true);
    }

    @Test
    public void testRegistrySqlKeycloak(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.SQL, null, true, true);
    }

    /* TESTS - KafkaSQL */

    @Test
    public void testRegistryKafkasql(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false, true);
    }

    @Test
    public void testRegistryKafkasqlKeycloak(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true, true);
    }

    @Test
    public void testRegistryKafkasqlTLS(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false, true);
    }

    @Test
    public void testRegistryKafkasqlTLSKeycloak(ExtensionContext testContext) throws InterruptedException {
        runTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true, true);
    }


}
