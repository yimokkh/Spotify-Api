package org.example.test.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.test.dto.ExternalApiRequest;
import org.example.test.dto.ExternalApiResponseArtist;
import org.example.test.dto.ExternalApiResponseTrack;
import org.example.test.dto.ExternalApiToken;
import org.example.test.repository.TagRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ExternalApiService {

    String token = null;

    TagRepository tagRepository;
    RestTemplate restTemplate;

    public ExternalApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    @Value("${external.api.clientSecret}")
    private String clientSecret;

    @Value("${external.api.clientId}")
    private String clientId;

    @Value("${external.api.spotifyApiTokenUrl}")
    private String spotifyApiTokenUrl;

    public String updateToken() {
        return "hello";
    }

    private void refreshToken() {
        RestTemplate restTemplateToken = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<ExternalApiToken> response = restTemplateToken.exchange(spotifyApiTokenUrl, HttpMethod.POST, request, ExternalApiToken.class);
        token = Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    public ExternalApiResponseArtist getArtistByName(ExternalApiRequest request) {
        try {
            refreshToken();
            restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String url = "https://api.spotify.com/v1/search?" + "q=" + request.getName() + "&type=artist&limit=1";
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
                    jsonNode = jsonNode.get("artists").get("items").get(0);
                    return new ExternalApiResponseArtist(jsonNode.get("name").asText(),
                            (jsonNode.get("followers").get("total").asInt()),
                            (jsonNode.get("popularity").asInt()),
                            (jsonNode.get("external_urls").get("spotify").asText()));
        } catch (Exception exception) {
            return null;
        }
    }

    public ExternalApiResponseTrack getTrackByName(ExternalApiRequest request) {
        try {
            refreshToken();
            restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String url = "https://api.spotify.com/v1/search?" + "q=" + request.getName() + "&type=track&limit=1";
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            jsonNode = jsonNode.get("tracks").get("items").get(0);
            return new ExternalApiResponseTrack(jsonNode.get("artists").get(0).get("name").asText(),
                    (jsonNode.get("name").asText()));
        } catch (Exception exception) {
            return null;
        }
    }
}


