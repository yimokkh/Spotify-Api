package org.example.test.controller;

import org.example.test.entity.Track;
import org.example.test.service.TrackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping()
    public Optional<List<Track>> getAllTracks() {
        return trackService.getAllTracks();
    }

    @GetMapping("/{id}")
    public Optional<Track> getTrackById(@PathVariable Integer id) {
        return trackService.getTrackById(id);
    }

    @PostMapping()
    public void postTrack(@RequestParam String name,
                          @RequestParam String artist) {
        trackService.postTrack(new Track(name, artist));
    }

    @DeleteMapping("/{id}")
    public void deleteTrackById(@PathVariable Integer id) {
        trackService.deleteTrackById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateTrackNameById(@PathVariable Integer id,
                                                      @RequestParam String newName) {
        return trackService.updateTrackNameById(id, newName);
    }

    @PostMapping("/{trackId}/tags/{tagId}")
    public void addTagToTrack(@PathVariable Integer trackId, @PathVariable Integer tagId) {
        trackService.addTagToTrack(trackId, tagId);
    }

    @DeleteMapping("/{trackId}/tags/{tagId}")
    public void removeTagFromTrack(@PathVariable Integer trackId, @PathVariable Integer tagId) {
        trackService.removeTagFromTrack(trackId, tagId);
    }
}
