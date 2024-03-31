package org.example.test.controller;

import org.example.test.entity.Track;
import org.example.test.service.TrackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService){
        this.trackService = trackService;
    }

    @GetMapping()
    public List<Track> getAllTracks(){
        return trackService.getAllTracks();
    }

    @PostMapping()
    public void postTrack(@RequestParam String name,
                          @RequestParam String artist){
        trackService.postTrack(new Track(name, artist));
    }

    @DeleteMapping("/{id}")
    public void deleteTrackById(@PathVariable Integer id) {
        trackService.deleteTrackById(id);
    }

    @PatchMapping("/{id}")
    public void updateTrackNameById(@PathVariable Integer id,
                                    @RequestParam String newName) {
        trackService.updateTrackNameById(id, newName);
    }
}
