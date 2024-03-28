package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final DevLibraryAccessors laccForDevLibraryAccessors = new DevLibraryAccessors(owner);
    private final IoLibraryAccessors laccForIoLibraryAccessors = new IoLibraryAccessors(owner);
    private final JavaxLibraryAccessors laccForJavaxLibraryAccessors = new JavaxLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>com</b>
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Group of libraries at <b>dev</b>
     */
    public DevLibraryAccessors getDev() {
        return laccForDevLibraryAccessors;
    }

    /**
     * Group of libraries at <b>io</b>
     */
    public IoLibraryAccessors getIo() {
        return laccForIoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>javax</b>
     */
    public JavaxLibraryAccessors getJavax() {
        return laccForJavaxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>org</b>
     */
    public OrgLibraryAccessors getOrg() {
        return laccForOrgLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComGithubLibraryAccessors laccForComGithubLibraryAccessors = new ComGithubLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github</b>
         */
        public ComGithubLibraryAccessors getGithub() {
            return laccForComGithubLibraryAccessors;
        }

    }

    public static class ComGithubLibraryAccessors extends SubDependencyFactory {
        private final ComGithubUlisesbocchioLibraryAccessors laccForComGithubUlisesbocchioLibraryAccessors = new ComGithubUlisesbocchioLibraryAccessors(owner);

        public ComGithubLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.ulisesbocchio</b>
         */
        public ComGithubUlisesbocchioLibraryAccessors getUlisesbocchio() {
            return laccForComGithubUlisesbocchioLibraryAccessors;
        }

    }

    public static class ComGithubUlisesbocchioLibraryAccessors extends SubDependencyFactory {
        private final ComGithubUlisesbocchioJasyptLibraryAccessors laccForComGithubUlisesbocchioJasyptLibraryAccessors = new ComGithubUlisesbocchioJasyptLibraryAccessors(owner);

        public ComGithubUlisesbocchioLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.ulisesbocchio.jasypt</b>
         */
        public ComGithubUlisesbocchioJasyptLibraryAccessors getJasypt() {
            return laccForComGithubUlisesbocchioJasyptLibraryAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptLibraryAccessors extends SubDependencyFactory {
        private final ComGithubUlisesbocchioJasyptSpringLibraryAccessors laccForComGithubUlisesbocchioJasyptSpringLibraryAccessors = new ComGithubUlisesbocchioJasyptSpringLibraryAccessors(owner);

        public ComGithubUlisesbocchioJasyptLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.ulisesbocchio.jasypt.spring</b>
         */
        public ComGithubUlisesbocchioJasyptSpringLibraryAccessors getSpring() {
            return laccForComGithubUlisesbocchioJasyptSpringLibraryAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptSpringLibraryAccessors extends SubDependencyFactory {
        private final ComGithubUlisesbocchioJasyptSpringBootLibraryAccessors laccForComGithubUlisesbocchioJasyptSpringBootLibraryAccessors = new ComGithubUlisesbocchioJasyptSpringBootLibraryAccessors(owner);

        public ComGithubUlisesbocchioJasyptSpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.github.ulisesbocchio.jasypt.spring.boot</b>
         */
        public ComGithubUlisesbocchioJasyptSpringBootLibraryAccessors getBoot() {
            return laccForComGithubUlisesbocchioJasyptSpringBootLibraryAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptSpringBootLibraryAccessors extends SubDependencyFactory {

        public ComGithubUlisesbocchioJasyptSpringBootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>starter</b> with <b>com.github.ulisesbocchio:jasypt-spring-boot-starter</b> coordinates and
         * with version reference <b>com.github.ulisesbocchio.jasypt.spring.boot.starter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getStarter() {
            return create("com.github.ulisesbocchio.jasypt.spring.boot.starter");
        }

    }

    public static class DevLibraryAccessors extends SubDependencyFactory {
        private final DevKordLibraryAccessors laccForDevKordLibraryAccessors = new DevKordLibraryAccessors(owner);

        public DevLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.kord</b>
         */
        public DevKordLibraryAccessors getKord() {
            return laccForDevKordLibraryAccessors;
        }

    }

    public static class DevKordLibraryAccessors extends SubDependencyFactory {
        private final DevKordKordLibraryAccessors laccForDevKordKordLibraryAccessors = new DevKordKordLibraryAccessors(owner);

        public DevKordLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>dev.kord.kord</b>
         */
        public DevKordKordLibraryAccessors getKord() {
            return laccForDevKordKordLibraryAccessors;
        }

    }

    public static class DevKordKordLibraryAccessors extends SubDependencyFactory {

        public DevKordKordLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>dev.kord:kord-core</b> coordinates and
         * with version reference <b>dev.kord.kord.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("dev.kord.kord.core");
        }

    }

    public static class IoLibraryAccessors extends SubDependencyFactory {
        private final IoJsonwebtokenLibraryAccessors laccForIoJsonwebtokenLibraryAccessors = new IoJsonwebtokenLibraryAccessors(owner);

        public IoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.jsonwebtoken</b>
         */
        public IoJsonwebtokenLibraryAccessors getJsonwebtoken() {
            return laccForIoJsonwebtokenLibraryAccessors;
        }

    }

    public static class IoJsonwebtokenLibraryAccessors extends SubDependencyFactory {

        public IoJsonwebtokenLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jjwt</b> with <b>io.jsonwebtoken:jjwt</b> coordinates and
         * with version reference <b>io.jsonwebtoken.jjwt</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJjwt() {
            return create("io.jsonwebtoken.jjwt");
        }

    }

    public static class JavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxPersistenceLibraryAccessors laccForJavaxPersistenceLibraryAccessors = new JavaxPersistenceLibraryAccessors(owner);

        public JavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.persistence</b>
         */
        public JavaxPersistenceLibraryAccessors getPersistence() {
            return laccForJavaxPersistenceLibraryAccessors;
        }

    }

    public static class JavaxPersistenceLibraryAccessors extends SubDependencyFactory {
        private final JavaxPersistenceJavaxLibraryAccessors laccForJavaxPersistenceJavaxLibraryAccessors = new JavaxPersistenceJavaxLibraryAccessors(owner);

        public JavaxPersistenceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.persistence.javax</b>
         */
        public JavaxPersistenceJavaxLibraryAccessors getJavax() {
            return laccForJavaxPersistenceJavaxLibraryAccessors;
        }

    }

    public static class JavaxPersistenceJavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxPersistenceJavaxPersistenceLibraryAccessors laccForJavaxPersistenceJavaxPersistenceLibraryAccessors = new JavaxPersistenceJavaxPersistenceLibraryAccessors(owner);

        public JavaxPersistenceJavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.persistence.javax.persistence</b>
         */
        public JavaxPersistenceJavaxPersistenceLibraryAccessors getPersistence() {
            return laccForJavaxPersistenceJavaxPersistenceLibraryAccessors;
        }

    }

    public static class JavaxPersistenceJavaxPersistenceLibraryAccessors extends SubDependencyFactory {

        public JavaxPersistenceJavaxPersistenceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>javax.persistence:javax.persistence-api</b> coordinates and
         * with version reference <b>javax.persistence.javax.persistence.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("javax.persistence.javax.persistence.api");
        }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgJetbrainsLibraryAccessors laccForOrgJetbrainsLibraryAccessors = new OrgJetbrainsLibraryAccessors(owner);
        private final OrgJunitLibraryAccessors laccForOrgJunitLibraryAccessors = new OrgJunitLibraryAccessors(owner);
        private final OrgMariadbLibraryAccessors laccForOrgMariadbLibraryAccessors = new OrgMariadbLibraryAccessors(owner);
        private final OrgProjectlombokLibraryAccessors laccForOrgProjectlombokLibraryAccessors = new OrgProjectlombokLibraryAccessors(owner);
        private final OrgSpringframeworkLibraryAccessors laccForOrgSpringframeworkLibraryAccessors = new OrgSpringframeworkLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jetbrains</b>
         */
        public OrgJetbrainsLibraryAccessors getJetbrains() {
            return laccForOrgJetbrainsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.junit</b>
         */
        public OrgJunitLibraryAccessors getJunit() {
            return laccForOrgJunitLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.mariadb</b>
         */
        public OrgMariadbLibraryAccessors getMariadb() {
            return laccForOrgMariadbLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.projectlombok</b>
         */
        public OrgProjectlombokLibraryAccessors getProjectlombok() {
            return laccForOrgProjectlombokLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.springframework</b>
         */
        public OrgSpringframeworkLibraryAccessors getSpringframework() {
            return laccForOrgSpringframeworkLibraryAccessors;
        }

    }

    public static class OrgJetbrainsLibraryAccessors extends SubDependencyFactory {
        private final OrgJetbrainsKotlinLibraryAccessors laccForOrgJetbrainsKotlinLibraryAccessors = new OrgJetbrainsKotlinLibraryAccessors(owner);

        public OrgJetbrainsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jetbrains.kotlin</b>
         */
        public OrgJetbrainsKotlinLibraryAccessors getKotlin() {
            return laccForOrgJetbrainsKotlinLibraryAccessors;
        }

    }

    public static class OrgJetbrainsKotlinLibraryAccessors extends SubDependencyFactory {
        private final OrgJetbrainsKotlinKotlinLibraryAccessors laccForOrgJetbrainsKotlinKotlinLibraryAccessors = new OrgJetbrainsKotlinKotlinLibraryAccessors(owner);

        public OrgJetbrainsKotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jetbrains.kotlin.kotlin</b>
         */
        public OrgJetbrainsKotlinKotlinLibraryAccessors getKotlin() {
            return laccForOrgJetbrainsKotlinKotlinLibraryAccessors;
        }

    }

    public static class OrgJetbrainsKotlinKotlinLibraryAccessors extends SubDependencyFactory {
        private final OrgJetbrainsKotlinKotlinStdlibLibraryAccessors laccForOrgJetbrainsKotlinKotlinStdlibLibraryAccessors = new OrgJetbrainsKotlinKotlinStdlibLibraryAccessors(owner);

        public OrgJetbrainsKotlinKotlinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>reflect</b> with <b>org.jetbrains.kotlin:kotlin-reflect</b> coordinates and
         * with version reference <b>org.jetbrains.kotlin.kotlin.reflect</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReflect() {
            return create("org.jetbrains.kotlin.kotlin.reflect");
        }

        /**
         * Dependency provider for <b>test</b> with <b>org.jetbrains.kotlin:kotlin-test</b> coordinates and
         * with version reference <b>org.jetbrains.kotlin.kotlin.test</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTest() {
            return create("org.jetbrains.kotlin.kotlin.test");
        }

        /**
         * Group of libraries at <b>org.jetbrains.kotlin.kotlin.stdlib</b>
         */
        public OrgJetbrainsKotlinKotlinStdlibLibraryAccessors getStdlib() {
            return laccForOrgJetbrainsKotlinKotlinStdlibLibraryAccessors;
        }

    }

    public static class OrgJetbrainsKotlinKotlinStdlibLibraryAccessors extends SubDependencyFactory {

        public OrgJetbrainsKotlinKotlinStdlibLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jdk8</b> with <b>org.jetbrains.kotlin:kotlin-stdlib-jdk8</b> coordinates and
         * with version reference <b>org.jetbrains.kotlin.kotlin.stdlib.jdk8</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJdk8() {
            return create("org.jetbrains.kotlin.kotlin.stdlib.jdk8");
        }

    }

    public static class OrgJunitLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterLibraryAccessors laccForOrgJunitJupiterLibraryAccessors = new OrgJunitJupiterLibraryAccessors(owner);

        public OrgJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.junit.jupiter</b>
         */
        public OrgJunitJupiterLibraryAccessors getJupiter() {
            return laccForOrgJunitJupiterLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterLibraryAccessors extends SubDependencyFactory {
        private final OrgJunitJupiterJunitLibraryAccessors laccForOrgJunitJupiterJunitLibraryAccessors = new OrgJunitJupiterJunitLibraryAccessors(owner);

        public OrgJunitJupiterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.junit.jupiter.junit</b>
         */
        public OrgJunitJupiterJunitLibraryAccessors getJunit() {
            return laccForOrgJunitJupiterJunitLibraryAccessors;
        }

    }

    public static class OrgJunitJupiterJunitLibraryAccessors extends SubDependencyFactory {

        public OrgJunitJupiterJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jupiter</b> with <b>org.junit.jupiter:junit-jupiter</b> coordinates and
         * with version reference <b>org.junit.jupiter.junit.jupiter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJupiter() {
            return create("org.junit.jupiter.junit.jupiter");
        }

    }

    public static class OrgMariadbLibraryAccessors extends SubDependencyFactory {
        private final OrgMariadbJdbcLibraryAccessors laccForOrgMariadbJdbcLibraryAccessors = new OrgMariadbJdbcLibraryAccessors(owner);

        public OrgMariadbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.mariadb.jdbc</b>
         */
        public OrgMariadbJdbcLibraryAccessors getJdbc() {
            return laccForOrgMariadbJdbcLibraryAccessors;
        }

    }

    public static class OrgMariadbJdbcLibraryAccessors extends SubDependencyFactory {
        private final OrgMariadbJdbcMariadbLibraryAccessors laccForOrgMariadbJdbcMariadbLibraryAccessors = new OrgMariadbJdbcMariadbLibraryAccessors(owner);

        public OrgMariadbJdbcLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.mariadb.jdbc.mariadb</b>
         */
        public OrgMariadbJdbcMariadbLibraryAccessors getMariadb() {
            return laccForOrgMariadbJdbcMariadbLibraryAccessors;
        }

    }

    public static class OrgMariadbJdbcMariadbLibraryAccessors extends SubDependencyFactory {
        private final OrgMariadbJdbcMariadbJavaLibraryAccessors laccForOrgMariadbJdbcMariadbJavaLibraryAccessors = new OrgMariadbJdbcMariadbJavaLibraryAccessors(owner);

        public OrgMariadbJdbcMariadbLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.mariadb.jdbc.mariadb.java</b>
         */
        public OrgMariadbJdbcMariadbJavaLibraryAccessors getJava() {
            return laccForOrgMariadbJdbcMariadbJavaLibraryAccessors;
        }

    }

    public static class OrgMariadbJdbcMariadbJavaLibraryAccessors extends SubDependencyFactory {

        public OrgMariadbJdbcMariadbJavaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>client</b> with <b>org.mariadb.jdbc:mariadb-java-client</b> coordinates and
         * with version reference <b>org.mariadb.jdbc.mariadb.java.client</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClient() {
            return create("org.mariadb.jdbc.mariadb.java.client");
        }

    }

    public static class OrgProjectlombokLibraryAccessors extends SubDependencyFactory {

        public OrgProjectlombokLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>lombok</b> with <b>org.projectlombok:lombok</b> coordinates and
         * with version reference <b>org.projectlombok.lombok</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLombok() {
            return create("org.projectlombok.lombok");
        }

    }

    public static class OrgSpringframeworkLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkBootLibraryAccessors laccForOrgSpringframeworkBootLibraryAccessors = new OrgSpringframeworkBootLibraryAccessors(owner);
        private final OrgSpringframeworkSessionLibraryAccessors laccForOrgSpringframeworkSessionLibraryAccessors = new OrgSpringframeworkSessionLibraryAccessors(owner);

        public OrgSpringframeworkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.boot</b>
         */
        public OrgSpringframeworkBootLibraryAccessors getBoot() {
            return laccForOrgSpringframeworkBootLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.springframework.session</b>
         */
        public OrgSpringframeworkSessionLibraryAccessors getSession() {
            return laccForOrgSpringframeworkSessionLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkBootLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkBootSpringLibraryAccessors laccForOrgSpringframeworkBootSpringLibraryAccessors = new OrgSpringframeworkBootSpringLibraryAccessors(owner);

        public OrgSpringframeworkBootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.boot.spring</b>
         */
        public OrgSpringframeworkBootSpringLibraryAccessors getSpring() {
            return laccForOrgSpringframeworkBootSpringLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkBootSpringBootLibraryAccessors laccForOrgSpringframeworkBootSpringBootLibraryAccessors = new OrgSpringframeworkBootSpringBootLibraryAccessors(owner);

        public OrgSpringframeworkBootSpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.boot.spring.boot</b>
         */
        public OrgSpringframeworkBootSpringBootLibraryAccessors getBoot() {
            return laccForOrgSpringframeworkBootSpringBootLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkBootSpringBootConfigurationLibraryAccessors laccForOrgSpringframeworkBootSpringBootConfigurationLibraryAccessors = new OrgSpringframeworkBootSpringBootConfigurationLibraryAccessors(owner);
        private final OrgSpringframeworkBootSpringBootStarterLibraryAccessors laccForOrgSpringframeworkBootSpringBootStarterLibraryAccessors = new OrgSpringframeworkBootSpringBootStarterLibraryAccessors(owner);

        public OrgSpringframeworkBootSpringBootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>devtools</b> with <b>org.springframework.boot:spring-boot-devtools</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.devtools</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDevtools() {
            return create("org.springframework.boot.spring.boot.devtools");
        }

        /**
         * Group of libraries at <b>org.springframework.boot.spring.boot.configuration</b>
         */
        public OrgSpringframeworkBootSpringBootConfigurationLibraryAccessors getConfiguration() {
            return laccForOrgSpringframeworkBootSpringBootConfigurationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.springframework.boot.spring.boot.starter</b>
         */
        public OrgSpringframeworkBootSpringBootStarterLibraryAccessors getStarter() {
            return laccForOrgSpringframeworkBootSpringBootStarterLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootConfigurationLibraryAccessors extends SubDependencyFactory {

        public OrgSpringframeworkBootSpringBootConfigurationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>processor</b> with <b>org.springframework.boot:spring-boot-configuration-processor</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.configuration.processor</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProcessor() {
            return create("org.springframework.boot.spring.boot.configuration.processor");
        }

    }

    public static class OrgSpringframeworkBootSpringBootStarterLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final OrgSpringframeworkBootSpringBootStarterDataLibraryAccessors laccForOrgSpringframeworkBootSpringBootStarterDataLibraryAccessors = new OrgSpringframeworkBootSpringBootStarterDataLibraryAccessors(owner);

        public OrgSpringframeworkBootSpringBootStarterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>starter</b> with <b>org.springframework.boot:spring-boot-starter</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("org.springframework.boot.spring.boot.starter");
        }

        /**
         * Dependency provider for <b>mail</b> with <b>org.springframework.boot:spring-boot-starter-mail</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter.mail</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMail() {
            return create("org.springframework.boot.spring.boot.starter.mail");
        }

        /**
         * Dependency provider for <b>security</b> with <b>org.springframework.boot:spring-boot-starter-security</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter.security</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSecurity() {
            return create("org.springframework.boot.spring.boot.starter.security");
        }

        /**
         * Dependency provider for <b>test</b> with <b>org.springframework.boot:spring-boot-starter-test</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter.test</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTest() {
            return create("org.springframework.boot.spring.boot.starter.test");
        }

        /**
         * Dependency provider for <b>web</b> with <b>org.springframework.boot:spring-boot-starter-web</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter.web</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWeb() {
            return create("org.springframework.boot.spring.boot.starter.web");
        }

        /**
         * Group of libraries at <b>org.springframework.boot.spring.boot.starter.data</b>
         */
        public OrgSpringframeworkBootSpringBootStarterDataLibraryAccessors getData() {
            return laccForOrgSpringframeworkBootSpringBootStarterDataLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootStarterDataLibraryAccessors extends SubDependencyFactory {

        public OrgSpringframeworkBootSpringBootStarterDataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jpa</b> with <b>org.springframework.boot:spring-boot-starter-data-jpa</b> coordinates and
         * with version reference <b>org.springframework.boot.spring.boot.starter.data.jpa</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJpa() {
            return create("org.springframework.boot.spring.boot.starter.data.jpa");
        }

    }

    public static class OrgSpringframeworkSessionLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkSessionSpringLibraryAccessors laccForOrgSpringframeworkSessionSpringLibraryAccessors = new OrgSpringframeworkSessionSpringLibraryAccessors(owner);

        public OrgSpringframeworkSessionLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.session.spring</b>
         */
        public OrgSpringframeworkSessionSpringLibraryAccessors getSpring() {
            return laccForOrgSpringframeworkSessionSpringLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkSessionSpringLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkSessionSpringSessionLibraryAccessors laccForOrgSpringframeworkSessionSpringSessionLibraryAccessors = new OrgSpringframeworkSessionSpringSessionLibraryAccessors(owner);

        public OrgSpringframeworkSessionSpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.session.spring.session</b>
         */
        public OrgSpringframeworkSessionSpringSessionLibraryAccessors getSession() {
            return laccForOrgSpringframeworkSessionSpringSessionLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkSessionSpringSessionLibraryAccessors extends SubDependencyFactory {

        public OrgSpringframeworkSessionSpringSessionLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.springframework.session:spring-session-core</b> coordinates and
         * with version reference <b>org.springframework.session.spring.session.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.springframework.session.spring.session.core");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final DevVersionAccessors vaccForDevVersionAccessors = new DevVersionAccessors(providers, config);
        private final IoVersionAccessors vaccForIoVersionAccessors = new IoVersionAccessors(providers, config);
        private final JavaxVersionAccessors vaccForJavaxVersionAccessors = new JavaxVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com</b>
         */
        public ComVersionAccessors getCom() {
            return vaccForComVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.dev</b>
         */
        public DevVersionAccessors getDev() {
            return vaccForDevVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.io</b>
         */
        public IoVersionAccessors getIo() {
            return vaccForIoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.javax</b>
         */
        public JavaxVersionAccessors getJavax() {
            return vaccForJavaxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org</b>
         */
        public OrgVersionAccessors getOrg() {
            return vaccForOrgVersionAccessors;
        }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComGithubVersionAccessors vaccForComGithubVersionAccessors = new ComGithubVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.github</b>
         */
        public ComGithubVersionAccessors getGithub() {
            return vaccForComGithubVersionAccessors;
        }

    }

    public static class ComGithubVersionAccessors extends VersionFactory  {

        private final ComGithubUlisesbocchioVersionAccessors vaccForComGithubUlisesbocchioVersionAccessors = new ComGithubUlisesbocchioVersionAccessors(providers, config);
        public ComGithubVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.github.ulisesbocchio</b>
         */
        public ComGithubUlisesbocchioVersionAccessors getUlisesbocchio() {
            return vaccForComGithubUlisesbocchioVersionAccessors;
        }

    }

    public static class ComGithubUlisesbocchioVersionAccessors extends VersionFactory  {

        private final ComGithubUlisesbocchioJasyptVersionAccessors vaccForComGithubUlisesbocchioJasyptVersionAccessors = new ComGithubUlisesbocchioJasyptVersionAccessors(providers, config);
        public ComGithubUlisesbocchioVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.github.ulisesbocchio.jasypt</b>
         */
        public ComGithubUlisesbocchioJasyptVersionAccessors getJasypt() {
            return vaccForComGithubUlisesbocchioJasyptVersionAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptVersionAccessors extends VersionFactory  {

        private final ComGithubUlisesbocchioJasyptSpringVersionAccessors vaccForComGithubUlisesbocchioJasyptSpringVersionAccessors = new ComGithubUlisesbocchioJasyptSpringVersionAccessors(providers, config);
        public ComGithubUlisesbocchioJasyptVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.github.ulisesbocchio.jasypt.spring</b>
         */
        public ComGithubUlisesbocchioJasyptSpringVersionAccessors getSpring() {
            return vaccForComGithubUlisesbocchioJasyptSpringVersionAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptSpringVersionAccessors extends VersionFactory  {

        private final ComGithubUlisesbocchioJasyptSpringBootVersionAccessors vaccForComGithubUlisesbocchioJasyptSpringBootVersionAccessors = new ComGithubUlisesbocchioJasyptSpringBootVersionAccessors(providers, config);
        public ComGithubUlisesbocchioJasyptSpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.github.ulisesbocchio.jasypt.spring.boot</b>
         */
        public ComGithubUlisesbocchioJasyptSpringBootVersionAccessors getBoot() {
            return vaccForComGithubUlisesbocchioJasyptSpringBootVersionAccessors;
        }

    }

    public static class ComGithubUlisesbocchioJasyptSpringBootVersionAccessors extends VersionFactory  {

        public ComGithubUlisesbocchioJasyptSpringBootVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.github.ulisesbocchio.jasypt.spring.boot.starter</b> with value <b>3.0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getStarter() { return getVersion("com.github.ulisesbocchio.jasypt.spring.boot.starter"); }

    }

    public static class DevVersionAccessors extends VersionFactory  {

        private final DevKordVersionAccessors vaccForDevKordVersionAccessors = new DevKordVersionAccessors(providers, config);
        public DevVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.dev.kord</b>
         */
        public DevKordVersionAccessors getKord() {
            return vaccForDevKordVersionAccessors;
        }

    }

    public static class DevKordVersionAccessors extends VersionFactory  {

        private final DevKordKordVersionAccessors vaccForDevKordKordVersionAccessors = new DevKordKordVersionAccessors(providers, config);
        public DevKordVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.dev.kord.kord</b>
         */
        public DevKordKordVersionAccessors getKord() {
            return vaccForDevKordKordVersionAccessors;
        }

    }

    public static class DevKordKordVersionAccessors extends VersionFactory  {

        public DevKordKordVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>dev.kord.kord.core</b> with value <b>0.13.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("dev.kord.kord.core"); }

    }

    public static class IoVersionAccessors extends VersionFactory  {

        private final IoJsonwebtokenVersionAccessors vaccForIoJsonwebtokenVersionAccessors = new IoJsonwebtokenVersionAccessors(providers, config);
        public IoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.jsonwebtoken</b>
         */
        public IoJsonwebtokenVersionAccessors getJsonwebtoken() {
            return vaccForIoJsonwebtokenVersionAccessors;
        }

    }

    public static class IoJsonwebtokenVersionAccessors extends VersionFactory  {

        public IoJsonwebtokenVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>io.jsonwebtoken.jjwt</b> with value <b>0.9.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJjwt() { return getVersion("io.jsonwebtoken.jjwt"); }

    }

    public static class JavaxVersionAccessors extends VersionFactory  {

        private final JavaxPersistenceVersionAccessors vaccForJavaxPersistenceVersionAccessors = new JavaxPersistenceVersionAccessors(providers, config);
        public JavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.persistence</b>
         */
        public JavaxPersistenceVersionAccessors getPersistence() {
            return vaccForJavaxPersistenceVersionAccessors;
        }

    }

    public static class JavaxPersistenceVersionAccessors extends VersionFactory  {

        private final JavaxPersistenceJavaxVersionAccessors vaccForJavaxPersistenceJavaxVersionAccessors = new JavaxPersistenceJavaxVersionAccessors(providers, config);
        public JavaxPersistenceVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.persistence.javax</b>
         */
        public JavaxPersistenceJavaxVersionAccessors getJavax() {
            return vaccForJavaxPersistenceJavaxVersionAccessors;
        }

    }

    public static class JavaxPersistenceJavaxVersionAccessors extends VersionFactory  {

        private final JavaxPersistenceJavaxPersistenceVersionAccessors vaccForJavaxPersistenceJavaxPersistenceVersionAccessors = new JavaxPersistenceJavaxPersistenceVersionAccessors(providers, config);
        public JavaxPersistenceJavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.persistence.javax.persistence</b>
         */
        public JavaxPersistenceJavaxPersistenceVersionAccessors getPersistence() {
            return vaccForJavaxPersistenceJavaxPersistenceVersionAccessors;
        }

    }

    public static class JavaxPersistenceJavaxPersistenceVersionAccessors extends VersionFactory  {

        public JavaxPersistenceJavaxPersistenceVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>javax.persistence.javax.persistence.api</b> with value <b>2.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("javax.persistence.javax.persistence.api"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgJetbrainsVersionAccessors vaccForOrgJetbrainsVersionAccessors = new OrgJetbrainsVersionAccessors(providers, config);
        private final OrgJunitVersionAccessors vaccForOrgJunitVersionAccessors = new OrgJunitVersionAccessors(providers, config);
        private final OrgMariadbVersionAccessors vaccForOrgMariadbVersionAccessors = new OrgMariadbVersionAccessors(providers, config);
        private final OrgProjectlombokVersionAccessors vaccForOrgProjectlombokVersionAccessors = new OrgProjectlombokVersionAccessors(providers, config);
        private final OrgSpringframeworkVersionAccessors vaccForOrgSpringframeworkVersionAccessors = new OrgSpringframeworkVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jetbrains</b>
         */
        public OrgJetbrainsVersionAccessors getJetbrains() {
            return vaccForOrgJetbrainsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.junit</b>
         */
        public OrgJunitVersionAccessors getJunit() {
            return vaccForOrgJunitVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.mariadb</b>
         */
        public OrgMariadbVersionAccessors getMariadb() {
            return vaccForOrgMariadbVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.projectlombok</b>
         */
        public OrgProjectlombokVersionAccessors getProjectlombok() {
            return vaccForOrgProjectlombokVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.springframework</b>
         */
        public OrgSpringframeworkVersionAccessors getSpringframework() {
            return vaccForOrgSpringframeworkVersionAccessors;
        }

    }

    public static class OrgJetbrainsVersionAccessors extends VersionFactory  {

        private final OrgJetbrainsKotlinVersionAccessors vaccForOrgJetbrainsKotlinVersionAccessors = new OrgJetbrainsKotlinVersionAccessors(providers, config);
        public OrgJetbrainsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jetbrains.kotlin</b>
         */
        public OrgJetbrainsKotlinVersionAccessors getKotlin() {
            return vaccForOrgJetbrainsKotlinVersionAccessors;
        }

    }

    public static class OrgJetbrainsKotlinVersionAccessors extends VersionFactory  {

        private final OrgJetbrainsKotlinKotlinVersionAccessors vaccForOrgJetbrainsKotlinKotlinVersionAccessors = new OrgJetbrainsKotlinKotlinVersionAccessors(providers, config);
        public OrgJetbrainsKotlinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jetbrains.kotlin.kotlin</b>
         */
        public OrgJetbrainsKotlinKotlinVersionAccessors getKotlin() {
            return vaccForOrgJetbrainsKotlinKotlinVersionAccessors;
        }

    }

    public static class OrgJetbrainsKotlinKotlinVersionAccessors extends VersionFactory  {

        private final OrgJetbrainsKotlinKotlinStdlibVersionAccessors vaccForOrgJetbrainsKotlinKotlinStdlibVersionAccessors = new OrgJetbrainsKotlinKotlinStdlibVersionAccessors(providers, config);
        public OrgJetbrainsKotlinKotlinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jetbrains.kotlin.kotlin.reflect</b> with value <b>2.0.0-Beta3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getReflect() { return getVersion("org.jetbrains.kotlin.kotlin.reflect"); }

        /**
         * Version alias <b>org.jetbrains.kotlin.kotlin.test</b> with value <b>2.0.0-Beta3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTest() { return getVersion("org.jetbrains.kotlin.kotlin.test"); }

        /**
         * Group of versions at <b>versions.org.jetbrains.kotlin.kotlin.stdlib</b>
         */
        public OrgJetbrainsKotlinKotlinStdlibVersionAccessors getStdlib() {
            return vaccForOrgJetbrainsKotlinKotlinStdlibVersionAccessors;
        }

    }

    public static class OrgJetbrainsKotlinKotlinStdlibVersionAccessors extends VersionFactory  {

        public OrgJetbrainsKotlinKotlinStdlibVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jetbrains.kotlin.kotlin.stdlib.jdk8</b> with value <b>2.0.0-Beta3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJdk8() { return getVersion("org.jetbrains.kotlin.kotlin.stdlib.jdk8"); }

    }

    public static class OrgJunitVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterVersionAccessors vaccForOrgJunitJupiterVersionAccessors = new OrgJunitJupiterVersionAccessors(providers, config);
        public OrgJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.junit.jupiter</b>
         */
        public OrgJunitJupiterVersionAccessors getJupiter() {
            return vaccForOrgJunitJupiterVersionAccessors;
        }

    }

    public static class OrgJunitJupiterVersionAccessors extends VersionFactory  {

        private final OrgJunitJupiterJunitVersionAccessors vaccForOrgJunitJupiterJunitVersionAccessors = new OrgJunitJupiterJunitVersionAccessors(providers, config);
        public OrgJunitJupiterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.junit.jupiter.junit</b>
         */
        public OrgJunitJupiterJunitVersionAccessors getJunit() {
            return vaccForOrgJunitJupiterJunitVersionAccessors;
        }

    }

    public static class OrgJunitJupiterJunitVersionAccessors extends VersionFactory  {

        public OrgJunitJupiterJunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.junit.jupiter.junit.jupiter</b> with value <b>5.8.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJupiter() { return getVersion("org.junit.jupiter.junit.jupiter"); }

    }

    public static class OrgMariadbVersionAccessors extends VersionFactory  {

        private final OrgMariadbJdbcVersionAccessors vaccForOrgMariadbJdbcVersionAccessors = new OrgMariadbJdbcVersionAccessors(providers, config);
        public OrgMariadbVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.mariadb.jdbc</b>
         */
        public OrgMariadbJdbcVersionAccessors getJdbc() {
            return vaccForOrgMariadbJdbcVersionAccessors;
        }

    }

    public static class OrgMariadbJdbcVersionAccessors extends VersionFactory  {

        private final OrgMariadbJdbcMariadbVersionAccessors vaccForOrgMariadbJdbcMariadbVersionAccessors = new OrgMariadbJdbcMariadbVersionAccessors(providers, config);
        public OrgMariadbJdbcVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.mariadb.jdbc.mariadb</b>
         */
        public OrgMariadbJdbcMariadbVersionAccessors getMariadb() {
            return vaccForOrgMariadbJdbcMariadbVersionAccessors;
        }

    }

    public static class OrgMariadbJdbcMariadbVersionAccessors extends VersionFactory  {

        private final OrgMariadbJdbcMariadbJavaVersionAccessors vaccForOrgMariadbJdbcMariadbJavaVersionAccessors = new OrgMariadbJdbcMariadbJavaVersionAccessors(providers, config);
        public OrgMariadbJdbcMariadbVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.mariadb.jdbc.mariadb.java</b>
         */
        public OrgMariadbJdbcMariadbJavaVersionAccessors getJava() {
            return vaccForOrgMariadbJdbcMariadbJavaVersionAccessors;
        }

    }

    public static class OrgMariadbJdbcMariadbJavaVersionAccessors extends VersionFactory  {

        public OrgMariadbJdbcMariadbJavaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.mariadb.jdbc.mariadb.java.client</b> with value <b>2.7.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getClient() { return getVersion("org.mariadb.jdbc.mariadb.java.client"); }

    }

    public static class OrgProjectlombokVersionAccessors extends VersionFactory  {

        public OrgProjectlombokVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.projectlombok.lombok</b> with value <b>1.18.22</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLombok() { return getVersion("org.projectlombok.lombok"); }

    }

    public static class OrgSpringframeworkVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkBootVersionAccessors vaccForOrgSpringframeworkBootVersionAccessors = new OrgSpringframeworkBootVersionAccessors(providers, config);
        private final OrgSpringframeworkSessionVersionAccessors vaccForOrgSpringframeworkSessionVersionAccessors = new OrgSpringframeworkSessionVersionAccessors(providers, config);
        public OrgSpringframeworkVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.boot</b>
         */
        public OrgSpringframeworkBootVersionAccessors getBoot() {
            return vaccForOrgSpringframeworkBootVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.springframework.session</b>
         */
        public OrgSpringframeworkSessionVersionAccessors getSession() {
            return vaccForOrgSpringframeworkSessionVersionAccessors;
        }

    }

    public static class OrgSpringframeworkBootVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkBootSpringVersionAccessors vaccForOrgSpringframeworkBootSpringVersionAccessors = new OrgSpringframeworkBootSpringVersionAccessors(providers, config);
        public OrgSpringframeworkBootVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.boot.spring</b>
         */
        public OrgSpringframeworkBootSpringVersionAccessors getSpring() {
            return vaccForOrgSpringframeworkBootSpringVersionAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkBootSpringBootVersionAccessors vaccForOrgSpringframeworkBootSpringBootVersionAccessors = new OrgSpringframeworkBootSpringBootVersionAccessors(providers, config);
        public OrgSpringframeworkBootSpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.boot.spring.boot</b>
         */
        public OrgSpringframeworkBootSpringBootVersionAccessors getBoot() {
            return vaccForOrgSpringframeworkBootSpringBootVersionAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkBootSpringBootConfigurationVersionAccessors vaccForOrgSpringframeworkBootSpringBootConfigurationVersionAccessors = new OrgSpringframeworkBootSpringBootConfigurationVersionAccessors(providers, config);
        private final OrgSpringframeworkBootSpringBootStarterVersionAccessors vaccForOrgSpringframeworkBootSpringBootStarterVersionAccessors = new OrgSpringframeworkBootSpringBootStarterVersionAccessors(providers, config);
        public OrgSpringframeworkBootSpringBootVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.devtools</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDevtools() { return getVersion("org.springframework.boot.spring.boot.devtools"); }

        /**
         * Group of versions at <b>versions.org.springframework.boot.spring.boot.configuration</b>
         */
        public OrgSpringframeworkBootSpringBootConfigurationVersionAccessors getConfiguration() {
            return vaccForOrgSpringframeworkBootSpringBootConfigurationVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.springframework.boot.spring.boot.starter</b>
         */
        public OrgSpringframeworkBootSpringBootStarterVersionAccessors getStarter() {
            return vaccForOrgSpringframeworkBootSpringBootStarterVersionAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootConfigurationVersionAccessors extends VersionFactory  {

        public OrgSpringframeworkBootSpringBootConfigurationVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.configuration.processor</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getProcessor() { return getVersion("org.springframework.boot.spring.boot.configuration.processor"); }

    }

    public static class OrgSpringframeworkBootSpringBootStarterVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        private final OrgSpringframeworkBootSpringBootStarterDataVersionAccessors vaccForOrgSpringframeworkBootSpringBootStarterDataVersionAccessors = new OrgSpringframeworkBootSpringBootStarterDataVersionAccessors(providers, config);
        public OrgSpringframeworkBootSpringBootStarterVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("org.springframework.boot.spring.boot.starter"); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter.mail</b> with value <b>2.4.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMail() { return getVersion("org.springframework.boot.spring.boot.starter.mail"); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter.security</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSecurity() { return getVersion("org.springframework.boot.spring.boot.starter.security"); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter.test</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTest() { return getVersion("org.springframework.boot.spring.boot.starter.test"); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter.web</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWeb() { return getVersion("org.springframework.boot.spring.boot.starter.web"); }

        /**
         * Group of versions at <b>versions.org.springframework.boot.spring.boot.starter.data</b>
         */
        public OrgSpringframeworkBootSpringBootStarterDataVersionAccessors getData() {
            return vaccForOrgSpringframeworkBootSpringBootStarterDataVersionAccessors;
        }

    }

    public static class OrgSpringframeworkBootSpringBootStarterDataVersionAccessors extends VersionFactory  {

        public OrgSpringframeworkBootSpringBootStarterDataVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.boot.spring.boot.starter.data.jpa</b> with value <b>2.6.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJpa() { return getVersion("org.springframework.boot.spring.boot.starter.data.jpa"); }

    }

    public static class OrgSpringframeworkSessionVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkSessionSpringVersionAccessors vaccForOrgSpringframeworkSessionSpringVersionAccessors = new OrgSpringframeworkSessionSpringVersionAccessors(providers, config);
        public OrgSpringframeworkSessionVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.session.spring</b>
         */
        public OrgSpringframeworkSessionSpringVersionAccessors getSpring() {
            return vaccForOrgSpringframeworkSessionSpringVersionAccessors;
        }

    }

    public static class OrgSpringframeworkSessionSpringVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkSessionSpringSessionVersionAccessors vaccForOrgSpringframeworkSessionSpringSessionVersionAccessors = new OrgSpringframeworkSessionSpringSessionVersionAccessors(providers, config);
        public OrgSpringframeworkSessionSpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.session.spring.session</b>
         */
        public OrgSpringframeworkSessionSpringSessionVersionAccessors getSession() {
            return vaccForOrgSpringframeworkSessionSpringSessionVersionAccessors;
        }

    }

    public static class OrgSpringframeworkSessionSpringSessionVersionAccessors extends VersionFactory  {

        public OrgSpringframeworkSessionSpringSessionVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.session.spring.session.core</b> with value <b>2.6.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.springframework.session.spring.session.core"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
