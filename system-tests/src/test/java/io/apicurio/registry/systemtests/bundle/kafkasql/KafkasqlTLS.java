package io.apicurio.registry.systemtests.bundle.kafkasql;

import io.fabric8.junit.jupiter.api.KubernetesTest;
import io.fabric8.junit.jupiter.api.LoadKubernetesManifests;

@KubernetesTest
@LoadKubernetesManifests("/kafka/tls.yaml")
public class KafkasqlTLS {
}
