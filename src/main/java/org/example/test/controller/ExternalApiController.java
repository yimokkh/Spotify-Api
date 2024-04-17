package org.example.test.controller;

import org.example.test.dto.ExternalApiRequest;
import org.example.test.dto.ExternalApiResponseArtist;
import org.example.test.dto.ExternalApiResponseTrack;

import org.example.test.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ExternalApiController {

    private final ExternalApiService externalApiService;

    public ExternalApiController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

    @GetMapping("/search/artists")
    public ExternalApiResponseArtist getArtistByName(@RequestParam String name)  {
        return externalApiService.getArtistByName(new ExternalApiRequest(name));
    }

    @GetMapping("/search/tracks")
    public ExternalApiResponseTrack getTrackByName(@RequestParam String name)  {
        return externalApiService.getTrackByName(new ExternalApiRequest(name));
    }

}
