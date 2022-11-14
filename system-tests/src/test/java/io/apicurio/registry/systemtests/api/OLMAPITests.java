package io.apicurio.registry.systemtests.api;

import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryOLMOperatorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class OLMAPITests extends APITests {
    private boolean clusterWide;

    public boolean getClusterWide() {
        return clusterWide;
    }

    public void setClusterWide(boolean clusterWide) {
        this.clusterWide = clusterWide;
    }

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());

        ApicurioRegistryOLMOperatorType registryOLMOperator = new ApicurioRegistryOLMOperatorType(null, clusterWide);

        operatorManager.installOperator(registryOLMOperator);
    }
}
