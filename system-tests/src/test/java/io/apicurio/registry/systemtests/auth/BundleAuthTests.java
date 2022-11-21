package io.apicurio.registry.systemtests.auth;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.types.ApicurioRegistryBundleOperatorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtensionContext;

@Tag("bundle")
@Tag("bundle-auth")
@Disabled
public class BundleAuthTests extends AuthTests {
    @Override
    public void setupTestClass() {
        LOGGER = LoggerUtils.getLogger();
    }

    @BeforeEach
    public void testBeforeEach(ExtensionContext testContext) throws InterruptedException {
        LOGGER.info("BeforeEach: " + testContext.getTestMethod().get().getName());

        ApicurioRegistryBundleOperatorType registryBundleOperator = new ApicurioRegistryBundleOperatorType(
                "/home/jenkins/workspace/operator/apicurio-operator/install-examples/apicurio-registry-install-examples/install/install.yaml"
        );

        operatorManager.installOperator(registryBundleOperator);
    }
}
