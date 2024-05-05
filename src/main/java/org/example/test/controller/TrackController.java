package org.example.test.controller;

import org.example.test.entity.Tag;
import org.example.test.entity.Track;
import org.example.test.service.RequestCounterService;
import org.example.test.service.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:5173", "http://192.168.1.106:5173/", "*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE},
        allowedHeaders = {"Authorization", "Content-Type"})
@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    private final RequestCounterService counterService;

    public TrackController(TrackService trackService, RequestCounterService counterService) {
        this.trackService = trackService;
        this.counterService = counterService;
    }

    @GetMapping()
    public Optional<List<Track>> getAllTracks() {
        counterService.requestIncrement();
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public Optional<Track> getTrackById(@PathVariable Integer id) {
        counterService.requestIncrement();
        return trackService.getTrackById(id);
    }

    @PostMapping()
    public Track postTrack(@RequestParam String name,
                          @RequestParam String artist) {
        counterService.requestIncrement();
        return trackService.postTrack(new Track(name, artist));
    }

    @DeleteMapping("/{id}")
    public void deleteTrackById(@PathVariable Integer id) {
        counterService.requestIncrement();
        trackService.deleteTrackById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateTrackNameById(@PathVariable Integer id,
                                                      @RequestParam String newName) {
        counterService.requestIncrement();
        return trackService.updateTrackNameById(id, newName);
    }

    @PostMapping("/{trackId}/tags/{tagId}")
    public Tag addTagToTrack(@PathVariable Integer trackId, @PathVariable Integer tagId) {
        counterService.requestIncrement();
        return trackService.addTagToTrack(trackId, tagId);
    }

    @DeleteMapping("/{trackId}/tags/{tagId}")
    public void removeTagFromTrack(@PathVariable Integer trackId, @PathVariable Integer tagId) {
        counterService.requestIncrement();
        trackService.removeTagFromTrack(trackId, tagId);
    }
}
