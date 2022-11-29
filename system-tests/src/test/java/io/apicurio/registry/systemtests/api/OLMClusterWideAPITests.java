package io.apicurio.registry.systemtests.api;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import org.junit.jupiter.api.Tag;

@Tag("olm")
@Tag("olm-clusterwide")
@Tag("olm-clusterwide-api")
public class OLMClusterWideAPITests extends OLMAPITests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();

        setClusterWide(true);
    }
}
