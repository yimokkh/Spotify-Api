package org.example.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.test.dto.ExternalApiRequest;
import org.example.test.dto.ExternalApiResponse;
import org.example.test.dto.ExternalApiToken;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    ExternalApiToken token = null;

    RestTemplate restTemplate;

    public ExternalApiService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    public String updateToken(){
        return "hello";
    }

    public ExternalApiResponse getByNameAndType(ExternalApiRequest request) throws JsonProcessingException {
        try {
            restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer BQBzp6HJEwCfQ-82MD8wGHrQM8DhZGbjsUsB63bSIBTelLMY3DiHhwVGrns0RVscK_ItbPBp3FgegVBDprOqTcZc3P78NT9a6gF2HhEIi5a8fcUkw7Q");
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String url = "https://api.spotify.com/v1/search?" + "q=" + request.getName() + "&type=" + request.getType() + "&limit=1";
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return new ExternalApiResponse(jsonNode.get("artists").get("items").get(0).get("name").asText(),
                    Integer.parseInt(jsonNode.get("artists").get("items").get(0).get("followers").get("total").asText()));
        } catch (Exception exception){
            return null;
        }
    }
}


// BQB3iWklopGVa7xhsZkez0D0MgwemLxKWOGPrIlGYbdHfQxZFiGQSMWAHMk1Oe16JdW18JGH9dvNDW3N4Lb4LV0NIvI3AFNyZm65TiBy8yJ2UP8Vf6k