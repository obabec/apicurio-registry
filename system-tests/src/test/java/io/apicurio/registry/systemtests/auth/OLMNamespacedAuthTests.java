package io.apicurio.registry.systemtests.auth;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Disabled;

@Disabled
public class OLMNamespacedAuthTests extends OLMAuthTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(false);
    }
}