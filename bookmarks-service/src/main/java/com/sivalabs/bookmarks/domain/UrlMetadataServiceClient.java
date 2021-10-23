package com.sivalabs.bookmarks.domain;

import com.sivalabs.bookmarks.config.ApplicationProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class UrlMetadataServiceClient {
    private final RestTemplate restTemplate;
    private final ApplicationProperties properties;

    public UrlMetadata getUrlMetadata(String url) {
        UrlMetadata metadata = new UrlMetadata();
        try {
            String apiUrl = properties.getUrlMetadataServiceUrl() + "/api/v1/url-metadata?url=" + url;
            ResponseEntity<UrlMetadata> response =
                    restTemplate.getForEntity(apiUrl, UrlMetadata.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                metadata = Objects.requireNonNull(response.getBody());
                log.info("Title for URL: {} is : {}", url, metadata.getTitle());
            }
        } catch (Exception e) {
            log.error("Error while getting metadata for URL: " + url, e);
        }
        return metadata;
    }
}
