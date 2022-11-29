package io.apicurio.registry.systemtests.deploy;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-namespace")
public class OLMNamespacedDeployTests extends OLMDeployTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(false);
    }
}
