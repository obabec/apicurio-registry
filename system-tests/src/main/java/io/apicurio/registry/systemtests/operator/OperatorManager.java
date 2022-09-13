package io.apicurio.registry.systemtests.operator;

import io.apicurio.registry.systemtests.framework.LoggerUtils;
import io.apicurio.registry.systemtests.operator.types.OperatorType;
import io.apicurio.registry.systemtests.platform.Kubernetes;
import io.apicurio.registry.systemtests.registryinfra.ResourceManager;
import io.apicurio.registry.systemtests.registryinfra.resources.NamespaceResourceType;
import io.apicurio.registry.systemtests.time.TimeoutBudget;
import io.fabric8.kubernetes.api.model.Namespace;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Stack;

public class OperatorManager {
    private static final Logger LOGGER = LoggerUtils.getLogger();
    private static OperatorManager instance;
    public static synchronized OperatorManager getInstance() {
        if (instance == null) {
            instance = new OperatorManager();
        }

        return instance;
    }

    public void installOperator(OperatorType operatorType) throws InterruptedException {
        installOperator(operatorType, true);
    }

    public void installOperator(OperatorType operatorType, boolean waitReady) throws InterruptedException {
        String kind = operatorType.getKind().toString();
        String name = operatorType.getDeploymentName();
        String namespace = operatorType.getNamespaceName();
        String operatorInfo = MessageFormat.format("{0} with name {1} in namespace {2}", kind, name, namespace);

        LOGGER.info("Installing operator {}...", operatorInfo);

        //operatorType.install();

        LOGGER.info("Operator {} installed.", operatorInfo);

        if (waitReady) {
            LOGGER.info("Waiting for operator {} to be ready...", operatorInfo);

            Assertions.assertTrue(
                    waitOperatorReady(operatorType),
                    MessageFormat.format("Timed out waiting for operator {0} to be ready.", operatorInfo)
            );

            if (operatorType.isReady()) {
                LOGGER.info("Operator {} is ready.", operatorInfo);
            }
        } else {
            LOGGER.info("Do not wait for operator {} to be ready.", operatorInfo);
        }
    }

    public void uninstallOperator(OperatorType operatorType) {
        uninstallOperator(operatorType, true);
    }

    public void uninstallOperator(OperatorType operatorType, boolean waitRemoved) {
        String kind = operatorType.getKind().toString();
        String name = operatorType.getDeploymentName();
        String namespace = operatorType.getNamespaceName();
        String operatorInfo = MessageFormat.format("{0} with name {1} in namespace {2}", kind, name, namespace);

        LOGGER.info("Uninstalling operator {}...", operatorInfo);

        operatorType.uninstall();

        if (waitRemoved) {
            LOGGER.info("Waiting for operator {} to be uninstalled...", operatorInfo);

            Assertions.assertTrue(
                    waitOperatorRemoved(operatorType),
                    MessageFormat.format("Timed out waiting for operator {0} to be uninstalled.", operatorInfo)
            );

            if (operatorType.doesNotExist()) {
                LOGGER.info("Operator {} uninstalled.", operatorInfo);
            }
        }  else {
            LOGGER.info("Do not wait for operator {} to be uninstalled.", operatorInfo);
        }
    }

    public boolean waitOperatorReady(OperatorType operatorType) {
        return waitOperatorReady(operatorType, TimeoutBudget.ofDuration(Duration.ofMinutes(15)));
    }

    public boolean waitOperatorReady(OperatorType operatorType, TimeoutBudget timeoutBudget) {
        while (!timeoutBudget.timeoutExpired()) {
            if (operatorType.isReady()) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        if (!operatorType.isReady()) {
            LOGGER.error(
                    "Operator {} with name {} in namespace {} failed readiness check.",
                    operatorType.getKind(), operatorType.getDeploymentName(), operatorType.getNamespaceName()
            );

            return false;
        }

        return true;
    }

    public boolean waitOperatorRemoved(OperatorType operatorType) {
        return waitOperatorRemoved(operatorType, TimeoutBudget.ofDuration(Duration.ofMinutes(15)));
    }

    public boolean waitOperatorRemoved(OperatorType operatorType, TimeoutBudget timeoutBudget) {
        while (!timeoutBudget.timeoutExpired()) {
            if (operatorType.doesNotExist()) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                return false;
            }
        }

        if (!operatorType.doesNotExist()) {
            LOGGER.error(
                    "Operator {} with name {} in namespace {} failed removal check.",
                    operatorType.getKind(), operatorType.getDeploymentName(), operatorType.getNamespaceName()
            );

            return false;
        }

        return true;
    }
}
