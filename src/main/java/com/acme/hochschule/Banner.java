package com.acme.hochschule;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.security.core.SpringSecurityCoreVersion;

/**
 * Banner als String-Konstante für den Start des Servers.
 */
@SuppressWarnings({
    "AccessOfSystemProperties",
    "CallToSystemGetenv",
    "UtilityClassCanBeEnum",
    "UtilityClass",
    "ClassUnconnectedToPackage"
})
@SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
final class Banner {
    private static final String JAVA = Runtime.version().toString() + " - " + System.getProperty("java.vendor");
    private static final String OS_VERSION = System.getProperty("os.name");
    private static final InetAddress LOCALHOST = getLocalhost();
    private static final long MEGABYTE = 1024L * 1024L;
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final String SERVICE_HOST = System.getenv("HOCHSCHULE_SERVICE_HOST");
    private static final String SERVICE_PORT = System.getenv("HOCHSCHULE_SERVICE_PORT");
    private static final String KUBERNETES = SERVICE_HOST == null
        ? "N/A"
        : "HOCHSCHULE_SERVICE_HOST=" + SERVICE_HOST + ", HOCHSCHULE_SERVICE_PORT=" + SERVICE_PORT;
    private static final String USERNAME = System.getProperty("user.name");

    /**
     * Banner für den Server-Start.
     */
    static final String TEXT = """

        $figlet
        (C) Konstantin Kraus, Hochschule Karlsruhe
        Version             2023.1.1
        Spring Boot         $springBoot
        Spring Security     $springSecurity
        Spring Framework    $spring
        Hibernate           $hibernate
        Java                $java
        Betriebssystem      $os
        Rechnername         $rechnername
        IP-Adresse          $ip
        Heap: Size          $heapSize MiB
        Heap: Free          $heapFree MiB
        Kubernetes          $kubernetes
        Username            $username
        JVM Locale          $locale
        GraphiQL            /graphiql   {"Authorization": "Basic YWRtaW46cA=="}
        OpenAPI             /swagger-ui.html /v3/api-docs.yaml
        H2 Console          $h2Console
        """
        .replace("$figlet", getFiglet())
        .replace("$springBoot", SpringBootVersion.getVersion())
        .replace("$springSecurity", SpringSecurityCoreVersion.getVersion())
        .replace("$spring", Objects.requireNonNull(SpringVersion.getVersion()))
        .replace("$hibernate", org.hibernate.Version.getVersionString())
        .replace("$java", JAVA)
        .replace("$os", OS_VERSION)
        .replace("$rechnername", LOCALHOST.getHostName())
        .replace("$ip", LOCALHOST.getHostAddress())
        .replace("$heapSize", String.valueOf(RUNTIME.totalMemory() / MEGABYTE))
        .replace("$heapFree", String.valueOf(RUNTIME.freeMemory() / MEGABYTE))
        .replace("$kubernetes", KUBERNETES)
        .replace("$username", USERNAME)
        .replace("$locale", Locale.getDefault().toString())
        .replace("$h2Console", getH2Console());

    @SuppressWarnings("ImplicitCallToSuper")
    private Banner() {
    }

    private static String getFiglet() {
        // http://patorjk.com/software/taag/#p=display&f=Slant&t=hochschule%20v2
        return """
                __               __              __          __             ___\s
               / /_  ____  _____/ /_  __________/ /_  __  __/ /__     _   _|__ \\
              / __ \\/ __ \\/ ___/ __ \\/ ___/ ___/ __ \\/ / / / / _ \\   | | / /_/ /
             / / / / /_/ / /__/ / / (__  ) /__/ / / / /_/ / /  __/   | |/ / __/\s
            /_/ /_/\\____/\\___/_/ /_/____/\\___/_/ /_/\\__,_/_/\\___/    |___/____/\s
                     \s
            """;
    }

    private static InetAddress getLocalhost() {
        try {
            return InetAddress.getLocalHost();
        } catch (final UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String getH2Console() {
        return Objects.equals(System.getProperty("spring.h2.console.enabled"), "true") ||
            Objects.equals(System.getenv("SPRING_H2_CONSOLE_ENABLED"), "true") ? "/h2-console" : "N/A";
    }
}
