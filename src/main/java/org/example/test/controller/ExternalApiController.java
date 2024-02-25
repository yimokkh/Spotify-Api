package org.example.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.test.dto.ExternalApiRequest;
import org.example.test.dto.ExternalApiResponse;
import org.example.test.service.ExternalApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService){
        this.externalApiService = externalApiService;
    }

    @GetMapping("/search")
    public ExternalApiResponse getByNameAndType(@RequestParam String name,
                                                @RequestParam String type) throws JsonProcessingException {
        return externalApiService.getByNameAndType(new ExternalApiRequest(name, type));
    }

}
