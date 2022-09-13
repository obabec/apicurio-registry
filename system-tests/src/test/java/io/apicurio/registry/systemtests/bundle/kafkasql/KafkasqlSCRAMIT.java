package io.apicurio.registry.systemtests.bundle.kafkasql;

import io.apicurio.registry.operator.api.model.ApicurioRegistry;
import io.apicurio.registry.systemtests.APITests;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.framework.DatabaseUtils;
import io.apicurio.registry.systemtests.registryinfra.resources.ApicurioRegistryResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.DeploymentResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaResourceType;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.LoadKubernetesManifests;
import io.fabric8.junit.jupiter.api.RequireK8sVersionAtLeast;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.client.OpenShiftClient;
import io.strimzi.api.kafka.model.Kafka;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@KubernetesTest
@LoadKubernetesManifests({/*"/keycloak/keycloak.yaml",*/ "/strimzi/subscription.yaml" /*,"/kafka/scram.yaml", "/apicurio/bundle.yaml"*/})
public class KafkasqlSCRAMIT extends TestBase {
    KubernetesClient client;

    @Test
    void testRegistryKafkasqlSCRAM(ExtensionContext testContext) {
        /*
            Vsechno vcetne operatoru bude nahozene pomoci anotaci.
            Jedine co zbude je apicurio samotne, protoze tam je potreba specifikovat url kafky
            Tady nasledne uz probehne jenom samotny API test

         */

        /*ApicurioRegistry registry = ApicurioRegistryResourceType.getDefaultKafkasql("registry", client.getNamespace());
        client.resource(registry).createOrReplace();
        client.resource(registry).waitUntilReady(5, TimeUnit.MINUTES);
        APITests.run(registry, "registry-admin", "changeme", false);*/
        assertTrue(false);
    }

    @Test
    public void testRegistryKafkasqlSCRAMKeycloak(ExtensionContext testContext) {
        //runTest(testContext, PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true, true);
    }
}
