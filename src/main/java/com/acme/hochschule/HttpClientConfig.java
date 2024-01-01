package com.acme.hochschule;

import com.acme.hochschule.repository.BibliothekRestRepository;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

/**
 * Konfigurationen für den Httpclient.
 */
interface HttpClientConfig {
    int BIBLIOTHEK_DEFAULT_PORT = 8080;
    int TIMEOUT_IN_SECONDS = 10;

    @Bean
    default WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    default UriComponentsBuilder uriComponentsBuilder() {
        final var schema = "http";
        final var host = "localhost";
        final int port = BIBLIOTHEK_DEFAULT_PORT;

        final var log = LoggerFactory.getLogger(HttpClientConfig.class);
        log.error("BibliothekHost: {}, BibliothekPort: {}", host, port);

        return UriComponentsBuilder.newInstance()
            .scheme(schema)
            .host(host)
            .port(port);
    }

    @Bean
    default WebClient restClient(final WebClient.Builder webClientBuilder,
                                 final UriComponentsBuilder uriComponentsBuilder,
                                 final WebClientSsl ssl) {
        final var baseUrl = uriComponentsBuilder.build().toUriString();
        return webClientBuilder
            .baseUrl(baseUrl)
            .apply(ssl.fromBundle("microservice"))
            .build();
    }

    @Bean
    default BibliothekRestRepository bibliothekRestRepository(final WebClient builder) {
        final var clientAdapter = WebClientAdapter.forClient(builder);
        final var proxyFactory = HttpServiceProxyFactory
            .builder(clientAdapter)
            .blockTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
            .build();
        return proxyFactory.createClient(BibliothekRestRepository.class);
    }
}
