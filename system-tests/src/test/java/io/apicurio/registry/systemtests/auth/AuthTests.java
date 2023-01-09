package io.apicurio.registry.systemtests.auth;

import io.apicur.registry.v1.ApicurioRegistry;
import io.apicurio.registry.systemtests.TestBase;
import io.apicurio.registry.systemtests.auth.features.AnonymousReadAccess;
import io.apicurio.registry.systemtests.auth.features.ArtifactGroupOwnerOnlyAuthorization;
import io.apicurio.registry.systemtests.auth.features.ArtifactOwnerOnlyAuthorization;
import io.apicurio.registry.systemtests.auth.features.AuthenticatedReads;
import io.apicurio.registry.systemtests.auth.features.BasicAuthentication;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationAdminOverrideClaim;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationAdminOverrideRole;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationApplication;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationRoleNames;
import io.apicurio.registry.systemtests.auth.features.RoleBasedAuthorizationToken;
import io.apicurio.registry.systemtests.framework.Constants;
import io.apicurio.registry.systemtests.registryinfra.resources.KafkaKind;
import io.apicurio.registry.systemtests.registryinfra.resources.PersistenceKind;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("auth")
public abstract class AuthTests extends TestBase {
    /* TEST RUNNERS */

    protected void runAnonymousReadAccessTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            boolean useKeycloak
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, useKeycloak);

        if (useKeycloak) {
            AnonymousReadAccess.testAnonymousReadAccess(
                    registry,
                    Constants.SSO_ADMIN_USER,
                    Constants.SSO_USER_PASSWORD,
                    true
            );
        } else {
            AnonymousReadAccess.testAnonymousReadAccess(registry, null, null, false);
        }
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runBasicAuthenticationTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        BasicAuthentication.testBasicAuthentication(registry, Constants.SSO_ADMIN_USER, Constants.SSO_USER_PASSWORD);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runAuthenticatedReadsTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        AuthenticatedReads.testAuthenticatedReads(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runArtifactOwnerOnlyAuthorizationTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        ArtifactOwnerOnlyAuthorization.testArtifactOwnerOnlyAuthorization(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runArtifactGroupOwnerOnlyAuthorizationTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        ArtifactGroupOwnerOnlyAuthorization.testArtifactGroupOwnerOnlyAuthorization(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationTokenTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationToken.testRoleBasedAuthorizationToken(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationApplicationTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationApplication.testRoleBasedAuthorizationApplication(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationRoleNamesTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationRoleNames.testRoleBasedAuthorizationRoleNames(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationAdminOverrideRoleTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind
    ) throws InterruptedException {
        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationAdminOverrideRole.testRoleBasedAuthorizationAdminOverrideRole(registry);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    protected void runRoleBasedAuthorizationAdminOverrideClaimTest(
            PersistenceKind persistenceKind,
            KafkaKind kafkaKind,
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) throws InterruptedException {
        LOGGER.info(
                "TEST PARAMETERS: claim={}, claimValue={}, adminSuffix={}, isAdminAllowed={}",
                claim, claimValue, adminSuffix, isAdminAllowed
        );

        ApicurioRegistry registry = deployTestRegistry(persistenceKind, kafkaKind, true);

        RoleBasedAuthorizationAdminOverrideClaim.testRoleBasedAuthorizationAdminOverrideClaim(
                registry,
                claim,
                claimValue,
                adminSuffix,
                isAdminAllowed
        );
    }

    /* TESTS - PostgreSQL */

    @Test
    @Tag("sql")
    public void testRegistrySqlNoIAMAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.SQL, null, false);
    }

    @Test
    @Tag("interop")
    @Tag("sql")
    public void testRegistrySqlKeycloakAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.SQL, null, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("interop")
    @Tag("sql")
    public void testRegistrySqlKeycloakBasicAuthentication() throws InterruptedException {
        runBasicAuthenticationTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakAuthenticatedReads() throws InterruptedException {
        runAuthenticatedReadsTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakArtifactOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactOwnerOnlyAuthorizationTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakArtifactGroupOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactGroupOwnerOnlyAuthorizationTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakRoleBasedAuthorizationToken() throws InterruptedException {
        runRoleBasedAuthorizationTokenTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakRoleBasedAuthorizationApplication() throws InterruptedException {
        runRoleBasedAuthorizationApplicationTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakRoleBasedAuthorizationRoleNames() throws InterruptedException {
        runRoleBasedAuthorizationRoleNamesTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("sql")
    public void testRegistrySqlKeycloakRoleBasedAuthorizationAdminOverrideRole(
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideRoleTest(PersistenceKind.SQL, null);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @ParameterizedTest
    @CsvFileSource(resources = "/adminOverrideClaimData.csv", numLinesToSkip = 1)
    @Tag("sql")
    public void testRegistrySqlKeycloakRoleBasedAuthorizationAdminOverrideClaim(
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideClaimTest(
                PersistenceKind.SQL,
                null,
                claim,
                claimValue,
                adminSuffix,
                isAdminAllowed
        );
    }

    /* TESTS - KafkaSQL */

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthNoIAMAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSNoIAMAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, false);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS, true);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMNoIAMAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, false);
    }

    @Test
    @Tag("interop")
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakAnonymousReadAccess() throws InterruptedException {
        runAnonymousReadAccessTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM, true);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakBasicAuthentication() throws InterruptedException {
        runBasicAuthenticationTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("interop")
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakBasicAuthentication() throws InterruptedException {
        runBasicAuthenticationTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakBasicAuthentication() throws InterruptedException {
        runBasicAuthenticationTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakAuthenticatedReads() throws InterruptedException {
        runAuthenticatedReadsTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakAuthenticatedReads() throws InterruptedException {
        runAuthenticatedReadsTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakAuthenticatedReads() throws InterruptedException {
        runAuthenticatedReadsTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakArtifactOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakArtifactOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakArtifactOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakArtifactGroupOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactGroupOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakArtifactGroupOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactGroupOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakArtifactGroupOwnerOnlyAuthorization() throws InterruptedException {
        runArtifactGroupOwnerOnlyAuthorizationTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationToken() throws InterruptedException {
        runRoleBasedAuthorizationTokenTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationToken() throws InterruptedException {
        runRoleBasedAuthorizationTokenTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationToken() throws InterruptedException {
        runRoleBasedAuthorizationTokenTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationApplication() throws InterruptedException {
        runRoleBasedAuthorizationApplicationTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationApplication() throws InterruptedException {
        runRoleBasedAuthorizationApplicationTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationApplication() throws InterruptedException {
        runRoleBasedAuthorizationApplicationTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationRoleNames() throws InterruptedException {
        runRoleBasedAuthorizationRoleNamesTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationRoleNames() throws InterruptedException {
        runRoleBasedAuthorizationRoleNamesTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationRoleNames() throws InterruptedException {
        runRoleBasedAuthorizationRoleNamesTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationAdminOverrideRole(
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideRoleTest(PersistenceKind.KAFKA_SQL, KafkaKind.NO_AUTH);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationAdminOverrideRole(
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideRoleTest(PersistenceKind.KAFKA_SQL, KafkaKind.TLS);
    }

    @Test
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationAdminOverrideRole(
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideRoleTest(PersistenceKind.KAFKA_SQL, KafkaKind.SCRAM);
    }
    /* -------------------------------------------------------------------------------------------------------------- */
    @ParameterizedTest
    @CsvFileSource(resources = "/adminOverrideClaimData.csv", numLinesToSkip = 1)
    @Tag("kafkasql")
    public void testRegistryKafkasqlNoAuthKeycloakRoleBasedAuthorizationAdminOverrideClaim(
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideClaimTest(
                PersistenceKind.KAFKA_SQL,
                KafkaKind.NO_AUTH,
                claim,
                claimValue,
                adminSuffix,
                isAdminAllowed
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/adminOverrideClaimData.csv", numLinesToSkip = 1)
    @Tag("kafkasql")
    public void testRegistryKafkasqlTLSKeycloakRoleBasedAuthorizationAdminOverrideClaim(
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideClaimTest(
                PersistenceKind.KAFKA_SQL,
                KafkaKind.TLS,
                claim,
                claimValue,
                adminSuffix,
                isAdminAllowed
        );
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/adminOverrideClaimData.csv", numLinesToSkip = 1)
    @Tag("kafkasql")
    public void testRegistryKafkasqlSCRAMKeycloakRoleBasedAuthorizationAdminOverrideClaim(
            String claim,
            String claimValue,
            String adminSuffix,
            boolean isAdminAllowed
    ) throws InterruptedException {
        runRoleBasedAuthorizationAdminOverrideClaimTest(
                PersistenceKind.KAFKA_SQL,
                KafkaKind.SCRAM,
                claim,
                claimValue,
                adminSuffix,
                isAdminAllowed
        );
    }
}
